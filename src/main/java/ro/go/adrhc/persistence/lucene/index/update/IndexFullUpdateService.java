package ro.go.adrhc.persistence.lucene.index.update;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.LuceneIndex;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.index.spi.DocumentsDatasource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class IndexFullUpdateService {
	private final String idField;
	private final DocumentsDatasource documentsDatasource;
	private final DocumentIndexReaderTemplate indexReaderTemplate;
	private final LuceneIndex luceneIndex;

	public static IndexFullUpdateService create(Enum<?> idField, DocumentsDatasource documentsDatasource,
			DocumentIndexReaderTemplate indexReaderTemplate, LuceneIndex luceneIndex) {
		return new IndexFullUpdateService(idField.name(), documentsDatasource, indexReaderTemplate, luceneIndex);
	}

	public void update() throws IOException {
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
		luceneIndex.removeByIds(changes.obsoleteIndexedIds());
		log.debug("\nextracting {} metadata to index", changes.notIndexedSize());
		Collection<Document> documents = documentsDatasource.loadByIds(changes.notIndexedIds());
		log.debug("\nadding {} metadata records to the index", documents.size());
		luceneIndex.addDocuments(documents);
		log.debug("\nIndex updated!");
	}
}
