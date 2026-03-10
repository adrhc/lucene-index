package ro.go.adrhc.persistence.lucene.core.typed.read;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.springframework.lang.Nullable;
import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexReader;
import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexReaderFactory;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.util.Assert;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.Integer.MAX_VALUE;
import static ro.go.adrhc.persistence.lucene.core.bare.field.FieldType.STORED;

@RequiredArgsConstructor
public class TypedIndexReader<I, T> implements Closeable {
	private final LuceneFieldSpec<T> idField;
	private final DocumentToTypedConverter<T> toTypedConverter;
	private final ScoreAndDocumentToScoreAndValueConverter<T> toScoreAndValueConverter;
	private final DocIndexReader docIndexReader;

	public static <I, T> TypedIndexReader<I, T>
	create(TypedIndexReaderParams<T> params) throws IOException {
		DocumentToTypedConverter<T> docToTypedConverter =
			DocumentToTypedConverter.create(params.rawFieldValueSerdes());
		ScoreAndDocumentToScoreAndValueConverter<T> toScoreAndTypedConverter =
			new ScoreAndDocumentToScoreAndValueConverter<>(docToTypedConverter);
		DocIndexReader docIndexReader = DocIndexReaderFactory.of(params);
		return new TypedIndexReader<>(params.idField(),
			docToTypedConverter, toScoreAndTypedConverter, docIndexReader);
	}

	public Stream<T> getAll() throws IOException {
		return docIndexReader.getDocuments().map(
			toTypedConverter::convert).flatMap(Optional::stream);
	}

	public Stream<I> getAllIds() throws IOException {
		return getFieldValues(idField);
	}

	public Stream<I> findIds(Query query) throws IOException {
		return findIds(query, null);
	}

	public Stream<I> findIds(Query query, @Nullable Sort sort) throws IOException {
		return docIndexReader
			.findFieldValuesSorted(idField.name(), query, MAX_VALUE, sort)
			.map(idField::toPropertyValue);
	}

	public Stream<I> findIds(Query query, int numHits) throws IOException {
		return docIndexReader
			.findFieldValues(idField.name(), query, numHits)
			.map(idField::toPropertyValue);
	}

	public Stream<ScoreAndValue<T>> findMany(Query query) throws IOException {
		return toScoreAndValueConverter.convertStream(docIndexReader.findMany(query, MAX_VALUE));
	}

	public Stream<ScoreAndValue<T>> findMany(Query query, int numHits) throws IOException {
		return toScoreAndValueConverter.convertStream(
			docIndexReader.findMany(query, numHits));
	}

	public Stream<ScoreAndValue<T>> findManySorted(Query query, Sort sort) throws IOException {
		return toScoreAndValueConverter.convertStream(
			docIndexReader.findManySorted(query, MAX_VALUE, sort));
	}

	public Stream<ScoreAndValue<T>> findManySorted(
		Query query, int numHits, Sort sort) throws IOException {
		return toScoreAndValueConverter.convertStream(
			docIndexReader.findManySorted(query, numHits, sort));
	}

	public Stream<ScoreAndValue<T>> findManyAfter(ScoreDoc after,
		Query query, Sort sort) throws IOException {
		return toScoreAndValueConverter.convertStream(
			docIndexReader.findManyAfter(after, query, MAX_VALUE, sort));
	}

	public Stream<ScoreAndValue<T>> findManyAfter(ScoreDoc after,
		Query query, int numHits, Sort sort) throws IOException {
		return toScoreAndValueConverter.convertStream(
			docIndexReader.findManyAfter(after, query, numHits, sort));
	}

	public boolean hasAfter(ScoreDoc scoreDoc, Sort sort) throws IOException {
		return docIndexReader.hasAfter(scoreDoc, sort);
	}

	/**
	 * The caller must use the proper type!
	 *
	 * @return the field values of all documents
	 */
	public <P> Stream<P> getFieldValues(LuceneFieldSpec<T> field) throws IOException {
		Assert.isTrue(field.isIdField() || field.fieldType() == STORED,
			field.name() + " must have STORED type!");
		return docIndexReader.getFieldValues(field.name())
			.map(field::indexedValueToPropValue);
	}

	@Override
	public void close() throws IOException {
		docIndexReader.close();
	}
}
