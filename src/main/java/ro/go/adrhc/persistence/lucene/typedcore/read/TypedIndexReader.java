package ro.go.adrhc.persistence.lucene.typedcore.read;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.read.DocumentsIndexReader;
import ro.go.adrhc.persistence.lucene.core.read.ScoreAndDocument;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.serde.DocumentToTypedConverter;
import ro.go.adrhc.util.Assert;
import ro.go.adrhc.util.ObjectUtils;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static ro.go.adrhc.persistence.lucene.core.field.FieldType.STORED;

@RequiredArgsConstructor
public class TypedIndexReader<ID, T> implements Closeable {
	private final TypedField<T> idField;
	private final DocumentToTypedConverter<T> docToTypedConverter;
	private final DocumentsIndexReader indexReader;

	public static <ID, T> TypedIndexReader<ID, T> create(TypedIndexReaderParams<T> params) throws IOException {
		DocumentToTypedConverter<T> docToTypedConverter = DocumentToTypedConverter.create(params.getType());
		DocumentsIndexReader indexReader = DocumentsIndexReader.create(params);
		return new TypedIndexReader<>(params.getIdField(), docToTypedConverter, indexReader);
	}

	public Stream<T> getAll() {
		return indexReader.getAll().map(docToTypedConverter::convert).flatMap(Optional::stream);
	}

	public Stream<ID> getAllIds() {
		return getFieldOfAll(idField);
	}

	/**
	 * The caller must use the proper type!
	 */
	public <F> Stream<F> getFieldOfAll(TypedField<T> field) {
		Assert.isTrue(field.isIdField() || field.fieldType() == STORED,
				field.name() + " must have STORED type!");
		return indexReader.getFieldOfAll(field.name())
				.map(field::indexableFieldToTypedValue)
				.map(ObjectUtils::cast);
	}

	public Stream<ScoreAndTyped<T>> findMany(Query query) throws IOException {
		return indexReader.findMany(query).map(this::toScoreAndType).flatMap(Optional::stream);
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
