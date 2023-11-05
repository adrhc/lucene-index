package ro.go.adrhc.persistence.lucene.typedindex.restore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.IndexRemoveService;
import ro.go.adrhc.persistence.lucene.index.core.write.DocumentIndexWriterTemplate;
import ro.go.adrhc.persistence.lucene.index.update.DocumentsIndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.update.IndexUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.core.TypedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedindex.domain.ExactQuery;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;

import static ro.go.adrhc.persistence.lucene.index.core.write.DocumentIndexWriterTemplate.fsWriterTemplate;
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
	public static <ID, T> DocumentsIndexRestoreService<ID, T>
	create(Analyzer analyzer, Class<T> tClass, TypedField<T> idField, Path indexPath) {
		ExactQuery exactQuery = ExactQuery.create(idField);
		DocumentIndexWriterTemplate writerTemplate = fsWriterTemplate(analyzer, indexPath);
		return new DocumentsIndexRestoreService<>(idField,
				TypedIndexReaderTemplate.create(tClass, indexPath),
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
