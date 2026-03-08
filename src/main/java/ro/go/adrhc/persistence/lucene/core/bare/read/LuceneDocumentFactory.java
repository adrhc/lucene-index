package ro.go.adrhc.persistence.lucene.core.bare.read;

import lombok.experimental.UtilityClass;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.StoredFields;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.Set;

@UtilityClass
public class LuceneDocumentFactory {
	/**
	 * indexReader.document might fail if the document
	 * is meanwhile purged (not only marked as removed)
	 */
	@NonNull
	public static Document of(StoredFields storedFields,
		Set<String> fieldNames, int docIndex) throws IOException {
		if (fieldNames == null || fieldNames.isEmpty()) {
			return storedFields.document(docIndex);
		} else {
			return storedFields.document(docIndex, fieldNames);
		}
	}
}
