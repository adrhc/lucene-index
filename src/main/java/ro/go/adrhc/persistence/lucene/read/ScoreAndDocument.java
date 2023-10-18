package ro.go.adrhc.persistence.lucene.read;

import org.apache.lucene.document.Document;

public record ScoreAndDocument(float score, Document document) {
}
