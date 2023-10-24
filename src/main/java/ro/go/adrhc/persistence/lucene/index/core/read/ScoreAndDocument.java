package ro.go.adrhc.persistence.lucene.index.core.read;

import org.apache.lucene.document.Document;

public record ScoreAndDocument(float score, Document document) {
	public String getField(Enum<?> field) {
		return document.get(field.name());
	}
}
