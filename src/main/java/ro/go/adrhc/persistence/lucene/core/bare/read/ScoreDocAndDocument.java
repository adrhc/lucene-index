package ro.go.adrhc.persistence.lucene.core.bare.read;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import ro.go.adrhc.util.specialcase.BrokenObject;

public record ScoreDocAndDocument(ScoreDoc scoreDoc, Document document) implements BrokenObject {
	public String getFieldValue(Enum<?> field) {
		return document.get(field.name());
	}

	@Override
	public boolean isBroken() {
		return scoreDoc == null || document == null;
	}
}
