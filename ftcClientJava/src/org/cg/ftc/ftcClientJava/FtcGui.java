package org.cg.ftc.ftcClientJava;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;
import javax.swing.text.Document;

import org.cg.common.check.Check;
import org.cg.common.interfaces.AbstractKeyListener;
import org.cg.common.interfaces.OnValueChangedEvent;
import org.cg.common.misc.SimpleObservable;
import org.cg.common.swing.WindowClosingListener;
import org.cg.ftc.ftcQueryEditor.QueryEditor;
import org.cg.ftc.shared.interfaces.CompletionsSource;
import org.cg.ftc.shared.interfaces.SyntaxElementSource;
import org.cg.ftc.shared.structures.ClientSettings;
import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

public class FtcGui extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	public static final Dimension dimensionButtons = new Dimension(45, 22);
	

	private QueryEditor queryEditor;

	private JEditorPane opResult;

	private JTextField textFieldClientId;
	private JPasswordField textFieldClientSecret;
	private JSpinner fieldDefaultLimit;
	private JSpinner fieldAuthTimeout;

	JSplitPane splitPaneH;
	JSplitPane splitPaneV;
	JPanel authPanel;
	JButton buttonExecSql;
	JButton buttonCancel;
	JButton buttonAuthenticate;

	private JTable dataTable = null;

	private final SyntaxElementSource syntaxElements;
	private final CompletionsSource completionsSource;
	private final ClientSettings clientSettings;
	private ActionListener passOnactionListener = null;

	public FtcGui(SyntaxElementSource syntaxElements, CompletionsSource completionsSource,
			ClientSettings clientSettings) {

		this.syntaxElements = syntaxElements;
		this.completionsSource = completionsSource;
		this.clientSettings = clientSettings;

		buildGui();

		this.addWindowListener(new WindowClosingListener() {

			@Override
			public void windowClosing(WindowEvent e) {
				writeClientSettings();
			}
		});

	}

	public void setActionListener(ActionListener l) {
		passOnactionListener = l;
	}

	@Override // ActionListener
	public void actionPerformed(ActionEvent e) {
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
				getPassOnactionListener().actionPerformed(new ActionEvent(e.getSource(), e.getID(), actionId));
			}
		};
	}

	public void addQueryTextKeyListener(AbstractKeyListener k) {
		queryEditor.addKeyListener(k);
	}

	private ActionListener getPassOnactionListener() {
		Check.notNull(passOnactionListener);
		return passOnactionListener;
	}

	public Document opResultDocument() {
		return opResult.getDocument();
	}

	public Observer createClientIdObserver() {
		return Observism.createObserver(textFieldClientId);
	}

	public Observer createClientSecretObserver() {
		return Observism.createObserver(textFieldClientSecret);
	}

	public void addClientIdChangedListener(OnValueChangedEvent e) {
		Observism.addValueChangedListener(textFieldClientId, e);
	}

	public void addClientSecretChangedListener(OnValueChangedEvent e) {
		Observism.addValueChangedListener(textFieldClientSecret, e);
	}

	public Observer createOpResultObserver() {
		return Observism.createObserver(opResult);
	}

	public Observer createQueryObserver() {
		return Observism.createObserver(queryEditor.queryText);
	}

	public Observer createResultDataObserver() {
		return new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				Object value = SimpleObservable.getValue(o);
				Check.isTrue(value instanceof TableModel);
				TableModel model = (TableModel) value;
				dataTable.setModel(model);
			}
		};
	}

	private void buildGui() {
		setLayout(new BorderLayout());

		JScrollPane resultDataArea = createTableDisplay();
		JPanel buttonArea = createButtonArea();
		queryEditor = new QueryEditor(syntaxElements, completionsSource, clientSettings);
		JPanel resultextArea = createResultDisplay();
		JPanel textControlsPane = createSettingsArea();

		createSplitLayout(resultDataArea, buttonArea, resultextArea, textControlsPane);

		setMenu();

	}

	private void createSplitLayout(JScrollPane resultDataArea, JPanel buttonArea, JPanel resultextArea,
			JPanel textControlsPane) {
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

		frame0L.add(textControlsPane);
		frame0L.add(resultextArea);
		textControlsPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		resultextArea.setAlignmentX(Component.LEFT_ALIGNMENT);

		splitPaneH.setTopComponent(queryEditor);
		splitPaneH.setBottomComponent(resultDataArea);

		add(splitPaneV);
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
		runMenu.add(createMenuItem(KeyEvent.VK_F5, KeyEvent.VK_M, alt,
				getAction(Const.tooltipMemorizeQuery, Const.memorizeQuery)));
		runMenu.add(createMenuItem(KeyEvent.VK_F4, KeyEvent.VK_V, none,
				getAction(Const.tooltipViewPreprocessedQuery, Const.viewPreprocessedQuery)));
		runMenu.add(
				createMenuItem(KeyEvent.VK_F5, KeyEvent.VK_E, none, getAction(Const.tooltipExecSql, Const.execSql)));
		runMenu.add(createMenuItem(KeyEvent.VK_F5, KeyEvent.VK_C, ctrl,
				getAction(Const.tooltipCancelExecSql, Const.cancelExecSql)));
		runMenu.add(createMenuItem(KeyEvent.VK_F3, KeyEvent.VK_L, none,
				getAction(Const.tooltipListTables, Const.listTables)));
		runMenu.add(createMenuItem(KeyEvent.VK_LEFT, KeyEvent.VK_P, alt,
				getAction(Const.tooltipPreviousCommand, Const.previousCommand)));
		runMenu.add(createMenuItem(KeyEvent.VK_RIGHT, KeyEvent.VK_N, alt,
				getAction(Const.tooltipNextCommand, Const.nextCommand)));
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
		buttonCancel = createButton(Const.cancelExecSql, "cancel.png", Const.tooltipCancelExecSql);
		JButton buttonListTables = createButton(Const.listTables, "table.png", Const.tooltipListTables);
		JButton buttonPreview = createButton(Const.viewPreprocessedQuery, "control_play.png",
				Const.tooltipViewPreprocessedQuery);
		JButton buttonPrevCmd = createButton(Const.previousCommand, "arrow_left.png", Const.tooltipPreviousCommand);
		JButton buttonNextCmd = createButton(Const.nextCommand, "arrow_right.png", Const.tooltipNextCommand);
		JButton buttonRememberCmd = createButton(Const.memorizeQuery, "page_white_edit.png",
				Const.tooltipMemorizeQuery);
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
		buttonPane.add(buttonCancel);
		buttonPane.add(buttonListTables);

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
		URL url = getClass().getResource(Const.resourcePath +  iconUrl);
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

	public Document queryTextDocument() {
		return queryEditor.getDocument();
	}

	static FtcGui createAndShowGUI(SyntaxElementSource s, CompletionsSource c, ClientSettings clientSettings) {
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		FtcGui result = new FtcGui(s, c, clientSettings);
		result.setPreferredSize(new Dimension(clientSettings.width, clientSettings.height));
		result.setLocation(clientSettings.x, clientSettings.y);

		result.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		result.pack();
		result.setVisible(true);
		result.queryEditor.queryText.grabFocus();

		return result;
	}
}
