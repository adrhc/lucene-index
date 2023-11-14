package ro.go.adrhc.persistence.lucene.typedcore.read;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.read.DocumentsIndexReader;
import ro.go.adrhc.persistence.lucene.core.read.ScoreAndDocument;
import ro.go.adrhc.persistence.lucene.typedcore.serde.DocumentToTypedConverter;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TypedIndexReader<T> implements Closeable {
	private final DocumentToTypedConverter<T> docToTypedConverter;
	private final DocumentsIndexReader indexReader;

	public static <T> TypedIndexReader<T> create(TypedIndexReaderParams<T> params) throws IOException {
		DocumentToTypedConverter<T> docToTypedConverter = DocumentToTypedConverter.create(params.getType());
		DocumentsIndexReader indexReader = DocumentsIndexReader.create(params);
		return new TypedIndexReader<>(docToTypedConverter, indexReader);
	}

	/*public static <ID, T, E extends Enum<E> & TypedField<?>> TypedIndexReader<ID, T>
	create(Class<T> tClass, Class<E> tFieldEnumClass, int maxResultsPerSearchedSong,
			IndexReaderPool indexReaderPool) throws IOException {
		DocumentToTypedConverter<T> docToTypedConverter = DocumentToTypedConverter.create(tClass);
		TypedField<?> idField = getIdField(tFieldEnumClass);
		return new TypedIndexReader<>(docToTypedConverter, idField,
				DocumentsIndexReader.create(maxResultsPerSearchedSong, indexReaderPool));
	}*/

	public Stream<T> getAll() {
		return indexReader.getAll().map(docToTypedConverter::convert).flatMap(Optional::stream);
	}

	public Stream<ScoreAndTyped<T>> findMany(Query query) throws IOException {
		return indexReader.findMany(query).map(this::toScoreAndType).flatMap(Optional::stream);
	}

	public Optional<T> findFirst(Query query) throws IOException {
		return indexReader.findFirst(query).flatMap(docToTypedConverter::convert);
	}

	protected Optional<ScoreAndTyped<T>> toScoreAndType(ScoreAndDocument scoreAndDocument) {
		return docToTypedConverter.convert(scoreAndDocument.document())
				.map(t -> new ScoreAndTyped<>(scoreAndDocument.score(), t));
	}

	@Override
	public void close() throws IOException {
		indexReader.close();
	}
}
