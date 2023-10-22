package ro.go.adrhc.persistence.lucene.index.domain.field;

import org.apache.lucene.document.FieldType;

import static org.apache.lucene.index.IndexOptions.DOCS_AND_FREQS;
import static org.apache.lucene.index.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS;

public class IndexOptions {
	public static final FieldType STORED_AND_ANALYZED;
	public static final FieldType STORED_AND_NOT_ANALYZED;

	static {
		STORED_AND_ANALYZED = new FieldType();
		STORED_AND_ANALYZED.setStored(true);
		STORED_AND_ANALYZED.setTokenized(true);
		STORED_AND_ANALYZED.setIndexOptions(DOCS_AND_FREQS_AND_POSITIONS);

		STORED_AND_NOT_ANALYZED = new FieldType();
		STORED_AND_NOT_ANALYZED.setStored(true);
		STORED_AND_NOT_ANALYZED.setTokenized(false);
		STORED_AND_NOT_ANALYZED.setIndexOptions(DOCS_AND_FREQS);
	}
}
