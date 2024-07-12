package ro.go.adrhc.persistence.lucene.core.read;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;

import java.io.IOException;
import java.util.stream.Stream;

@Slf4j
public class HitsLimitedDocsIndexReader extends DocsIndexReader {
	private final int numHits;

	public HitsLimitedDocsIndexReader(IndexReaderPool indexReaderPool,
			IndexReader indexReader, int numHits) {
		super(indexReaderPool, indexReader);
		this.numHits = numHits;
	}

	public static HitsLimitedDocsIndexReader
	createUnlimited(IndexReaderPool indexReaderPool) throws IOException {
		return new HitsLimitedDocsIndexReader(indexReaderPool,
				indexReaderPool.getReader(), Integer.MAX_VALUE);
	}

	public static HitsLimitedDocsIndexReader create(
			HitsLimitedDocsIndexReaderParams params) throws IOException {
		return new HitsLimitedDocsIndexReader(params.getIndexReaderPool(),
				params.getIndexReaderPool().getReader(), params.getNumHits());
	}

	public static HitsLimitedDocsIndexReader create(
			IndexReaderPool indexReaderPool, int numHits) throws IOException {
		return new HitsLimitedDocsIndexReader(
				indexReaderPool, indexReaderPool.getReader(), numHits);
	}

	public Stream<ScoreDocAndDocument> findMany(Query query) throws IOException {
		return findMany(query, numHits);
	}

	public Stream<ScoreDocAndDocument> findMany(Query query, Sort sort) throws IOException {
		return findMany(query, numHits, sort);
	}

	public Stream<ScoreDocAndDocument> findManyAfter(
			ScoreDoc after, Query query, Sort sort) throws IOException {
		return findManyAfter(after, query, numHits, sort);
	}

	public Stream<Object> findFieldValues(String fieldName, Query query) throws IOException {
		return findFieldValues(fieldName, query, numHits);
	}
}
