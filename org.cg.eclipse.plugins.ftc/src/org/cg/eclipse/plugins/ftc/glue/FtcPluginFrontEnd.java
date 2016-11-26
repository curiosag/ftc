package org.cg.eclipse.plugins.ftc.glue;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.cg.common.check.Check;
import org.cg.common.core.Logging;
import org.cg.common.interfaces.OnTextFieldChangedEvent;
import org.cg.common.interfaces.OnValueChanged;
import org.cg.common.interfaces.Progress;
import org.cg.common.io.FileUtil;
import org.cg.eclipse.plugins.ftc.FtcEditor;
import org.cg.eclipse.plugins.ftc.PluginConst;
import org.cg.eclipse.plugins.ftc.WorkbenchUtil;
import org.cg.eclipse.plugins.ftc.view.ResultView;
import org.cg.ftc.ftcClientJava.FrontEnd;
import org.cg.ftc.ftcClientJava.Observism;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import com.google.common.base.Optional;

public class FtcPluginFrontEnd implements FrontEnd {

	private final IPreferenceStore preferenceStore;
	private final Progress progress;

	private final Logging logging;
	private Optional<FtcEditor> activeEditor = Optional.absent();
	private final List<FtcEditor> editors = new LinkedList<FtcEditor>();

	private boolean offline;
	private OnTextFieldChangedEvent queryTextChangedListener;
	private OnValueChanged<Integer> queryCaretChangedListener;

	FtcPluginFrontEnd(IPreferenceStore preferenceStore, Progress progress, Logging logging) {
		this.preferenceStore = preferenceStore;
		this.progress = progress;
		this.logging = logging;
	}

	public boolean activeEditorPresent() {
		return activeEditor.isPresent();
	}

	public void registerEditor(FtcEditor e) {
		if (editors.indexOf(e) < 0){
			editors.add(e);
			Check.notNull(queryCaretChangedListener);
			e.setCaretListener(queryCaretChangedListener);
			e.setDocumentChangedListener(queryTextChangedListener);
		}
	}

	public void unRegisterEditor(FtcEditor e) {
		editors.remove(e);
		activeEditor = Optional.absent();
	}

	public void onEditorActivated(IWorkbenchPart e) {
		activeEditor = Optional.of((FtcEditor) e);
	}
	
	public void hdlExportCsv() {
		String delim = preferenceStore.getString(FtcPreferenceStore.KEY_CSV_DELIMITER);
		String quote = preferenceStore.getString(FtcPreferenceStore.KEY_CSV_QUOTECHAR);
		String csvData = getResultView().getCsv(delim, quote);

		FileDialog dialog = new FileDialog(WorkbenchUtil.getShell(), SWT.SAVE);

		dialog.setFilterPath(preferenceStore.getString(FtcPreferenceStore.KEY_LAST_EXPORT_PATH));
		dialog.setFilterNames(new String[] { "csv files", "All Files (*.*)" });
		dialog.setFilterExtensions(new String[] { "*.csv", "*.*" });
		String fullPath = dialog.open();

		if (fullPath != null) {
			File f = new File(fullPath);
			String path = f.getPath();
			FileUtil.writeToFile(csvData, fullPath);
			preferenceStore.setValue(FtcPreferenceStore.KEY_LAST_EXPORT_PATH, path);
		}
	}

	private ResultView getResultView() {
		IViewPart view = WorkbenchUtil.showView(PluginConst.RESULT_VIEW_ID);
		Check.isTrue(view instanceof ResultView);
		return (ResultView) view;
	}

	@Override
	public Progress getProgressMonitor() {
		return progress;
	}

	@Override
	public void setActionListener(ActionListener l) {
		// handled by FtcPluginClient
	}

	@Override
	public void addClientIdChangedListener(OnTextFieldChangedEvent e) {
		preferenceStore.addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(FtcPreferenceStore.KEY_CLIENT_ID))
					e.notify(Unbox.asString(event.getNewValue()));
			}
		});
	}

	@Override
	public void addClientSecretChangedListener(OnTextFieldChangedEvent e) {
		preferenceStore.addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(FtcPreferenceStore.KEY_CLIENT_SECRET))
					e.notify(Unbox.asString(event.getNewValue()));
			}
		});
	}

	@Override
	public void addResultTextChangedListener(OnTextFieldChangedEvent e) {
	}

	@Override
	public void addQueryTextChangedListener(OnTextFieldChangedEvent e) {
		Check.notNull(e);
		this.queryTextChangedListener = e;
		editors.forEach(f -> f.setDocumentChangedListener(e));
	}

	@Override
	public void addQueryCaretChangedListener(OnValueChanged<Integer> onChange) {
		Check.notNull(onChange);
		this.queryCaretChangedListener = onChange;
		editors.forEach(f -> f.setCaretListener(onChange));
	}

	@Override
	public Observer createClientIdObserver() {
		return Observism.unObserver;
	}

	@Override
	public Observer createClientSecretObserver() {
		return Observism.unObserver;
	}

	@Override
	public Observer createOpResultObserver() {
		return new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						logging.Info(Observism.decodeTextModelObservable(o));
					}
				});

			}
		};
	}

	@Override
	public Observer createQueryObserver() {
		return Observism.unObserver;
	}

	@Override
	public Observer createResultDataObserver() {
		return new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						getResultView().displayTable(Observism.decodeTableModelObservable(o));
					}
				});
			}
		};

	}

	private void refreshEditors() {
		for (FtcEditor e : editors)
			e.invalidateTextRepresentation();
	}

	@Override
	public void addOfflineChangedListener(OnValueChanged<Boolean> onChange) {
		preferenceStore.addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(FtcPreferenceStore.KEY_OFFLINE)) {
					boolean recent = offline;
					offline = Unbox.asBoolean(event.getNewValue());
					if (recent != offline) {
						onChange.notify(offline);
						refreshEditors();
						if (offline)
							logging.Info("working offline");
					}
				}
			}
		});

	}

}
