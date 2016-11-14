package org.cg.eclipse.plugins.ftc;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.cg.common.core.AbstractLogger;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.google.common.base.Optional;

public class MessageConsoleLogger extends AbstractLogger {

	private final String consoleName = PluginConst.MESSAGECONSOLE_NAME;
	private MessageConsole console;
	private MessageConsoleStream outputStream;
	private static MessageConsoleLogger _default;
	
	private final static Format formatter = new SimpleDateFormat("HH:mm:ss"); 
	
	public static String getLogLine(String info)
	{
		String date = formatter.format(new Date());
		return date + " " + info;
	}
	
	private MessageConsole findConsole(String name) {
		IConsoleManager conMan = getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];

		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}
	
	private IConsoleManager getConsoleManager() {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		return conMan;
	}

	public static MessageConsoleLogger getDefault() {
		if (_default == null)
			_default = new MessageConsoleLogger();
		return _default;
	}

	private MessageConsoleLogger() {
		console = findConsole(consoleName);
		outputStream = console.newMessageStream();
	}

	private Optional<IWorkbenchPage> getPage() {
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		return Optional.fromNullable(page);
	}

	private void write(String s) {
		outputStream.println(getLogLine(s));
	}

	public boolean reveal() {
		Optional<IWorkbenchPage> optPage = getPage();
		if (optPage.isPresent()) {
			IWorkbenchPage page = optPage.get();
			String id = IConsoleConstants.ID_CONSOLE_VIEW;
			IConsoleView view;
			try {
				view = (IConsoleView) page.showView(id);
			} catch (PartInitException e) {
				return false;
			}
			view.display(console);
			return true;
		}
		return false;
	}

	@Override
	protected void hdlInfo(String info) {
		write(info);
	}

	@Override
	protected void hdlError(String error) {
		write(error);
	}

}
