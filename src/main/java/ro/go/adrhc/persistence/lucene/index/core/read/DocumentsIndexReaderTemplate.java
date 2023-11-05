package ro.go.adrhc.persistence.lucene.index.core.read;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.Query;
import ro.go.adrhc.util.Assert;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public record DocumentsIndexReaderTemplate(int numHits, Path indexPath) {
	/**
	 * numHits = Integer.MAX_VALUE
	 */
	public static DocumentsIndexReaderTemplate create(Path indexPath) {
		return new DocumentsIndexReaderTemplate(Integer.MAX_VALUE, indexPath);
	}

	public <R, E extends Exception> R transformFields(String fieldName,
			SneakyFunction<Stream<IndexableField>, R, E> fieldValuesTransformer) throws IOException, E {
		return useReader(indexReader -> fieldValuesTransformer.apply(indexReader.getAllField(fieldName)));
	}

	public <R, E extends Exception> R transformFieldValues(String fieldName,
			SneakyFunction<Stream<String>, R, E> fieldValuesTransformer) throws IOException, E {
		return useReader(indexReader -> fieldValuesTransformer.apply(indexReader.getAllFieldValues(fieldName)));
	}

	public <R, E extends Exception> R transformDocuments(Set<String> fieldNames,
			SneakyFunction<Stream<Document>, R, E> documentsTransformer) throws IOException, E {
		return useReader(indexReader -> documentsTransformer.apply(indexReader.getAll(fieldNames)));
	}

	public <R, E extends Exception> R transformDocuments(
			SneakyFunction<Stream<Document>, R, E> documentsTransformer) throws IOException, E {
		return useReader(indexReader -> documentsTransformer.apply(indexReader.getAll()));
	}

	public Optional<Document> findById(Query idQuery) throws IOException {
		return useReader(indexReader -> indexReader.findById(idQuery));
	}

	/**
	 * Make sure that songsIndexReaderFn does not return a Stream! at the moment
	 * the Stream will actually run the DocumentIndexReader shall already be closed.
	 */
	public <R, E extends Exception> R useReader(
			SneakyFunction<DocumentIndexReader, R, E> indexReaderFn)
			throws IOException, E {
		try (DocumentIndexReader indexReader = DocumentIndexReader.of(numHits, indexPath)) {
			R result = indexReaderFn.apply(indexReader);
			Assert.isTrue(!(result instanceof Stream<?>), "Result must not be a stream!");
			return result;
		}
	}
}