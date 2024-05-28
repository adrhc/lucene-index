package ro.go.adrhc.persistence.lucene.core.read;

import org.apache.lucene.index.StoredFieldVisitor;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import ro.go.adrhc.util.stream.StreamAware;

import java.io.IOException;
import java.util.stream.Stream;

public record TopDocsStoredFields(TopDocs topDocs, StoredFields storedFields)
		implements StreamAware<ScoreDoc> {
	@Override
	public Stream<ScoreDoc> rawStream() {
		return Stream.of(topDocs.scoreDocs);
	}

	public void document(int doc, StoredFieldVisitor fieldVisitor) throws IOException {
		storedFields.document(doc, fieldVisitor);
	}
}
