package ro.go.adrhc.persistence.lucene.core.bare.read;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;

public record ScoreDocAndDocument(ScoreDoc scoreDoc, Document document) {
	public String getFieldValue(Enum<?> field) {
		return document.get(field.name());
	}
}
