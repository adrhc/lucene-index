package ro.go.adrhc.persistence.lucene.index.core.write;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static ro.go.adrhc.util.collection.IterableUtils.iterable;

@RequiredArgsConstructor
@Slf4j
public class DocumentsIndexWriter implements Closeable {
	private final IndexWriter indexWriter;

	public void copyTo(Path destination) throws IOException {
		Directory sourceDir = indexWriter.getDirectory();
		try (FSDirectory fsDirectory = FSDirectory.open(destination)) {
			for (String fileName : sourceDir.listAll()) {
				fsDirectory.copyFrom(sourceDir, fileName, fileName, null);
			}
		}
	}

	public void addDocument(Iterable<? extends IndexableField> document) throws IOException {
//		log.debug("\nAdding to index:\n{}", doc);
		indexWriter.addDocument(document);
	}

	public void addDocuments(Iterable<? extends Iterable<? extends IndexableField>> documents) throws IOException {
		indexWriter.addDocuments(documents);
	}

	public void addDocuments(Stream<? extends Iterable<? extends IndexableField>> documents) throws IOException {
		addDocuments(iterable(documents));
	}

	/*public void update(Term idTerm, Document document) throws IOException {
		indexWriter.updateDocValues(idTerm, document.getFields().toArray(Field[]::new));
	}*/

	public void update(Query idQuery, Document document) throws IOException {
		indexWriter.updateDocuments(idQuery, List.of(document));
	}

	public void deleteDocument(Query query) throws IOException {
		indexWriter.deleteDocuments(query);
	}

	public void deleteDocuments(Collection<? extends Query> queries) throws IOException {
		indexWriter.deleteDocuments(queries.toArray(Query[]::new));
	}

	@Override
	public void close() throws IOException {
		indexWriter.flush();
	}

	public void deleteAll() throws IOException {
		indexWriter.deleteAll();
	}
}
