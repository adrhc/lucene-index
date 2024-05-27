package ro.go.adrhc.persistence.lucene.typedindex.restore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReader;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexAdderTemplate;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemover;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Stream;

import static ro.go.adrhc.util.stream.StreamUtils.collectToHashSet;

@RequiredArgsConstructor
@Slf4j
public class TypedIndexRestoreService<ID, T> implements IndexRestoreService<ID, T> {
	private final TypedIndexReaderTemplate<ID, ?> indexReaderTemplate;
	private final TypedIndexRemover<ID> indexRemover;
	private final TypedIndexAdderTemplate<T> typedIndexAdderTemplate;

	/**
	 * constructor parameters union
	 */
	public static <ID, T> TypedIndexRestoreService<ID, T>
	create(TypedIndexRestoreServiceParams<T> params) {
		return new TypedIndexRestoreService<>(
				TypedIndexReaderTemplate.create(params),
				TypedIndexRemover.create(params),
				TypedIndexAdderTemplate.create(params));
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

	protected IndexChanges<ID> getIndexChanges(IndexDataSource<ID, ?> dataSource) throws IOException {
		Set<ID> notIndexedIds = collectToHashSet(dataSource.loadAllIds());
		Set<ID> indexedButRemovedFromDS = indexReaderTemplate
				.useReader(reader -> docsToRemove(notIndexedIds, reader));
		return new IndexChanges<>(notIndexedIds, indexedButRemovedFromDS);
	}

	protected void applyIndexChanges(
			IndexDataSource<ID, T> dataSource,
			IndexChanges<ID> changes) throws IOException {
		log.debug("\nremoving {} missing data from the index", changes.indexIdsMissingDataSize());
		// no IndexWriter flush
		indexRemover.removeMany(changes.indexedButRemovedFromDS());
		log.debug("\nextracting {} metadata to index", changes.notIndexedSize());
		Stream<T> items = dataSource.loadByIds(changes.notIndexedIds());
		log.debug("\nadding documents to the index");
		// with IndexWriter flush
		typedIndexAdderTemplate.useAdder(writer -> writer.addMany(items));
		log.debug("\nIndex restored!");
	}

	/**
	 * @return ids(reader) - ids
	 */
	protected Set<ID> docsToRemove(Set<ID> ids, TypedIndexReader<ID, ?> reader) {
		return collectToHashSet(reader.getAllIds().filter(id -> !ids.remove(id)));
	}
}
