package ro.go.adrhc.persistence.lucene.typedindex.restore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIdIndexReader;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIdIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexAdderTemplate;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemover;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Stream;

import static ro.go.adrhc.util.collection.StreamUtils.collectToHashSet;

@RequiredArgsConstructor
@Slf4j
public class TypedIndexRestoreService<ID, T> implements IndexRestoreService<ID, T> {
	private final TypedIdIndexReaderTemplate<ID> typedIdIndexReaderTemplate;
	private final TypedIndexRemover<ID> indexRemover;
	private final TypedIndexAdderTemplate<T> typedIndexAdderTemplate;

	/**
	 * constructor parameters union
	 */
	public static <ID, T> TypedIndexRestoreService<ID, T>
	create(TypedIndexRestoreServiceParams<T> params) {
		return new TypedIndexRestoreService<>(
				TypedIdIndexReaderTemplate.create(params),
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

	private IndexChanges<ID> getIndexChanges(IndexDataSource<ID, ?> dataSource) throws IOException {
		Set<ID> allDsIds = collectToHashSet(dataSource.loadAllIds());
		Set<ID> docsToRemove = typedIdIndexReaderTemplate
				.useIdsReader(idsReader -> docsToRemove(allDsIds, idsReader));
		return new IndexChanges<>(allDsIds, docsToRemove);
	}

	private void applyIndexChanges(
			IndexDataSource<ID, T> dataSource,
			IndexChanges<ID> changes) throws IOException {
		log.debug("\nremoving {} missing data from the index", changes.indexIdsMissingDataSize());
		// no IndexWriter flush
		indexRemover.removeMany(changes.obsoleteIndexedIds());
		log.debug("\nextracting {} metadata to index", changes.notIndexedSize());
		Stream<T> items = dataSource.loadByIds(changes.notIndexedIds());
		log.debug("\nadding documents to the index");
		// with IndexWriter flush
		typedIndexAdderTemplate.useAdder(writer -> writer.addMany(items));
		log.debug("\nIndex updated!");
	}

	private Set<ID> docsToRemove(Set<ID> ids, TypedIdIndexReader<ID> idsReader) {
		return collectToHashSet(idsReader.getAll().filter(id -> !ids.remove(id)));
	}
}
