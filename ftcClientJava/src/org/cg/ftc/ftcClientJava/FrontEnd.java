package org.cg.ftc.ftcClientJava;

import java.awt.event.ActionListener;
import java.util.Observer;

import javax.swing.event.DocumentListener;

import org.cg.common.interfaces.OnTextFieldChangedEvent;
import org.cg.common.interfaces.OnValueChanged;
import org.cg.common.interfaces.Progress;

public interface FrontEnd {

	Progress getProgressMonitor();

	void setActionListener(ActionListener l);

	void addClientIdChangedListener(OnTextFieldChangedEvent e);

	void addClientSecretChangedListener(OnTextFieldChangedEvent e);

	void addResultTextChangedListener(DocumentListener listener);

	void addQueryTextChangedListener(DocumentListener listener);
	
	void addQueryCaretChangedListener(OnValueChanged<Integer> onChange);

	Observer createClientIdObserver();

	Observer createClientSecretObserver();
	
	Observer createOpResultObserver();

	Observer createQueryObserver();

	Observer createResultDataObserver();

}