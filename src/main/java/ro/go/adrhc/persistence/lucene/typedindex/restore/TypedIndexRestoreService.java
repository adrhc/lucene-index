package ro.go.adrhc.persistence.lucene.typedindex.restore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.core.write.DocsIndexWriterTemplate;
import ro.go.adrhc.persistence.lucene.index.IndexRemoveService;
import ro.go.adrhc.persistence.lucene.index.update.DocumentsIndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.update.IndexUpdateService;
import ro.go.adrhc.persistence.lucene.typedcore.ExactQuery;
import ro.go.adrhc.persistence.lucene.typedcore.docserde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedcore.docserde.TypedToDocumentConverter;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.reader.TypedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexFactoriesParams;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Stream;

import static ro.go.adrhc.util.collection.StreamUtils.collectToHashSet;
import static ro.go.adrhc.util.conversion.OptionalResultConversionUtils.convertStream;
import static ro.go.adrhc.util.fn.SneakyBiFunctionUtils.curry;

@RequiredArgsConstructor
@Slf4j
public class TypedIndexRestoreService<ID, T extends Identifiable<ID>> implements IndexRestoreService<ID, T> {
	private final TypedField<T> idField;
	private final TypedToDocumentConverter<T> typedToDocumentConverter;
	private final TypedIndexReaderTemplate<T> typedIndexReaderTemplate;
	private final IndexUpdateService<Document> indexUpdateService;
	private final IndexRemoveService<ID> indexRemoveService;

	/**
	 * constructor parameters union
	 */
	public static <ID, T extends Identifiable<ID>> TypedIndexRestoreService<ID, T>
	create(TypedIndexFactoriesParams<ID, T, ?> factoriesParams) {
		ExactQuery exactQuery = ExactQuery.create(factoriesParams.getIdField());
		DocsIndexWriterTemplate writerTemplate =
				new DocsIndexWriterTemplate(factoriesParams.getIndexWriter());
		return new TypedIndexRestoreService<>(factoriesParams.getIdField(),
				TypedToDocumentConverter.create(factoriesParams),
				TypedIndexReaderTemplate.create(factoriesParams),
				new DocumentsIndexUpdateService(exactQuery, writerTemplate),
				new TypedIndexRemoveService<>(exactQuery, writerTemplate));
	}

	@Override
	public void restore(IndexDataSource<ID, T> dataSource) throws IOException {
		IndexChanges<ID> changes = getIndexChanges(dataSource);
		if (changes.hasChanges()) {
			applyIndexChanges(dataSource, changes);
		} else {
			log.debug("\nNo changes detected!");
		}
	}

	private IndexChanges<ID> getIndexChanges(IndexDataSource<ID, ?> dataSource) throws IOException {
		return typedIndexReaderTemplate.transformFieldValues(idField, curry(this::toIndexChanges, dataSource));
	}

	private IndexChanges<ID> toIndexChanges(
			IndexDataSource<ID, ?> dataSource,
			Stream<ID> indexedIds) throws IOException {
		Set<ID> ids = collectToHashSet(dataSource.loadAllIds());
		Set<ID> docsToRemove = collectToHashSet(indexedIds.filter(id -> !ids.remove(id)));
		return new IndexChanges<>(ids, docsToRemove);
	}

	private void applyIndexChanges(
			IndexDataSource<ID, T> dataSource,
			IndexChanges<ID> changes) throws IOException {
		log.debug("\nremoving {} missing data from the index", changes.indexIdsMissingDataSize());
		indexRemoveService.removeByIds(changes.obsoleteIndexedIds());
		log.debug("\nextracting {} metadata to index", changes.notIndexedSize());
		Stream<T> items = dataSource.loadByIds(changes.notIndexedIds());
		Stream<Document> documents = convertStream(typedToDocumentConverter::convert, items);
		log.debug("\nadding documents to the index");
		indexUpdateService.addAll(documents);
		log.debug("\nIndex updated!");
	}
}
