package ro.go.adrhc.persistence.lucene.core.typed.read;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.springframework.lang.Nullable;
import ro.go.adrhc.persistence.lucene.core.bare.read.HitsLimitedDocIndexReader;
import ro.go.adrhc.persistence.lucene.core.bare.read.HitsLimitedDocIndexReaderFactory;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.util.Assert;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static ro.go.adrhc.persistence.lucene.core.bare.field.FieldType.STORED;

@RequiredArgsConstructor
public class HitsLimitedIndexReader<I, T> implements Closeable {
	private final LuceneFieldSpec<T> idField;
	private final DocumentToTypedConverter<T> toTypedConverter;
	private final ScoreAndDocumentToScoreAndValueConverter<T> toScoreAndValueConverter;
	private final HitsLimitedDocIndexReader hitsLimitedDocsIndexReader;

	public static <I, T> HitsLimitedIndexReader<I, T>
	create(HitsLimitedIndexReaderParams<T> params) throws IOException {
		DocumentToTypedConverter<T> docToTypedConverter =
			DocumentToTypedConverter.create(params.rawFieldValueSerdes());
		ScoreAndDocumentToScoreAndValueConverter<T> toScoreAndTypedConverter =
			new ScoreAndDocumentToScoreAndValueConverter<>(docToTypedConverter);
		HitsLimitedDocIndexReader docIndexReader = HitsLimitedDocIndexReaderFactory.create(params);
		return new HitsLimitedIndexReader<>(params.idField(),
			docToTypedConverter, toScoreAndTypedConverter, docIndexReader);
	}

	public Stream<T> getAll() throws IOException {
		return hitsLimitedDocsIndexReader.getDocuments().map(
			toTypedConverter::convert).flatMap(Optional::stream);
	}

	public Stream<I> getAllIds() throws IOException {
		return getFieldValues(idField);
	}

	public Stream<I> findIds(Query query) throws IOException {
		return findIds(query, null);
	}

	public Stream<I> findIds(Query query, @Nullable Sort sort) throws IOException {
		return hitsLimitedDocsIndexReader
			.findFieldValuesSorted(idField.name(), query, sort)
			.map(idField::toPropertyValue);
	}

	public Stream<I> findIds(Query query, int numHits) throws IOException {
		return hitsLimitedDocsIndexReader
			.findFieldValues(idField.name(), query, numHits)
			.map(idField::toPropertyValue);
	}

	public Stream<ScoreAndValue<T>> findMany(Query query) throws IOException {
		return toScoreAndValueConverter.convertStream(
			hitsLimitedDocsIndexReader.findMany(query));
	}

	public Stream<ScoreAndValue<T>> findMany(Query query, int numHits) throws IOException {
		return toScoreAndValueConverter.convertStream(
			hitsLimitedDocsIndexReader.findMany(query, numHits));
	}

	public Stream<ScoreAndValue<T>> findManySorted(Query query, Sort sort) throws IOException {
		return toScoreAndValueConverter.convertStream(
			hitsLimitedDocsIndexReader.findManySorted(query, sort));
	}

	public Stream<ScoreAndValue<T>> findManySorted(
		Query query, int numHits, Sort sort) throws IOException {
		return toScoreAndValueConverter.convertStream(
			hitsLimitedDocsIndexReader.findManySorted(query, numHits, sort));
	}

	public Stream<ScoreAndValue<T>> findManyAfter(ScoreDoc after,
		Query query, Sort sort) throws IOException {
		return toScoreAndValueConverter.convertStream(
			hitsLimitedDocsIndexReader.findManyAfter(after, query, sort));
	}

	public Stream<ScoreAndValue<T>> findManyAfter(ScoreDoc after,
		Query query, int numHits, Sort sort) throws IOException {
		return toScoreAndValueConverter.convertStream(
			hitsLimitedDocsIndexReader.findManyAfter(after, query, numHits, sort));
	}

	public boolean hasAfter(ScoreDoc scoreDoc, Sort sort) throws IOException {
		return hitsLimitedDocsIndexReader.hasAfter(scoreDoc, sort);
	}

	/**
	 * The caller must use the proper type!
	 *
	 * @return the field values of all documents
	 */
	public <P> Stream<P> getFieldValues(LuceneFieldSpec<T> field) throws IOException {
		Assert.isTrue(field.isIdField() || field.fieldType() == STORED,
			field.name() + " must have STORED type!");
		return hitsLimitedDocsIndexReader.getFieldValues(field.name())
			.map(field::indexedValueToPropValue);
	}

	@Override
	public void close() throws IOException {
		hitsLimitedDocsIndexReader.close();
	}
}
