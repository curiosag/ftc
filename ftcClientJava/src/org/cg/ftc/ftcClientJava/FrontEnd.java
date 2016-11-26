package org.cg.ftc.ftcClientJava;

import java.awt.event.ActionListener;
import java.util.Observer;

import org.cg.common.interfaces.OnTextFieldChangedEvent;
import org.cg.common.interfaces.OnValueChanged;
import org.cg.common.interfaces.Progress;

public interface FrontEnd {

	Progress getProgressMonitor();

	void setActionListener(ActionListener l);

	void addClientIdChangedListener(OnTextFieldChangedEvent e);

	void addClientSecretChangedListener(OnTextFieldChangedEvent e);

	void addResultTextChangedListener(OnTextFieldChangedEvent e);

	void addQueryTextChangedListener(OnTextFieldChangedEvent e);
	
	void addQueryCaretChangedListener(OnValueChanged<Integer> onChange);
	
	void addOfflineChangedListener(OnValueChanged<Boolean> onChange);

	Observer createClientIdObserver();

	Observer createClientSecretObserver();
	
	Observer createOpResultObserver();

	Observer createQueryObserver();

	Observer createResultDataObserver();

}