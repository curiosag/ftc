package org.cg.eclipse.plugins.ftc;

import org.cg.eclipse.plugins.ftc.syntaxstyle.SqlCommentPartitionScanner;
import org.eclipse.core.filebuffers.IDocumentSetupParticipant;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;

/**
 *
 */
public class SqlDocumentSetupParticipant implements IDocumentSetupParticipant {

	public SqlDocumentSetupParticipant() {
	}

	@Override
	public void setup(IDocument document) {
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension3= (IDocumentExtension3) document;
			IDocumentPartitioner partitioner= new FastPartitioner(SqlCommentPartitionScanner.getDefault(), SqlCommentPartitionScanner.partition_types);
			extension3.setDocumentPartitioner(FtcPlugin.SQL_PARTITIONING, partitioner);
			partitioner.connect(document);
		}
	}
}
