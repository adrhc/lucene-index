package ro.go.adrhc.persistence.lucene.core.write;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.Query;
import ro.go.adrhc.util.stream.StreamCounter;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static ro.go.adrhc.persistence.lucene.core.write.IndexWriterFactory.fsWriter;
import static ro.go.adrhc.persistence.lucene.core.write.IndexWriterFactory.ramWriter;
import static ro.go.adrhc.util.collection.IterableUtils.iterable;

@RequiredArgsConstructor
@Slf4j
public class DocsIndexWriter implements Closeable {
	private final IndexWriter indexWriter;

	public static DocsIndexWriter
	ofRamWriter(Analyzer analyzer) throws IOException {
		return new DocsIndexWriter(ramWriter(analyzer));
	}

	public static DocsIndexWriter ofFsWriter(
			Analyzer analyzer, Path indexPath) throws IOException {
		return new DocsIndexWriter(fsWriter(analyzer, indexPath));
	}

	public void addOne(Iterable<? extends IndexableField> document) throws IOException {
		indexWriter.addDocument(document);
	}

	public void addMany(Iterable<? extends Iterable<? extends IndexableField>> documents)
			throws IOException {
		indexWriter.addDocuments(documents);
	}

	public void addMany(Stream<? extends Iterable<? extends IndexableField>> documents)
			throws IOException {
		addMany(iterable(documents));
	}

	public void upsert(Query idQuery, Iterable<? extends IndexableField> doc) throws IOException {
		upsertMany(idQuery, List.of(doc));
	}

	public void upsertMany(Query idsQuery,
			Iterable<? extends Iterable<? extends IndexableField>> docs) throws IOException {
		indexWriter.updateDocuments(idsQuery, docs);
	}

	public void deleteByQuery(Query query) throws IOException {
		indexWriter.deleteDocuments(query);
	}

	public void deleteByQueries(Collection<? extends Query> queries) throws IOException {
		indexWriter.deleteDocuments(queries.toArray(Query[]::new));
	}

	public void deleteAll() throws IOException {
		indexWriter.deleteAll();
	}

	public void reset(Stream<Document> stateAfterReset) throws IOException {
		log.debug("\nremoving all documents ...");
		deleteAll();
		log.debug("\nadding documents ...");
		StreamCounter counter = new StreamCounter();
		addMany(counter.countedStream(stateAfterReset));
		log.debug("\nadded {} documents", counter.getCount());
	}

	public void commit() throws IOException {
		indexWriter.commit();
	}

	@Override
	public void close() throws IOException {
//		indexWriter.flush();
	}
}
