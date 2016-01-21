package org.cg.ftc.ftcClientCodeCompletionExperiments;
import javax.swing.tree.DefaultMutableTreeNode;

import org.cg.ftc.shared.structures.AbstractCompletion;
import org.cg.ftc.shared.structures.Completions;

public class ToTreeData {
	
	public static DefaultMutableTreeNode fromCompletions(String caption, Completions values) {
		if (values.size() == 0)
			return null;

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(caption);
		for (AbstractCompletion c : values.getAll()) 
			append(root, c);
		
		return root;
	}

	private static void append(DefaultMutableTreeNode root, AbstractCompletion c) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(c);
		
		for ( AbstractCompletion child : c.children)
			append(node, child);
		
		root.add(node);
	}

}
