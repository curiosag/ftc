package org.cg.ftc.ftcClientJava;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.TableModel;

import org.cg.common.check.Check;
import org.cg.common.interfaces.OnTextFieldChangedEvent;
import org.cg.common.interfaces.OnValueChanged;
import org.cg.common.interfaces.Progress;
import org.cg.common.io.CustomCloseActionWriterDecorator;
import org.cg.common.io.WriterCloseAction;
import org.cg.ftc.shared.uglySmallThings.CSV;

public class CmdLineFrontEnd implements FrontEnd {
	ActionListener actionListener = null;

	private final String outputFilePath;

	public CmdLineFrontEnd(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}

	@SuppressWarnings("serial")
	public void startAsync() {
		Check.notNull(actionListener);
		actionListener.actionPerformed(new ActionEvent(this, 0, Const.execAllSql) {
		});
	}

	@Override
	public Progress getProgressMonitor() {
		return null;
	}

	@Override
	public void setActionListener(ActionListener l) {
		actionListener = l;
	}

	@Override
	public void addClientIdChangedListener(OnTextFieldChangedEvent e) {
	}

	@Override
	public void addClientSecretChangedListener(OnTextFieldChangedEvent e) {
	}

	@Override
	public void addResultTextChangedListener(OnTextFieldChangedEvent e){
	}

	@Override
	public void addQueryTextChangedListener(OnTextFieldChangedEvent e) {
	}

	@Override
	public void addQueryCaretChangedListener(OnValueChanged<Integer> onChange) {
	}

	@Override
	public void addOfflineChangedListener(OnValueChanged<Boolean> onChange) {	
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
				CmdLineClient.info(Observism.decodeTextModelObservable(o));
			}
		};
	}

	@Override
	public Observer createQueryObserver() {
		return Observism.unObserver;
	}

	private final static WriterCloseAction dontClose = null;

	@Override
	public Observer createResultDataObserver() {
		return new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				TableModel data = Observism.decodeTableModelObservable(o);
				CSV csv = new CSV();
				if (outputFilePath != null)
					csv.write(data, outputFilePath);
				else
					// System.out must not get closed, therefore packed in
					// CustomCloseActionWriterDecorator with doesen't close
					// Even if OutputStreamWriter creates a leak (which it may
					// not) it doesen't matter so much here
					csv.write(data, new BufferedWriter(
							new CustomCloseActionWriterDecorator(new OutputStreamWriter(System.out), dontClose)));
			}
		};
	}


}
