package ro.go.adrhc.persistence.lucene.core.bare.read;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.search.ScoreDoc;
import org.springframework.lang.NonNull;

import java.io.IOException;

public record ScoreAndDocument(@NonNull ScoreDoc scoreDoc, @NonNull Document document) {
	public static ScoreAndDocument of(StoredFields storedFields, ScoreDoc scoreDoc) throws IOException {
		return new ScoreAndDocument(scoreDoc,
			LuceneDocumentFactory.of(storedFields, null, scoreDoc.doc));
	}
}
