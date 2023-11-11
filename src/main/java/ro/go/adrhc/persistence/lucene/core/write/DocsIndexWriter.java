package ro.go.adrhc.persistence.lucene.core.write;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.Query;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static ro.go.adrhc.util.collection.IterableUtils.iterable;

@RequiredArgsConstructor
@Slf4j
public class DocsIndexWriter implements Closeable {
	private final IndexWriter indexWriter;

	public void addOne(Iterable<? extends IndexableField> document) throws IOException {
//		log.debug("\nAdding to index:\n{}", doc);
		indexWriter.addDocument(document);
	}

	public void addMany(Iterable<? extends Iterable<? extends IndexableField>> documents) throws IOException {
		indexWriter.addDocuments(documents);
	}

	public void addMany(Stream<? extends Iterable<? extends IndexableField>> documents) throws IOException {
		addMany(iterable(documents));
	}

	/*public void update(Term idTerm, Document document) throws IOException {
		indexWriter.updateDocValues(idTerm, document.getFields().toArray(Field[]::new));
	}*/

	public void update(Query idQuery, Document document) throws IOException {
		indexWriter.updateDocuments(idQuery, List.of(document));
	}

	public void deleteOne(Query query) throws IOException {
		indexWriter.deleteDocuments(query);
	}

	public void deleteMany(Collection<? extends Query> queries) throws IOException {
		indexWriter.deleteDocuments(queries.toArray(Query[]::new));
	}

	public void deleteAll() throws IOException {
		indexWriter.deleteAll();
	}

	@Override
	public void close() throws IOException {
		indexWriter.flush();
	}
}
