package org.cg.eclipse.plugins.ftc;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import org.eclipse.core.runtime.Assert;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;

public class SimpleCompletionProposal implements ICompletionProposal {

		/** The string to be displayed in the completion proposal popup. */
		private String fDisplayString;
		/** The replacement string. */
		private String fReplacementString;
		/** The replacement offset. */
		private int fReplacementOffset;
		/** The replacement length. */
		private int fReplacementLength;
		/** The cursor position after this proposal has been applied. */
		private int fCursorPosition;
		/** The context information of this proposal. */
		private IContextInformation fContextInformation;
		

		/**
		 * Creates a new completion proposal based on the provided information. The replacement string is
		 * considered being the display string too. All remaining fields are set to <code>null</code>.
		 *
		 * @param replacementString the actual string to be inserted into the document
		 * @param replacementOffset the offset of the text to be replaced
		 * @param replacementLength the length of the text to be replaced
		 * @param cursorPosition the position of the cursor following the insert relative to replacementOffset
		 */
		public SimpleCompletionProposal(String replacementString, int replacementOffset, int replacementLength, int cursorPosition) {
			this(replacementString, replacementOffset, replacementLength, cursorPosition, null, null);
		}

		/**
		 * Creates a new completion proposal. All fields are initialized based on the provided information.
		 *
		 * @param replacementString the actual string to be inserted into the document
		 * @param replacementOffset the offset of the text to be replaced
		 * @param replacementLength the length of the text to be replaced
		 * @param cursorPosition the position of the cursor following the insert relative to replacementOffset
		 * @param displayString the string to be displayed for the proposal
		 * @param contextInformation the context information associated with this proposal
		 */
		public SimpleCompletionProposal(String replacementString, int replacementOffset, int replacementLength, int cursorPosition, String displayString, IContextInformation contextInformation) {
			Assert.isNotNull(replacementString);
			Assert.isTrue(replacementOffset >= 0);
			Assert.isTrue(replacementLength >= 0);
			Assert.isTrue(cursorPosition >= 0);

			fReplacementString= replacementString;
			fReplacementOffset= replacementOffset;
			fReplacementLength= replacementLength;
			fCursorPosition= cursorPosition;
			fDisplayString= displayString;
			fContextInformation= contextInformation;
		}

		/*
		 * @see ICompletionProposal#apply(IDocument)
		 */
		public void apply(IDocument document) {
			try {
				document.replace(fReplacementOffset, fReplacementLength, fReplacementString);
				
			} catch (BadLocationException x) {
				x.printStackTrace();
			}
		}

		/*
		 * @see ICompletionProposal#getSelection(IDocument)
		 */
		public Point getSelection(IDocument document) {
			return new Point(fReplacementOffset + fCursorPosition, 0);
		}

		/*
		 * @see ICompletionProposal#getContextInformation()
		 */
		public IContextInformation getContextInformation() {
			return fContextInformation;
		}

		/*
		 * @see ICompletionProposal#getDisplayString()
		 */
		public String getDisplayString() {
			if (fDisplayString != null)
				return fDisplayString;
			return fReplacementString;
		}

		@Override
		public String getAdditionalProposalInfo() {
			return null;
		}

		@Override
		public Image getImage() {
			return null;
		}

	}
