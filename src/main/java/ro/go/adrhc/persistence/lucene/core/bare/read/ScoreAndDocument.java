package ro.go.adrhc.persistence.lucene.core.bare.read;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import ro.go.adrhc.util.Breakable;

public record ScoreAndDocument(ScoreDoc scoreDoc, Document document)
	implements Breakable<ScoreAndDocument> {
	@Override
	public boolean isBroken() {
		return scoreDoc == null || document == null;
	}
}
