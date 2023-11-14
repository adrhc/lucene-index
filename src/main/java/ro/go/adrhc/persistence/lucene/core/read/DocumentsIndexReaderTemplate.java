package ro.go.adrhc.persistence.lucene.core.read;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import com.rainerhahnekamp.sneakythrow.functional.SneakySupplier;
import lombok.RequiredArgsConstructor;
import ro.go.adrhc.util.Assert;

import java.io.IOException;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DocumentsIndexReaderTemplate {
	private final SneakySupplier<DocumentsIndexReader, IOException> indexReaderFactory;

	public static DocumentsIndexReaderTemplate create(DocumentsIndexReaderParams params) {
		return new DocumentsIndexReaderTemplate(() -> DocumentsIndexReader.create(params));
	}

	public static DocumentsIndexReaderTemplate create(int numHits, IndexReaderPool indexReaderPool) {
		return new DocumentsIndexReaderTemplate(() -> DocumentsIndexReader.create(numHits, indexReaderPool));
	}

	/*public <R, E extends Exception> R transformFields(String fieldName,
			SneakyFunction<Stream<IndexableField>, R, E> fieldValuesTransformer) throws IOException, E {
		return useReader(indexReader -> fieldValuesTransformer.apply(indexReader.getFieldOfAll(fieldName)));
	}

	public <R, E extends Exception> R transformFieldValues(String fieldName,
			SneakyFunction<Stream<String>, R, E> fieldValuesTransformer) throws IOException, E {
		return useReader(indexReader -> fieldValuesTransformer.apply(indexReader.getFieldValueOfAll(fieldName)));
	}

	public <R, E extends Exception> R transformDocuments(Set<String> fieldNames,
			SneakyFunction<Stream<Document>, R, E> documentsTransformer) throws IOException, E {
		return useReader(indexReader -> documentsTransformer.apply(indexReader.getFieldsOfAll(fieldNames)));
	}

	public <R, E extends Exception> R transformDocuments(
			SneakyFunction<Stream<Document>, R, E> documentsTransformer) throws IOException, E {
		return useReader(indexReader -> documentsTransformer.apply(indexReader.getAll()));
	}

	public Optional<Document> findFirst(Query idQuery) throws IOException {
		return useReader(indexReader -> indexReader.findFirst(idQuery));
	}*/

	/**
	 * Make sure that songsIndexReaderFn does not return a Stream! at the moment
	 * the Stream will actually run the DocumentsIndexReader shall already be closed.
	 */
	public <R, E extends Exception> R useReader(
			SneakyFunction<DocumentsIndexReader, R, E> indexReaderFn)
			throws IOException, E {
		try (DocumentsIndexReader indexReader = indexReaderFactory.get()) {
			R result = indexReaderFn.apply(indexReader);
			Assert.isTrue(!(result instanceof Stream<?>), "Result must not be a stream!");
			return result;
		}
	}
}
