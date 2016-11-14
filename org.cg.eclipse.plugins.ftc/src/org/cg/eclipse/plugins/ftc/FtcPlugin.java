package org.cg.eclipse.plugins.ftc;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class FtcPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "ftcEditor"; //$NON-NLS-1$
	public final static String SQL_PARTITIONING = "sql_partitioning";

	// The shared instance
	private static FtcPlugin plugin;

	/**
	 * The constructor
	 */
	public FtcPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
	 * BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static FtcPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 *
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * resets syntax coloring and notifies all ftc editors
	 */
	public void notifyColoringChange() {
		org.eclipse.ui.IWorkbench workbench = org.eclipse.ui.PlatformUI.getWorkbench();
		// org.eclipse.ui.IEditorPart editor =
		// workbench.getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		for (IEditorReference p : workbench.getActiveWorkbenchWindow().getActivePage().getEditorReferences()) {
			IEditorPart editor = p.getEditor(false);
			if (editor != null && editor instanceof FtcEditor) {
				FtcEditor ftcEditor = (FtcEditor) editor;
				ftcEditor.invalidateTextRepresentation();
			}

		}

	}

}
