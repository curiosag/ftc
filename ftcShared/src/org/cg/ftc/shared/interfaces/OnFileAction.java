package org.cg.ftc.shared.interfaces;

import java.awt.event.ActionEvent;
import java.io.File;

import com.google.common.base.Optional;

public interface OnFileAction {
	public void onFileAction(ActionEvent e, Optional<File> file);
}
