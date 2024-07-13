package ro.go.adrhc.persistence.lucene.typedcore.read;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import ro.go.adrhc.persistence.lucene.core.read.HitsLimitedDocsIndexReader;
import ro.go.adrhc.persistence.lucene.core.read.ScoreDocAndDocument;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.serde.DocumentToTypedConverter;
import ro.go.adrhc.persistence.lucene.typedcore.serde.ScoreAndDocumentToScoreAndTypedConverter;
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
	private final ScoreAndDocumentToScoreAndTypedConverter<T> toScoreAndTypedConverter;
	private final HitsLimitedDocsIndexReader hitsLimitedDocsIndexReader;

	public static <ID, T> TypedIndexReader<ID, T>
	create(TypedIndexReaderParams<T> params) throws IOException {
		DocumentToTypedConverter<T> docToTypedConverter =
				DocumentToTypedConverter.create(params.getType());
		ScoreAndDocumentToScoreAndTypedConverter<T> toScoreAndTypedConverter =
				new ScoreAndDocumentToScoreAndTypedConverter<>(docToTypedConverter);
		HitsLimitedDocsIndexReader indexReader = HitsLimitedDocsIndexReader.create(params);
		return new TypedIndexReader<>(params.getIdField(),
				docToTypedConverter, toScoreAndTypedConverter, indexReader);
	}

	public Stream<T> getAll() {
		return hitsLimitedDocsIndexReader.getDocumentStream().map(
				docToTypedConverter::convert).flatMap(Optional::stream);
	}

	public Stream<ID> getAllIds() {
		return getFieldOfAll(idField);
	}

	public Stream<ID> findIds(Query query) throws IOException {
		return hitsLimitedDocsIndexReader.findFieldValues(idField.name(), query)
				.map(value -> (ID) idField.toPropValue(value));
	}

	public Stream<ID> findIds(Query query, int numHits) throws IOException {
		return hitsLimitedDocsIndexReader.findFieldValues(idField.name(), query, numHits)
				.map(value -> (ID) idField.toPropValue(value));
	}

	public Stream<ScoreDocAndValue<T>> findMany(Query query) throws IOException {
		return convert(hitsLimitedDocsIndexReader.findMany(query));
	}

	public Stream<ScoreDocAndValue<T>> findMany(Query query, int numHits) throws IOException {
		return convert(hitsLimitedDocsIndexReader.findMany(query, numHits));
	}

	public Stream<ScoreDocAndValue<T>> findMany(Query query, Sort sort) throws IOException {
		return convert(hitsLimitedDocsIndexReader.findMany(query, sort));
	}

	public Stream<ScoreDocAndValue<T>> findMany(
			Query query, int numHits, Sort sort) throws IOException {
		return convert(hitsLimitedDocsIndexReader.findMany(query, numHits, sort));
	}

	public Stream<ScoreDocAndValue<T>> findManyAfter(ScoreDoc after,
			Query query, Sort sort) throws IOException {
		return convert(hitsLimitedDocsIndexReader.findManyAfter(after, query, sort));
	}

	public Stream<ScoreDocAndValue<T>> findManyAfter(ScoreDoc after,
			Query query, int numHits, Sort sort) throws IOException {
		return convert(hitsLimitedDocsIndexReader.findManyAfter(after, query, numHits, sort));
	}

	/**
	 * The caller must use the proper type!
	 */
	public <F> Stream<F> getFieldOfAll(TypedField<T> field) {
		Assert.isTrue(field.isIdField() || field.fieldType() == STORED,
				field.name() + " must have STORED type!");
		return hitsLimitedDocsIndexReader.getFieldStream(field.name())
				.map(field::indexableFieldToPropValue)
				.map(ObjectUtils::cast);
	}

	@Override
	public void close() throws IOException {
		hitsLimitedDocsIndexReader.close();
	}

	private Stream<ScoreDocAndValue<T>> convert(Stream<ScoreDocAndDocument> stream) {
		return stream.map(toScoreAndTypedConverter::convert).flatMap(Optional::stream);
	}
}
