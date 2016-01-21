package demo;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import org.fife.ui.autocomplete.*;
import org.fife.ui.autocomplete.ParameterizedCompletion.Parameter;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class AutoCompleteDemo extends JFrame {
	private static final long serialVersionUID = 1L;

	public AutoCompleteDemo() {

		JPanel contentPane = new JPanel(new BorderLayout());
		RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setCodeFoldingEnabled(true);
		contentPane.add(new RTextScrollPane(textArea));

		// A CompletionProvider is what knows of all possible completions, and
		// analyzes the contents of the text area at the caret position to
		// determine what completion choices should be presented. Most instances
		// of CompletionProvider (such as DefaultCompletionProvider) are
		// designed
		// so that they can be shared among multiple text components.
		CompletionProvider provider = createCompletionProvider();

		// An AutoCompletion acts as a "middle-man" between a text component
		// and a CompletionProvider. It manages any options associated with
		// the auto-completion (the popup trigger key, whether to display a
		// documentation window along with completion choices, etc.). Unlike
		// CompletionProviders, instances of AutoCompletion cannot be shared
		// among multiple text components.
		AutoCompletion ac = new AutoCompletion(provider);
		ac.setParameterAssistanceEnabled(true);
		ac.setAutoCompleteEnabled(true);
		ac.install(textArea);
		
		setContentPane(contentPane);
		setTitle("AutoComplete Demo");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		
	}

	/**
	 * Create a simple provider that adds some Java-related completions.
	 */
	private CompletionProvider createCompletionProvider() {

		// A DefaultCompletionProvider is the simplest concrete implementation
		// of CompletionProvider. This provider has no understanding of
		// language semantics. It simply checks the text entered up to the
		// caret position for a match against known completions. This is all
		// that is needed in the majority of cases.

		final DefaultCompletionProvider provider = new DefaultCompletionProvider();
		
		ParameterChoicesProvider pcp = new ParameterChoicesProvider(){

			@Override
			public List<Completion> getParameterChoices(JTextComponent tc, Parameter param) {
				ArrayList<Completion> p = new ArrayList<Completion>();
				p.add(new BasicCompletion(provider, "p1"));
				p.add(new BasicCompletion(provider, "p 2"));
				p.add(new BasicCompletion(provider, "p3"));
				return p;
			}};
			
		provider.setParameterChoicesProvider(pcp);
		
		ArrayList<Completion> subs = new ArrayList<Completion>();
		
		
		subs.add(new TemplateCompletion(provider, "x", "forlp", "for (int ${i} = 0; ${i} &lt; ${array}.length; ${i}++)"));	
		subs.add(new BasicCompletion(provider, "b"));
		subs.add(new BasicCompletion(provider, "s2"));
		subs.add(new BasicCompletion(provider, "s3"));
		provider.addCompletion(new BasicCompletion(provider, "s", "",subs));
		
		// Add completions for all Java keywords. A BasicCompletion is just
		// a straightforward word completion.
		TemplateCompletion tc = new TemplateCompletion(provider, "x1", "x", "for (int ${i} = 0; ${i} &lt; ${array}.length; ${i}++)");
		provider.addCompletion(tc);
		
		 tc = new TemplateCompletion(provider, "x2", "x", "for (int ${i} = 0; ${i} &lt; ${array}.length; ${i}++)");
		provider.addCompletion(tc);
		 tc = new TemplateCompletion(provider, "x3", "x", "for (int ${i} = 0; ${i} &lt; ${array}.length; ${i}++)");
		provider.addCompletion(tc);
		
		
		provider.addCompletion(new BasicCompletion(provider, "assert"));
		provider.addCompletion(new BasicCompletion(provider, "break"));
		provider.addCompletion(new BasicCompletion(provider, "case"));
		// ... etc ...
		provider.addCompletion(new BasicCompletion(provider, "transient"));
		provider.addCompletion(new BasicCompletion(provider, "try"));
		provider.addCompletion(new BasicCompletion(provider, "void"));
		provider.addCompletion(new BasicCompletion(provider, "volatile"));
		provider.addCompletion(new BasicCompletion(provider, "while"));

		// Add a couple of "shorthand" completions. These completions don't
		// require the input text to be the same thing as the replacement text.
		provider.addCompletion(
				new ShorthandCompletion(provider, "sysout", "System.out.println(", "System.out.println("));
		provider.addCompletion(
				new ShorthandCompletion(provider, "syserr", "System.err.println(", "System.err.println("));

		return provider;

	}

	public static void main(String[] args) {
		// Instantiate GUI on the EDT.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					String laf = UIManager.getSystemLookAndFeelClassName();
					UIManager.setLookAndFeel(laf);
				} catch (Exception e) {
					/* Never happens */ }
				new AutoCompleteDemo().setVisible(true);
			}
		});
	}

}