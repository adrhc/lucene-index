package ro.go.adrhc.persistence.lucene.index.restore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.IndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.core.docds.datasource.IndexDataSource;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Stream;

import static ro.go.adrhc.util.collection.StreamUtils.collectToHashSet;
import static ro.go.adrhc.util.fn.SneakyBiFunctionUtils.curry;

@RequiredArgsConstructor
@Slf4j
public class DSIndexRestoreService implements IndexRestoreService<String, Document> {
	private final String idField;
	private final DocumentIndexReaderTemplate indexReaderTemplate;
	private final IndexUpdateService indexUpdateService;

	public static DSIndexRestoreService create(Enum<?> idField,
			DocumentIndexReaderTemplate indexReaderTemplate, IndexUpdateService indexUpdateService) {
		return new DSIndexRestoreService(idField.name(), indexReaderTemplate, indexUpdateService);
	}

	public void restore(IndexDataSource<String, Document> dataSource) throws IOException {
		IndexChanges changes = getIndexChanges(dataSource);
		if (changes.hasChanges()) {
			applyIndexChanges(dataSource, changes);
		} else {
			log.debug("\nNo changes detected!");
		}
	}

	private IndexChanges getIndexChanges(IndexDataSource<String, Document> dataSource) throws IOException {
		return indexReaderTemplate.transformFieldValues(idField, curry(this::toIndexChanges, dataSource));
	}

	private IndexChanges toIndexChanges(
			IndexDataSource<String, Document> dataSource,
			Stream<String> indexedIds) throws IOException {
		Set<String> ids = collectToHashSet(dataSource.loadAllIds());
		Set<String> docsToRemove = collectToHashSet(indexedIds.filter(id -> !ids.remove(id)));
		return new IndexChanges(ids, docsToRemove);
	}

	private void applyIndexChanges(
			IndexDataSource<String, Document> dataSource,
			IndexChanges changes) throws IOException {
		log.debug("\nremoving {} missing data from the index", changes.indexIdsMissingDataSize());
		indexUpdateService.removeByIds(changes.obsoleteIndexedIds());
		log.debug("\nextracting {} metadata to index", changes.notIndexedSize());
		Stream<Document> documents = dataSource.loadByIds(changes.notIndexedIds());
		log.debug("\nadding documents to the index");
		indexUpdateService.addDocuments(documents);
		log.debug("\nIndex updated!");
	}
}
