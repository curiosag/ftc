package org.cg.ftc.ftcClientJava;

import java.io.File;

import org.cg.common.check.Check;
import org.cg.common.interfaces.OnValueChanged;
import org.cg.common.interfaces.Progress;
import org.cg.common.io.PreferencesStringStorage;
import org.cg.common.util.StringUtil;
import org.cg.ftc.shared.interfaces.Event;
import org.cg.ftc.shared.structures.ClientSettings;
import org.apache.commons.io.FileUtils;

public class CmdLineClient extends BaseClient {

	private static int EXIT_SUCCESS = 0;
	private static int EXIT_FAILURE = 1;
	private static boolean done = false;

	final Progress progress = createProgress();

	final ClientSettings clientSettings = ClientSettings.instance(GuiClient.class);
	final ftcClientModel model = new ftcClientModel(clientSettings);
	final ftcClientController controller = new ftcClientController(model, logging, getConnector(), clientSettings,
			new PreferencesStringStorage(org.cg.ftc.shared.uglySmallThings.Const.PREF_ID_CMDHISTORY,
					GuiClient.class),
			progress);

	private CmdLineClient _default = null;

	// quote issues with cmd line parameters:
	// http://stackoverflow.com/questions/4771755/how-to-pass-arguments-from-wrapper-shell-script-to-java-application

	public static int start(String inputFilePath, String outputFilePath) {

		try {
			File inputFile = new File(inputFilePath);
			if (!inputFile.isFile())
				abort("Invalid input file " + inputFilePath);

			String queries = FileUtils.readFileToString(inputFile);
			if (queries.length() == 0)
				error("Empty file " + inputFilePath);
			else {
				feedbackProcessingInfo(inputFilePath, outputFilePath);
				new CmdLineClient(queries, outputFilePath);
			}

		} catch (Exception e) {
			if (e instanceof GracefulCmdLineException)
				error(e.getMessage());
			else
				error(StringUtil.getStackTrace(e));
			return EXIT_FAILURE;
		}
		return EXIT_SUCCESS;
	}

	private CmdLineClient(String queries, String outputFilePath) {
		Check.isTrue(_default == null);
		_default = this;
		
		CmdLineFrontEnd frontEnd = new CmdLineFrontEnd(outputFilePath);
		setUp(clientSettings, model, controller, frontEnd);

		model.queryText.append(queries);

		controller.setOnAllQueriesProcessed(getOnAllQueriesProcessed());
		controller.authenticate();
		frontEnd.startAsync();

		if (allQueriesProcessed())
		{
			info("yo");
			System.out.flush();
		}
	}

	private static boolean allQueriesProcessed() {
		while (!done) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
		return true;
	}

	private static Event getOnAllQueriesProcessed() {
		return new Event() {
			@Override
			public void fire() {
				done = true;
			}
		};
	}

	@Override
	protected OnValueChanged<Integer> createQueryCaretChangedListener(final ftcClientModel model) {
		return new OnValueChanged<Integer>() {
			@Override
			public void notify(Integer value) {
			}
		};
	}

	private static void feedbackProcessingInfo(String inputFilePath, String outputFilePath) {
		info("Running queries in " + inputFilePath);
		if (outputFilePath != null)
			info("Writing results to " + outputFilePath);
	}

	static void abort(String reason) {
		throw new GracefulCmdLineException(reason);
	}

	static void info(String value) {
		System.out.println(value);
	}

	static void error(String value) {
		System.err.println(value);
	}

	
	private Progress createProgress() {
		return new Progress() {

			int max;

			@Override
			public void init(int max) {
				this.max = max;
			}

			@Override
			public void announce(int progress) {
				System.out.println(String.format("%d/%d", progress, max));
			}
		};
	}

}
