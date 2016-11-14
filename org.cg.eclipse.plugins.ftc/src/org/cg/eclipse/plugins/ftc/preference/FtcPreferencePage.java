package org.cg.eclipse.plugins.ftc.preference;

import org.eclipse.jface.preference.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.cg.eclipse.plugins.ftc.glue.FtcPluginClient;
import org.cg.eclipse.plugins.ftc.glue.FtcPreferenceStore;

public class FtcPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public FtcPreferencePage() {
		super(GRID);
		setPreferenceStore(FtcPluginClient.getDefault().getPreferenceStore());
		setDescription("Fusion Tables Console");
	}

	public void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		StringFieldEditor fldClientId = new StringFieldEditor(FtcPreferenceStore.KEY_CLIENT_ID, "Client &Id", parent);
		StringFieldEditor fldClientSecret = new StringFieldEditor(FtcPreferenceStore.KEY_CLIENT_SECRET,
				"Client &Secret", getFieldEditorParent());
		IntegerFieldEditor fldAuthTimeout = new IntegerFieldEditor(FtcPreferenceStore.KEY_CLIENT_AUTHTIMEOUT, "&Authentication Timeout",
				getFieldEditorParent());
		addField(fldClientId);
		addField(fldClientSecret);
		addField(fldAuthTimeout);

		Button authenticate = new Button(getFieldEditorParent(), SWT.PUSH);
		authenticate.setText("Authenticate");
		authenticate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getPreferenceStore().setValue(FtcPreferenceStore.KEY_CLIENT_ID, fldClientId.getStringValue());
				getPreferenceStore().setValue(FtcPreferenceStore.KEY_CLIENT_SECRET, fldClientSecret.getStringValue());
				getPreferenceStore().setValue(FtcPreferenceStore.KEY_CLIENT_AUTHTIMEOUT, fldAuthTimeout.getIntValue());
				FtcPluginClient.getDefault().reAuthenticate();
			}
		});

		new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
		
		addField(new IntegerFieldEditor(FtcPreferenceStore.KEY_CLIENT_QUERYLIMIT, "&Query Result Limit",
				getFieldEditorParent()));

		new Label(parent, SWT.LEFT).setText("Csv export settings");
		new Label(parent, SWT.LEFT);

		addField(new StringFieldEditor(FtcPreferenceStore.KEY_CSV_DELIMITER, "Delimiter", parent));
		addField(new StringFieldEditor(FtcPreferenceStore.KEY_CSV_QUOTECHAR, "Quote character", parent));

		new Label(parent, SWT.LEFT);

		addField(new BooleanFieldEditor(FtcPreferenceStore.KEY_OFFLINE, "Work &offline", getFieldEditorParent()));
		addField(new BooleanFieldEditor(FtcPreferenceStore.KEY_SHOW_CARETPOS, "Show &caret position",
				getFieldEditorParent()));
		new Label(parent, SWT.LEFT);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

}