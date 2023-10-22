package ro.go.adrhc.persistence.lucene.index.core.read;

import org.apache.lucene.document.Document;

public record ScoreAndDocument(float score, Document document) {
}
