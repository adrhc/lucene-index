package ro.go.adrhc.persistence.lucene.core.bare.read;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.stream.Stream;

@Slf4j
public class HitsLimitedDocIndexReader extends DocIndexReader {
	private final int numHits;

	public HitsLimitedDocIndexReader(IndexReaderPool indexReaderPool,
		IndexReader indexReader, int numHits) {
		super(indexReaderPool, indexReader);
		this.numHits = numHits;
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

	public Stream<Object> findFieldValues(
		String fieldName, Query query, @Nullable Sort sort) throws IOException {
		return super.findFieldValues(fieldName, query, numHits, sort);
	}
}
