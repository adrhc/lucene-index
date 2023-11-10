package ro.go.adrhc.persistence.lucene.typedindex.restore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.IndexRemoveService;
import ro.go.adrhc.persistence.lucene.index.core.write.DocumentsIndexWriterTemplate;
import ro.go.adrhc.persistence.lucene.index.update.DocumentsIndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.update.IndexUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexSpec;
import ro.go.adrhc.persistence.lucene.typedindex.core.TypedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.domain.ExactQuery;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Stream;

import static ro.go.adrhc.util.collection.StreamUtils.collectToHashSet;
import static ro.go.adrhc.util.fn.SneakyBiFunctionUtils.curry;

@RequiredArgsConstructor
@Slf4j
public class DocumentsIndexRestoreService<ID, T> implements IndexRestoreService<ID, Document> {
	private final TypedField<T> idField;
	private final TypedIndexReaderTemplate<T> typedIndexReaderTemplate;
	private final IndexUpdateService<Document> indexUpdateService;
	private final IndexRemoveService<ID> indexRemoveService;

	/**
	 * constructor parameters union
	 */
	public static <ID, T extends Identifiable<ID>> DocumentsIndexRestoreService<ID, T>
	create(TypedIndexSpec<ID, T, ?> typedIndexSpec) {
		ExactQuery exactQuery = ExactQuery.create(typedIndexSpec.getIdField());
		DocumentsIndexWriterTemplate writerTemplate =
				new DocumentsIndexWriterTemplate(typedIndexSpec.getIndexWriter());
		return new DocumentsIndexRestoreService<>(typedIndexSpec.getIdField(),
				TypedIndexReaderTemplate.create(typedIndexSpec),
				new DocumentsIndexUpdateService(exactQuery, writerTemplate),
				new TypedIndexRemoveService<>(exactQuery, writerTemplate));
	}

	public void restore(IndexDataSource<ID, Document> dataSource) throws IOException {
		IndexChanges<ID> changes = getIndexChanges(dataSource);
		if (changes.hasChanges()) {
			applyIndexChanges(dataSource, changes);
		} else {
			log.debug("\nNo changes detected!");
		}
	}

	private IndexChanges<ID> getIndexChanges(IndexDataSource<ID, Document> dataSource) throws IOException {
		return typedIndexReaderTemplate.transformFieldValues(idField, curry(this::toIndexChanges, dataSource));
	}

	private IndexChanges<ID> toIndexChanges(
			IndexDataSource<ID, Document> dataSource,
			Stream<ID> indexedIds) throws IOException {
		Set<ID> ids = collectToHashSet(dataSource.loadAllIds());
		Set<ID> docsToRemove = collectToHashSet(indexedIds.filter(id -> !ids.remove(id)));
		return new IndexChanges<>(ids, docsToRemove);
	}

	private void applyIndexChanges(
			IndexDataSource<ID, Document> dataSource,
			IndexChanges<ID> changes) throws IOException {
		log.debug("\nremoving {} missing data from the index", changes.indexIdsMissingDataSize());
		indexRemoveService.removeByIds(changes.obsoleteIndexedIds());
		log.debug("\nextracting {} metadata to index", changes.notIndexedSize());
		Stream<Document> documents = dataSource.loadByIds(changes.notIndexedIds());
		log.debug("\nadding documents to the index");
		indexUpdateService.addAll(documents);
		log.debug("\nIndex updated!");
	}
}
