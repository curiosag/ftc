package org.cg.ftc.ftcClientJava;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import org.cg.common.check.Check;
import org.cg.common.interfaces.OnTextFieldChangedEvent;
import org.cg.common.interfaces.OnValueChanged;
import org.cg.common.interfaces.Progress;
import org.cg.common.misc.SimpleObservable;
import org.cg.common.swing.WindowClosingListener;
import org.cg.common.util.Op;
import org.cg.ftc.ftcQueryEditor.QueryEditor;
import org.cg.ftc.shared.interfaces.CompletionsSource;
import org.cg.ftc.shared.interfaces.SyntaxElementSource;
import org.cg.ftc.shared.structures.ClientSettings;
import org.cg.ftc.shared.structures.RunState;
import org.cg.ftc.shared.uglySmallThings.Events;
import org.fife.ui.rtextarea.ConfigurableCaret;

import com.google.common.eventbus.Subscribe;

import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

public class FtcGui extends JFrame implements ActionListener, FrontEnd {

	private static final long serialVersionUID = 1L;
	private static final Dimension dimensionButtons = new Dimension(45, 22);

	private QueryEditor queryEditor;

	private JEditorPane opResult;

	private JTextField textFieldClientId;
	private JPasswordField textFieldClientSecret;
	private JSpinner fieldDefaultLimit;
	private JSpinner fieldAuthTimeout;

	private JSplitPane splitPaneH;
	private JSplitPane splitPaneV;
	private JPanel authPanel;
	private JButton buttonExecSql;
	private JButton buttonExecAllSql;
	private JButton buttonCancel;
	private JButton buttonAuthenticate;

	private JTable dataTable = null;

	private final SyntaxElementSource syntaxElements;
	private final CompletionsSource completionsSource;
	private final ClientSettings clientSettings;
	private ActionListener passOnactionListener = null;

	private final static int progressScale = org.cg.ftc.shared.uglySmallThings.Const.PROGRESS_SCALE;
	private int maxProgress = 0;
	private JProgressBar progressBar = new JProgressBar(0, progressScale);

	private final Progress progressMonitor = new Progress() {

		@Override
		public void init(int max) {
			maxProgress = max;
			setProgress(0);
		}

		@Override
		public void announce(final int progress) {
			setProgress(progress);
		}

		private void setProgress(final int progress) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					int val = progress == maxProgress ? progressScale : progressScale / maxProgress * progress;
					progressBar.setValue(val);
				}
			});
		}

	};

	public FtcGui(SyntaxElementSource syntaxElements, CompletionsSource completionsSource,
			ClientSettings clientSettings) {

		this.syntaxElements = syntaxElements;
		this.completionsSource = completionsSource;
		this.clientSettings = clientSettings;

		buildGui();

		this.addWindowListener(new WindowClosingListener() {

			@Override
			public void windowActivated(WindowEvent e) {
				actionPerformed(new ActionEvent(this, 0, Const.refreshTables));
			}

			@Override
			public void windowClosing(WindowEvent e) {
				writeClientSettings();
			}
		});

		registerForLongOperationEvent();
	}

	@Override
	public void setActionListener(ActionListener l) {
		passOnactionListener = l;
	}

	@Override // ActionListener
	public void actionPerformed(ActionEvent e) {
		hdlButtonActions(e);
	}

	private void hdlButtonActions(ActionEvent e) {
		getPassOnactionListener().actionPerformed(e);
	}

	private void writeClientSettings() {
		clientSettings.clientId = textFieldClientId.getText();
		clientSettings.clientSecret = String.valueOf(textFieldClientSecret.getPassword());
		clientSettings.dividerLocationH = splitPaneH.getDividerLocation();
		clientSettings.dividerLocationV = splitPaneV.getDividerLocation();
		clientSettings.x = getX();
		clientSettings.y = getY();
		clientSettings.width = getWidth();
		clientSettings.height = getHeight();
		clientSettings.defaultQueryLimit = getNumber(fieldDefaultLimit);
		clientSettings.authTimeout = getNumber(fieldAuthTimeout);
		clientSettings.write();
	}

	private int getNumber(JSpinner num) {
		return ((SpinnerNumberModel) num.getModel()).getNumber().intValue();
	};

	private AbstractAction getAction(String name, final String actionId) {
		return new AbstractAction(name) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals(Const.tooltipFocusEditor))
					queryEditor.requestFocus();
				else
					getPassOnactionListener().actionPerformed(new ActionEvent(e.getSource(), e.getID(), actionId));
			}
		};
	}

	private ActionListener getPassOnactionListener() {
		Check.notNull(passOnactionListener);
		return passOnactionListener;
	}

	@Override
	public Observer createClientIdObserver() {
		return Observism.createObserver(textFieldClientId);
	}

	@Override
	public Observer createClientSecretObserver() {
		return Observism.createObserver(textFieldClientSecret);
	}

	@Override
	public void addClientIdChangedListener(OnTextFieldChangedEvent e) {
		Observism.addValueChangedListener(textFieldClientId, e);
	}

	@Override
	public void addClientSecretChangedListener(OnTextFieldChangedEvent e) {
		Observism.addValueChangedListener(textFieldClientSecret, e);
	}

	@Override
	public void addResultTextChangedListener(DocumentListener listener) {
		opResult.getDocument().addDocumentListener(listener);
	}

	@Override
	public void addQueryTextChangedListener(DocumentListener listener) {
		queryEditor.getDocument().addDocumentListener(listener);
	}

	@Override
	public void addQueryCaretChangedListener(OnValueChanged<Integer> onChange) {
		queryEditor.queryText.getCaret().addChangeListener(createCaretChangedListener(onChange));
	}

	private ChangeListener createCaretChangedListener(final OnValueChanged<Integer> onChange) {
		return new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				Check.isTrue(e.getSource() instanceof ConfigurableCaret);
				ConfigurableCaret caret = (ConfigurableCaret) e.getSource();
				onChange.notify(caret.getDot());
			}
		};
	}

	@Override
	public Observer createOpResultObserver() {
		return Observism.createAppendingObserver(opResult);
	}

	@Override
	public Observer createQueryObserver() {
		return Observism.createObserver(queryEditor.queryText);
	}

	@Override
	public Observer createResultDataObserver() {
		return new Observer() {

			@Override
			public void update(final Observable o, Object arg) {

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						dataTable.setModel(Observism.decodeTableModelObservable(o));
					}
				});

			}
		};
	}

	private void buildGui() {
		setLayout(new BorderLayout());

		JScrollPane resultDataArea = createTableDisplay();
		JPanel buttonArea = createButtonArea();
		queryEditor = new QueryEditor(syntaxElements, completionsSource, clientSettings);
		JPanel resultextArea = createResultDisplay();
		JPanel textControlsPanel = createSettingsArea();
		JPanel statusPanel = createStatusPanel();

		createSplitLayout(resultDataArea, buttonArea, resultextArea, textControlsPanel, statusPanel);

		setMenu();

	}

	private JPanel createStatusPanel() {
		JPanel result = new JPanel();
		BoxLayout layout = new BoxLayout(result, BoxLayout.LINE_AXIS);
		result.setLayout(layout);
		result.add(progressBar);
		return result;
	}

	private void createSplitLayout(JScrollPane resultDataArea, JPanel buttonArea, JPanel resultextArea,
			JPanel textControlsPanel, JPanel statusPanel) {

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		splitPaneV = createSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPaneV.setDividerLocation(clientSettings.dividerLocationV);

		JPanel frame0L = new JPanel();
		frame0L.setLayout(new BoxLayout(frame0L, BoxLayout.Y_AXIS));

		JPanel frame0R = new JPanel();
		frame0R.setLayout(new BoxLayout(frame0R, BoxLayout.Y_AXIS));

		splitPaneH = createSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPaneH.setDividerLocation(clientSettings.dividerLocationH);

		frame0R.add(buttonArea);
		frame0R.add(splitPaneH);

		buttonArea.setAlignmentX(Component.LEFT_ALIGNMENT);
		splitPaneH.setAlignmentX(Component.LEFT_ALIGNMENT);

		splitPaneV.add(frame0L, JSplitPane.LEFT);
		splitPaneV.add(frame0R, JSplitPane.RIGHT);

		frame0L.add(textControlsPanel);
		frame0L.add(resultextArea);
		textControlsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		resultextArea.setAlignmentX(Component.LEFT_ALIGNMENT);

		splitPaneH.setTopComponent(queryEditor);
		splitPaneH.setBottomComponent(resultDataArea);

		mainPanel.add(splitPaneV);
		mainPanel.add(statusPanel);
		add(mainPanel);
	}

	private JSplitPane createSplitPane(int orientation) {
		JSplitPane frame0 = new JSplitPane();
		frame0.setOrientation(orientation);
		frame0.setDividerSize(2);
		return frame0;
	}

	private void setMenu() {
		int ctrl = ActionEvent.CTRL_MASK;
		int alt = ActionEvent.ALT_MASK;
		int none = 0;

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.add(createMenuItem(KeyEvent.VK_O, KeyEvent.VK_O, ctrl, getAction("Open", Const.fileOpen)));
		fileMenu.add(createMenuItem(KeyEvent.VK_S, KeyEvent.VK_S, ctrl, getAction("Save", Const.fileSave)));
		fileMenu.add(
				createMenuItem(KeyEvent.VK_E, KeyEvent.VK_E, ctrl, getAction(Const.tooltipExportCsv, Const.exportCsv)));
		menuBar.add(fileMenu);

		menuBar.add(queryEditor.getMenu());

		JMenu runMenu = new JMenu("Run");
		runMenu.setMnemonic(KeyEvent.VK_R);
		runMenu.add(
				createMenuItem(KeyEvent.VK_F5, KeyEvent.VK_E, none, getAction(Const.tooltipExecSql, Const.execSql)));
		runMenu.add(createMenuItem(KeyEvent.VK_F7, KeyEvent.VK_A, none,
				getAction(Const.tooltipExecAllSql, Const.execAllSql)));
		runMenu.add(createMenuItem(KeyEvent.VK_F5, KeyEvent.VK_C, ctrl,
				getAction(Const.tooltipCancelExecSql, Const.cancelExecution)));
		runMenu.add(createMenuItem(KeyEvent.VK_F5, KeyEvent.VK_V, alt,
				getAction(Const.tooltipViewPreprocessedQuery, Const.viewPreprocessedQuery)));

		runMenu.add(createMenuItem(KeyEvent.VK_LEFT, KeyEvent.VK_P, alt,
				getAction(Const.tooltipPreviousCommand, Const.previousCommand)));
		runMenu.add(createMenuItem(KeyEvent.VK_RIGHT, KeyEvent.VK_N, alt,
				getAction(Const.tooltipNextCommand, Const.nextCommand)));
		runMenu.add(createMenuItem(KeyEvent.VK_M, KeyEvent.VK_M, alt,
				getAction(Const.tooltipMemorizeQuery, Const.memorizeQuery)));

		runMenu.add(createMenuItem(KeyEvent.VK_T, KeyEvent.VK_T, ctrl,
				getAction(Const.tooltipListTables, Const.listTables)));

		runMenu.add(createMenuItem(KeyEvent.VK_F, KeyEvent.VK_F12, ctrl,
				getAction(Const.tooltipFocusEditor, Const.focusEditor)));

		runMenu.add(createMenuItem(KeyEvent.VK_R, KeyEvent.VK_R, ctrl,
				getAction(Const.tooltipRefreshTables, Const.refreshTables)));

		menuBar.add(runMenu);

		setJMenuBar(menuBar);
		menuBar.setVisible(true);
	}

	private JMenuItem createMenuItem(int keyEvent, int mnemonic, int keyMask, Action action) {
		JMenuItem result = new JMenuItem(action);
		result.setAccelerator(KeyStroke.getKeyStroke(keyEvent, keyMask));
		result.setMnemonic(mnemonic);
		return result;
	}

	private JScrollPane createTableDisplay() {
		dataTable = new JTable();
		JScrollPane scrollPane = new JScrollPane(dataTable);
		dataTable.setFillsViewportHeight(true);
		return scrollPane;
	}

	private JEditorPane createEditorPane() {
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(true);
		editorPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
		return editorPane;
	}

	private JPanel createButtonArea() {

		buttonExecSql = createButton(Const.execSql, "control_play_blue.png", Const.tooltipExecSql);
		buttonExecAllSql = createButton(Const.execAllSql, "control_fastforward_blue.png",
				Const.tooltipExecAllSql);
		buttonCancel = createButton(Const.cancelExecution, "cancel.png", Const.tooltipCancelExecSql);
		JButton buttonListTables = createButton(Const.listTables, "table.png", Const.tooltipListTables);
		JButton buttonPreview = createButton(Const.viewPreprocessedQuery, "control_play.png",
				Const.tooltipViewPreprocessedQuery);
		JButton buttonPrevCmd = createButton(Const.previousCommand, "arrow_left.png", Const.tooltipPreviousCommand);
		JButton buttonNextCmd = createButton(Const.nextCommand, "arrow_right.png", Const.tooltipNextCommand);
		JButton buttonRememberCmd = createButton(Const.memorizeQuery, "page_white_edit.png",
				Const.tooltipMemorizeQuery);
		JButton buttonRefreshTablesCmd = createButton(Const.refreshTables, "arrow_refresh_small.png",
				Const.tooltipRefreshTables);
		JButton buttonExportCsvCmd = createButton(Const.exportCsv, "page_save.png", Const.tooltipExportCsv);

		JPanel buttonPane = new JPanel(new MigLayout());

		final int spacerWidht = 15;
		buttonPane.add(createSpacer(spacerWidht));
		buttonPane.add(buttonPrevCmd);
		buttonPane.add(buttonNextCmd);
		buttonPane.add(buttonRememberCmd);
		buttonPane.add(createSpacer(spacerWidht));

		buttonPane.add(buttonPreview);
		buttonPane.add(buttonExecSql);
		buttonPane.add(buttonExecAllSql);
		buttonPane.add(buttonCancel);
		buttonPane.add(createSpacer(spacerWidht));

		buttonPane.add(buttonListTables);
		buttonPane.add(buttonRefreshTablesCmd);
		buttonPane.add(createSpacer(spacerWidht));
		buttonPane.add(buttonExportCsvCmd);

		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 2, 2, 0));

		return buttonPane;
	}

	private Component createSpacer(int width) {
		return Box.createRigidArea(new Dimension(width, 0));
	}

	private JButton createButton(String actionCommand, String iconUrl, String tipText) {
		JButton b = new JButton(createIcon(iconUrl));

		b.addActionListener(this);
		b.setActionCommand(actionCommand);
		b.setToolTipText(tipText);
		b.setMaximumSize(dimensionButtons);
		return b;
	}

	private ImageIcon createIcon(String iconUrl) {
		URL url = getClass().getResource(Const.resourcePath + iconUrl);
		return new ImageIcon(url);
	}

	private JPanel createSettingsArea() {
		final int hightSpinners = 18;

		textFieldClientId = new JTextField(26);
		textFieldClientSecret = new JPasswordField(26);
		textFieldClientSecret.setEchoChar('*');
		buttonAuthenticate = createButton(Const.authorize, "key.png", Const.tooltipAuthorize);
		buttonAuthenticate.setMaximumSize(dimensionButtons);
		SpinnerNumberModel defaultLimitNumberModel = createNumberModel(clientSettings.defaultQueryLimit,
				createDefaultLimitChangeListener());
		fieldDefaultLimit = new JSpinner(defaultLimitNumberModel);
		fieldDefaultLimit.setEditor(new JSpinner.NumberEditor(fieldDefaultLimit));
		fieldAuthTimeout = new JSpinner(
				createNumberModel(clientSettings.authTimeout, createAuthTimeoutChangeListener()));
		fieldAuthTimeout.setEditor(new JSpinner.NumberEditor(fieldAuthTimeout));
		fieldAuthTimeout.setPreferredSize(new Dimension(30, hightSpinners));

		defaultLimitNumberModel.addChangeListener(createDefaultLimitChangeListener());

		ItemListener showPasswordListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				setPasswordVisible(e.getStateChange() == ItemEvent.SELECTED);
			}
		};

		authPanel = new JPanel(new MigLayout("wrap 3"));
		authPanel.add(buttonAuthenticate, "gapleft 5");
		authPanel.add(new JLabel("Auth timeout"));
		authPanel.add(fieldAuthTimeout);

		authPanel.add(textFieldClientId, "span 3");

		authPanel.add(new JLabel("Client id", createIcon("bullet_arrow_up.png"), JLabel.RIGHT));
		authPanel.add(new JLabel("Client secret", createIcon("bullet_arrow_down.png"), JLabel.LEFT));

		JCheckBox view = new JCheckBox("show");
		view.addItemListener(showPasswordListener);
		authPanel.add(view, "wrap");

		authPanel.add(textFieldClientSecret, "span 3");

		authPanel.add(new JLabel("Query limit"));
		authPanel.add(fieldDefaultLimit, "wrap");
		fieldDefaultLimit.setMinimumSize(new Dimension(100, hightSpinners));

		authPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 0, 2));
		return authPanel;
	}

	private SpinnerNumberModel createNumberModel(int initValue, ChangeListener l) {
		SpinnerNumberModel result = new SpinnerNumberModel(initValue, 0, 100000, 1);
		result.addChangeListener(l);
		return result;
	}

	private ChangeListener createDefaultLimitChangeListener() {
		return new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				clientSettings.defaultQueryLimit = getNumber(fieldDefaultLimit);
			}
		};
	}

	private ChangeListener createAuthTimeoutChangeListener() {
		return new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				clientSettings.authTimeout = getNumber(fieldAuthTimeout);
			}
		};
	}

	private void setPasswordVisible(boolean value) {
		if (value)
			textFieldClientSecret.setEchoChar((char) 0);
		else
			textFieldClientSecret.setEchoChar('*');
	}

	private JPanel createResultDisplay() {
		opResult = createEditorPane();
		JScrollPane opResultScrollPane = new JScrollPane(opResult);

		JPanel resultPane = new JPanel(new BorderLayout());
		resultPane.add(opResultScrollPane, BorderLayout.CENTER);

		resultPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		resultPane.setPreferredSize(new Dimension(300, 600));

		return resultPane;
	}

	static FrontEnd createAndShowGUI(SyntaxElementSource s, CompletionsSource c, ClientSettings clientSettings) {
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		FtcGui result = new FtcGui(s, c, clientSettings);
		result.setPreferredSize(new Dimension(clientSettings.width, clientSettings.height));
		result.setLocation(clientSettings.x, clientSettings.y);

		result.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		result.pack();
		result.setVisible(true);
		result.queryEditor.requestFocus();

		return result;
	}

	@Override
	public Progress getProgressMonitor() {
		return progressMonitor;
	}

	private void registerForLongOperationEvent() {
		Events.ui.register(this);
	}

	@Subscribe
	public void eventBusOnLongOperation(RunState state) {
		enableStartElements(! Op.in(state, RunState.AUTHENTICATION_STARTED, RunState.QUERYEXEC_STARTED));
	}

	private void enableStartElements(boolean enable) {
		buttonExecSql.setEnabled(enable);
		buttonExecAllSql.setEnabled(enable);
		buttonAuthenticate.setEnabled(enable);
	}

}
