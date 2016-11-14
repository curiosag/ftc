package org.cg.eclipse.plugins.ftc;

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.progress.IProgressConstants;

public class FtcPerspective implements IPerspectiveFactory {

	private IPageLayout layoutFactory;

	@Override
	public void createInitialLayout(IPageLayout layout) {

		this.layoutFactory = layout;
		addViews();
		layout.addActionSet(JavaUI.ID_ELEMENT_CREATION_ACTION_SET);
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");
	}

	private void addViews() {

		IFolderLayout bottom = layoutFactory.createFolder("bottomRight", // NON-NLS-1
				IPageLayout.BOTTOM, 0.75f, layoutFactory.getEditorArea());
		bottom.addView(PluginConst.RESULT_VIEW_ID);
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottom.addView(IProgressConstants.PROGRESS_VIEW_ID);

		IFolderLayout topLeft = layoutFactory.createFolder("topLeft", // NON-NLS-1
				IPageLayout.LEFT, 0.25f, layoutFactory.getEditorArea());
		topLeft.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		topLeft.addView(IPageLayout.ID_PROJECT_EXPLORER);//ID_RES_NAV);

	}

}
