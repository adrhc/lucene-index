package ro.go.adrhc.persistence.lucene.index.restore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.IndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.core.docds.DocumentsDataSource;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class DSIndexRestoreService {
	private final String idField;
	private final DocumentsDataSource documentsDatasource;
	private final DocumentIndexReaderTemplate indexReaderTemplate;
	private final IndexUpdateService indexUpdateService;

	public static DSIndexRestoreService create(Enum<?> idField, DocumentsDataSource documentsDatasource,
			DocumentIndexReaderTemplate indexReaderTemplate, IndexUpdateService indexUpdateService) {
		return new DSIndexRestoreService(idField.name(), documentsDatasource, indexReaderTemplate, indexUpdateService);
	}

	public void restore() throws IOException {
		IndexChanges changes = getIndexChanges();
		if (changes.hasChanges()) {
			applyIndexChanges(changes);
		} else {
			log.debug("\nNo changes detected!");
		}
	}

	private IndexChanges getIndexChanges() throws IOException {
		return indexReaderTemplate.transformFieldValues(idField, this::toIndexChanges);
	}

	private IndexChanges toIndexChanges(Stream<String> indexedIds) throws IOException {
		List<String> ids = new ArrayList<>(documentsDatasource.loadAllIds());
		List<String> docsToRemove = indexedIds.filter(id -> !ids.remove(id)).toList();
		return new IndexChanges(ids, docsToRemove);
	}

	private void applyIndexChanges(IndexChanges changes) throws IOException {
		log.debug("\nremoving {} missing data from the index", changes.indexIdsMissingDataSize());
		indexUpdateService.removeByIds(changes.obsoleteIndexedIds());
		log.debug("\nextracting {} metadata to index", changes.notIndexedSize());
		Collection<Document> documents = documentsDatasource.loadByIds(changes.notIndexedIds());
		log.debug("\nadding {} metadata records to the index", documents.size());
		indexUpdateService.addDocuments(documents);
		log.debug("\nIndex updated!");
	}
}
