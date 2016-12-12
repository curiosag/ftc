package org.cg.ftc.ftcClientJava;

import java.awt.event.ActionEvent;
import java.io.File;

import org.cg.common.core.AbstractLogger;
import org.cg.common.interfaces.Progress;
import org.cg.common.io.FileUtil;
import org.cg.common.io.StringStorage;
import org.cg.ftc.shared.interfaces.Connector;
import org.cg.ftc.shared.interfaces.OnFileAction;
import org.cg.ftc.shared.structures.ClientSettings;
import org.cg.ftc.shared.uglySmallThings.CSV;

import com.google.common.base.Optional;

public class FtcGuiController extends ClientController{

	private Optional<File> lastFileUsed = Optional.absent();
	
	public FtcGuiController(ClientModel model, AbstractLogger logging, Connector connector,
			ClientSettings clientSettings, StringStorage cmdHistoryStorage, Progress progress) {
		super(model, logging, connector, clientSettings, cmdHistoryStorage, progress);
		
	}
	
	@Override
	protected void hdlFileOpen() {
		new FileAction(FileAction.FILE_OPEN, clientSettings.pathScriptFile, null, new OnFileAction() {

			@Override
			public void onFileAction(ActionEvent e, Optional<File> file) {
				if (file.isPresent()) {
					model.queryText.setValue(FileUtil.readFromFile(file.get().getPath()));
					clientSettings.pathScriptFile = FileUtil.getPathOnly(file.get());
					lastFileUsed = file;
				}
			}

		}).actionPerformed(null);
	}

	@Override
	protected void hdlFileSave() {
		if (lastFileUsed.isPresent())
			FileUtil.writeToFile(model.queryText.getValue(), lastFileUsed.get().getPath());
		else
			hdlFileSaveAs();
	}

	@Override
	protected void hdlFileSaveAs() {
		new FileAction(FileAction.FILE_SAVE_AS, clientSettings.pathScriptFile, null, new OnFileAction() {

			@Override
			public void onFileAction(ActionEvent e, Optional<File> file) {
				if (file.isPresent()) {
					FileUtil.writeToFile(model.queryText.getValue(), file.get().getPath());
					lastFileUsed = file;
					clientSettings.pathScriptFile = FileUtil.getPathOnly(file.get());
				}
			}
		}).actionPerformed(null);
	}

	@Override
	protected void hdlExportCsvAction(ActionEvent e) {
		if (tablePopulated()) {
			new FileAction(FileAction.EXPORT_FILE, clientSettings.pathCsvFile, null, new OnFileAction() {

				@Override
				public void onFileAction(ActionEvent e, Optional<File> file) {
					if (file.isPresent()) {
						clientSettings.pathCsvFile = FileUtil.getPathOnly(file.get());
						logging.Info((new CSV()).write(model.resultData.getValue(), file.get().getPath()));
					}
				}
			}).actionPerformed(e);
		} else
			logging.Info("no data to export");
	}

	private boolean tablePopulated() {
		return model.resultData.getValue() != null && model.resultData.getValue().getRowCount() > 0;
	}

}
