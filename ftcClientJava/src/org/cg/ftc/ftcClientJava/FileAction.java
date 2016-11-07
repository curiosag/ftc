package org.cg.ftc.ftcClientJava;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.cg.common.check.Check;
import org.cg.common.io.FileUtil;
import org.cg.ftc.shared.interfaces.OnFileAction;

import com.google.common.base.Optional;

public class FileAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	public final static String FILE_OPEN = "Open";
	public static final String FILE_SAVE = "SAVE";
	public final static String FILE_SAVE_AS = "Save as";
	public static final String EXPORT_FILE = "Export csv";


	Component owner;
	private final OnFileAction onFileAction;
	private final JFileChooser fileChooser = new JFileChooser();
	private String defaultDirectory;
	private final String name;

	public FileAction(String name, String defaultDirectory, Component owner, OnFileAction onFileAction) {
		super(name);
		this.name = name;
		this.owner = owner;
		Check.notNull(onFileAction);
		this.onFileAction = onFileAction;
		this.defaultDirectory = defaultDirectory;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		fileChooser.setCurrentDirectory(new File(defaultDirectory));
		int returnVal = JFileChooser.CANCEL_OPTION;
		Optional<File> result = Optional.absent();
		
		if (name.equals(FILE_OPEN))
			returnVal = fileChooser.showOpenDialog(owner);
		else 
			returnVal = fileChooser.showSaveDialog(owner);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File f = fileChooser.getSelectedFile();
			defaultDirectory = FileUtil.getPathOnly(f);
			result = Optional.of(f);
		}
		onFileAction.onFileAction(e, result);
	}

}
