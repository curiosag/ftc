package org.cg.ftc.ftcClientJava;

import java.awt.event.ActionListener;
import java.util.Observer;

import javax.swing.event.DocumentListener;

import org.cg.common.interfaces.OnValueChangedEvent;

public interface FrontEnd {

	void setActionListener(ActionListener l);

	void addClientIdChangedListener(OnValueChangedEvent e);

	void addClientSecretChangedListener(OnValueChangedEvent e);

	void addResultTextChangedListener(DocumentListener listener);

	void addQueryTextChangedListener(DocumentListener listener);

	Observer createClientIdObserver();

	Observer createClientSecretObserver();
	
	Observer createOpResultObserver();

	Observer createQueryObserver();

	Observer createResultDataObserver();

}