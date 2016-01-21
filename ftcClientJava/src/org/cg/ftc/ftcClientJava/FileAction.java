package org.cg.ftc.ftcClientJava;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.cg.common.check.Check;
import org.cg.common.util.FileUtil;
import org.cg.ftc.shared.interfaces.OnFileAction;

public class FileAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	Component owner;
	private final OnFileAction onFileAction;
	private final JFileChooser fileChooser = new JFileChooser();
	private String defaultDirectory;

	public FileAction (String name, String defaultDirectory, Component owner, OnFileAction onFileAction)
	{
		super(name);
		this.owner = owner;
		Check.notNull(onFileAction);
		this.onFileAction = onFileAction;
		this.defaultDirectory = defaultDirectory;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		fileChooser.setCurrentDirectory(new File(defaultDirectory));
		int returnVal = fileChooser.showOpenDialog(owner);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File f = fileChooser.getSelectedFile();
			onFileAction.onFileAction(e, f);
			defaultDirectory = FileUtil.getPathOnly(f);
		}

	}

}
