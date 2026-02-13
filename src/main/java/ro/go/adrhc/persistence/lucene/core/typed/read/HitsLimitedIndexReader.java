package ro.go.adrhc.persistence.lucene.core.typed.read;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.springframework.lang.Nullable;
import ro.go.adrhc.persistence.lucene.core.bare.read.HitsLimitedDocsIndexReader;
import ro.go.adrhc.persistence.lucene.core.bare.read.ScoreDocAndValue;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.serde.DocumentToTypedConverter;
import ro.go.adrhc.persistence.lucene.core.typed.serde.ScoreAndDocumentToScoreDocAndValueConverter;
import ro.go.adrhc.util.Assert;
import ro.go.adrhc.util.ObjectUtils;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static ro.go.adrhc.persistence.lucene.core.bare.field.FieldType.STORED;

@RequiredArgsConstructor
public class HitsLimitedIndexReader<ID, T> implements Closeable {
	private final LuceneFieldSpec<T> idField;
	private final DocumentToTypedConverter<T> docToTypedConverter;
	private final ScoreAndDocumentToScoreDocAndValueConverter<T> toScoreDocAndValueConverter;
	private final HitsLimitedDocsIndexReader hitsLimitedDocsIndexReader;

	public static <ID, T> HitsLimitedIndexReader<ID, T>
	create(HitsLimitedIndexReaderParams<T> params) throws IOException {
		DocumentToTypedConverter<T> docToTypedConverter =
			DocumentToTypedConverter.create(params.type());
		ScoreAndDocumentToScoreDocAndValueConverter<T> toScoreAndTypedConverter =
			new ScoreAndDocumentToScoreDocAndValueConverter<>(docToTypedConverter);
		HitsLimitedDocsIndexReader indexReader = HitsLimitedDocsIndexReader.create(params);
		return new HitsLimitedIndexReader<>(params.idField(),
			docToTypedConverter, toScoreAndTypedConverter, indexReader);
	}

	public Stream<T> getAll() throws IOException {
		return hitsLimitedDocsIndexReader.getDocumentStream().map(
			docToTypedConverter::convert).flatMap(Optional::stream);
	}

	public Stream<ID> getAllIds() throws IOException {
		return getFieldOfAll(idField);
	}

	public Stream<ID> findIds(Query query) throws IOException {
		return findIds(query, null);
	}

	public Stream<ID> findIds(Query query, @Nullable Sort sort) throws IOException {
		return hitsLimitedDocsIndexReader
			.findFieldValues(idField.name(), query, sort)
			.map(idField::toPropValue);
	}

	public Stream<ID> findIds(Query query, int numHits) throws IOException {
		return hitsLimitedDocsIndexReader
			.findFieldValues(idField.name(), query, numHits)
			.map(idField::toPropValue);
	}

	public Stream<ScoreDocAndValue<T>> findMany(Query query) throws IOException {
		return toScoreDocAndValueConverter.convertStream(
			hitsLimitedDocsIndexReader.findMany(query));
	}

	public Stream<ScoreDocAndValue<T>> findMany(Query query, int numHits) throws IOException {
		return toScoreDocAndValueConverter.convertStream(
			hitsLimitedDocsIndexReader.findMany(query, numHits));
	}

	public Stream<ScoreDocAndValue<T>> findMany(Query query, Sort sort) throws IOException {
		return toScoreDocAndValueConverter.convertStream(
			hitsLimitedDocsIndexReader.findMany(query, sort));
	}

	public Stream<ScoreDocAndValue<T>> findMany(
		Query query, int numHits, Sort sort) throws IOException {
		return toScoreDocAndValueConverter.convertStream(
			hitsLimitedDocsIndexReader.findMany(query, numHits, sort));
	}

	public Stream<ScoreDocAndValue<T>> findManyAfter(ScoreDoc after,
		Query query, Sort sort) throws IOException {
		return toScoreDocAndValueConverter.convertStream(
			hitsLimitedDocsIndexReader.findManyAfter(after, query, sort));
	}

	public Stream<ScoreDocAndValue<T>> findManyAfter(ScoreDoc after,
		Query query, int numHits, Sort sort) throws IOException {
		return toScoreDocAndValueConverter.convertStream(
			hitsLimitedDocsIndexReader.findManyAfter(after, query, numHits, sort));
	}

	/**
	 * The caller must use the proper type!
	 */
	public <F> Stream<F> getFieldOfAll(LuceneFieldSpec<T> field) throws IOException {
		Assert.isTrue(field.isIdField() || field.fieldType() == STORED,
			field.name() + " must have STORED type!");
		return hitsLimitedDocsIndexReader.getFields(field.name())
			.map(field::indexableValueToPropValue)
			.map(ObjectUtils::cast);
	}

	@Override
	public void close() throws IOException {
		hitsLimitedDocsIndexReader.close();
	}
}
