package org.cg.ftc.ftcClientCodeCompletionExperiments;

/**
* The MIT License
* Copyright (c) 2007 Collin Fagan
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.cg.common.check.Check;
import org.cg.ftc.shared.interfaces.SqlCompletionType;
import org.cg.ftc.shared.structures.AbstractCompletion;
import org.cg.ftc.shared.structures.ModelElementCompletion;

import com.google.common.base.Optional;

/**
 * 
 * DefaultTreeModelDemoPanel
 * 
 * This class is intended to demonstrate the right and wrong ways to manipulate
 * a DefaultTreeModel.
 * 
 * @author Collin Fagan
 * 
 */

public class TreeNamePicker extends JPanel implements WindowListener, KeyEventHandler {
	private static final long serialVersionUID = 1L;
	private final JTree tree;
	private final DefaultMutableTreeNode root;

	private final ItemChosenHandler onChosen;
	private JPopupMenu menu = new JPopupMenu("Operations");

	public TreeNamePicker(DefaultMutableTreeNode root, Optional<String> prefixTable, ItemChosenHandler onChosen) {
		Check.notNull(onChosen);
		Check.notNull(prefixTable);
		this.onChosen = onChosen;
		this.root = root;

		if (root != null)
			tree = new JTree(root);
		else
			tree = new JTree();

		expandForPrefix(tree, prefixTable);

		setLayout(new BorderLayout());
		add(new JScrollPane(tree));

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(false);
		tree.addMouseListener(treeMenuClicked);

		JMenuItem removeNode = new JMenuItem("Remove from node - [Will NOT update correctly]");
		removeNode.setToolTipText("Will NOT update correctly");

		JMenuItem removeModel = new JMenuItem("Remove from model");
		removeModel.setToolTipText("Will update correctly");
		removeModel.addActionListener(removeNodeFromModel);

		JMenuItem addNumbers = new JMenuItem("Add odd/even numbered children");
		addNumbers.setToolTipText("Example of a bulk update");
		addNumbers.addActionListener(addEvenOddNumbers);

		JMenuItem longTextNoUpdate = new JMenuItem("Set long text - without update - [Will NOT update correctly]");
		longTextNoUpdate.setToolTipText("Will NOT update correctly");

		JMenuItem longTextWithUpdate = new JMenuItem("Set long text - with update");
		longTextWithUpdate.setToolTipText("Will update correctly");
		longTextWithUpdate.addActionListener(modifyUserObjectWithUpdate);
		menu.add(removeNode);
		menu.add(removeModel);
		menu.addSeparator();
		menu.add(addNumbers);
		menu.addSeparator();
		menu.add(longTextNoUpdate);
		menu.add(longTextWithUpdate);
	}

	private void expandForPrefix(JTree tree, Optional<String> prefixTable) {

		if (prefixTable.isPresent()) {
			TreePath p = tree.getNextMatch(prefixTable.get(), 0, Position.Bias.Forward);
			if (p != null)
				tree.setSelectionPath(p);
		}

	}

	private MouseAdapter treeMenuClicked = new MouseAdapter() {

		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				int row = tree.getClosestRowForLocation(e.getX(), e.getY());
				tree.setSelectionPath(tree.getPathForRow(row));
				menu.show(tree, e.getX(), e.getY());
			}
		}
	};

	private ActionListener removeNodeFromModel = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			TreePath currentSelection = tree.getSelectionPath();

			if (currentSelection != null) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) currentSelection.getLastPathComponent();
				DefaultTreeModel model = ((DefaultTreeModel) tree.getModel());
				model.removeNodeFromParent(node);
			}
		}
	};

	/**
	 * 
	 * Example of adding children in bulk, then updating the tree.
	 * 
	 */

	private ActionListener addEvenOddNumbers = new ActionListener() {

		public void actionPerformed(ActionEvent e) {

			TreePath currentSelection = tree.getSelectionPath();
			if (currentSelection != null) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) currentSelection.getLastPathComponent();
				DefaultTreeModel model = ((DefaultTreeModel) tree.getModel());
				DefaultMutableTreeNode odd = new DefaultMutableTreeNode("odd");
				node.add(odd);
				DefaultMutableTreeNode even = new DefaultMutableTreeNode("even");
				node.add(even);

				for (int i = 0; i < 50; i++)
					if (i % 2 == 0)
						even.add(new DefaultMutableTreeNode(i));
					else
						odd.add(new DefaultMutableTreeNode(i));

				// The above changes may not seem to take effect until
				// nodeStructureChanged is called
				model.nodeStructureChanged(node);
			}
		}
	};

	/**
	 * 
	 * Example of setting the user object and updating the model. This WILL
	 * update the tree correctly.
	 * 
	 */

	private ActionListener modifyUserObjectWithUpdate = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			TreePath currentSelection = tree.getSelectionPath();
			if (currentSelection != null) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) currentSelection.getLastPathComponent();
				node.setUserObject("THIS IS A VERY LOOOOOOOOOOOOOOOOOOOOONG STRING");
				DefaultTreeModel model = ((DefaultTreeModel) tree.getModel());
				model.nodeChanged(node);
			}
		}
	};

	private static DefaultMutableTreeNode getDemoTree() {
		ModelElementCompletion rootC = new ModelElementCompletion(SqlCompletionType.table, "R", null);
		ModelElementCompletion C1C = new ModelElementCompletion(SqlCompletionType.column, "C1", rootC);
		ModelElementCompletion C2C = new ModelElementCompletion(SqlCompletionType.column, "C2", rootC);
		ModelElementCompletion C3C = new ModelElementCompletion(SqlCompletionType.column, "C3", rootC);
		ModelElementCompletion C11C = new ModelElementCompletion(SqlCompletionType.table, "C11", C1C);
		ModelElementCompletion C12C = new ModelElementCompletion(SqlCompletionType.table, "C12", C1C);
		
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootC);
		DefaultMutableTreeNode child1Node = new DefaultMutableTreeNode(C1C);
		DefaultMutableTreeNode child2Node = new DefaultMutableTreeNode(C2C);
		DefaultMutableTreeNode child3Node = new DefaultMutableTreeNode(C3C);
		child1Node.add(new DefaultMutableTreeNode(C11C));
		child1Node.add(new DefaultMutableTreeNode(C12C));
		rootNode.add(child1Node);
		rootNode.add(child2Node);
		rootNode.add(child3Node);

		return rootNode;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		tree.expandRow(0);
	}

	private int longestPathCount() {
		if (root != null)
			return root.getDepth() + 1;
		else
			return 0;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		Object item = null;

		if (!tree.isSelectionEmpty()) {
			TreePath path = tree.getSelectionPath();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
			item = selectedNode.getUserObject();

			if (longestPathCount() == 2 && path.getPathCount() == 2)
				item = selectedNode.getUserObject();

			if (longestPathCount() == 3)
				if (path.getPathCount() == 2)
					item = selectedNode.getUserObject();
				else if (path.getPathCount() == 3) {
					item = selectedNode.getUserObject();
				}
		}

		if (item != null) {
			Check.isTrue(item instanceof AbstractCompletion);
			onChosen.onItemChosen((AbstractCompletion) item);
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {
		setVisible(false);
	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

	public static void show(DefaultMutableTreeNode root, Optional<String> prefixTable, ItemChosenHandler onChosen) {
		TreeNamePicker picker = new TreeNamePicker(root, prefixTable, onChosen);
		JFrame demoFrame = new JFrame("Eee");
		demoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		demoFrame.setContentPane(picker);
		demoFrame.addWindowListener(picker);

		demoFrame.setSize(300, 600);
		demoFrame.setLocationRelativeTo(null);

		demoFrame.setVisible(true);
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Optional<String> prefix = Optional.absent();
				show(getDemoTree(), prefix, new ItemChosenHandler() {

					@Override
					public void onItemChosen(AbstractCompletion item) {
						String itemText = item == null ? "<no item>" : item.displayName;
						String parentText = item != null && item.parent != null ? item.parent.displayName
								: "<no parent>";
						System.out.println(String.format("%s - %s", parentText, itemText));

					}

				});
			}
		});
	}

	@Override
	public void onKeyEvent(KeyEvent e, Object sender) {
		// e.getID() == KeyEvent.KEY_PRESSED, KeyEvent.KEY_RELEASED,
		// KeyEvent.KEY_TYPED

		int keycode = e.getKeyCode();
		switch (keycode) {

		case KeyEvent.VK_ENTER:
			System.out.println(sender.getClass().getName());
			if (sender instanceof KeyControlledFrame)
				((KeyControlledFrame) sender).dispose();
			break;

		default:
			break;
		}
	}

}
