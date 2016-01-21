package org.cg.ftc.ftcClientJava;

import javax.swing.table.TableModel;

import org.cg.common.check.Check;
import org.cg.common.misc.SimpleObservable;
import org.cg.ftc.shared.structures.ClientSettings;

public class ftcClientModel {

	public final TextModel clientId = new TextModel();
	public final TextModel clientSecret = new TextModel();
	
	public final TextModel queryText = new TextModel();
	public final TextModel resultText = new TextModel();

	public final SimpleObservable<TableModel> resultData = new SimpleObservable<TableModel>(null);

	public ftcClientModel(ClientSettings clientSettings) {
		Check.notNull(clientSettings);
		clientId.setValue(clientSettings.clientId);
		clientSecret.setValue(clientSettings.clientSecret);
	}

}
