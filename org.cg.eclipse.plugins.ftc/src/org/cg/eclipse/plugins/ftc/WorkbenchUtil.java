package org.cg.eclipse.plugins.ftc;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class WorkbenchUtil {

	public static IViewPart showView(String viewId) {
		try {
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(viewId);
		} catch (PartInitException e) {
			System.out.println(String.format("Error opening view %s: %s", viewId, e.getMessage()));
			return null;
		}
	}
	
	public static Shell getShell(){
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}


	public static void activatePart(IWorkbenchPart part) {
		
		Display.getDefault().asyncExec(new Runnable() { // due to the implementation of getActiveWorkbenchWindow, see comment there
			public void run() {
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().activate(part);
				} catch (NullPointerException e) {
					// all kind of stuff can be null in that call chain 
				}
				
			}
		});
	}
	
}
