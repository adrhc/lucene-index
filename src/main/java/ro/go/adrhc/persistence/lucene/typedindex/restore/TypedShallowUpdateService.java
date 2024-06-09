package ro.go.adrhc.persistence.lucene.typedindex.restore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
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
public class TypedShallowUpdateService<ID, T> implements ShallowUpdateService<ID, T> {
	private final TypedIndexReaderTemplate<ID, ?> indexReaderTemplate;
	private final TypedIndexRemover<ID> indexRemover;
	private final TypedIndexAdderTemplate<T> typedIndexAdderTemplate;

	/**
	 * constructor parameters union
	 */
	public static <ID, T> TypedShallowUpdateService<ID, T>
	create(TypedShallowUpdateServiceParams<T> params) {
		return new TypedShallowUpdateService<>(
				TypedIndexReaderTemplate.create(params.toAllHitsTypedIndexReaderParams()),
				TypedIndexRemover.create(params.toTypedIndexRemoverParams()),
				TypedIndexAdderTemplate.create(params));
	}

	@Override
	public void shallowUpdate(IndexDataSource<ID, T> dataSource) throws IOException {
		IndexChanges<ID> changes = getIndexChanges(dataSource, null);
		if (changes.hasChanges()) {
			applyIndexChanges(dataSource, changes);
		} else {
			log.debug("\nNo changes detected!");
		}
	}

	@Override
	public void shallowUpdateSubset(IndexDataSource<ID, T> dataSource, Query query)
			throws IOException {
		IndexChanges<ID> changes = getIndexChanges(dataSource, query);
		if (changes.hasChanges()) {
			applyIndexChanges(dataSource, changes);
		} else {
			log.debug("\nNo changes detected!");
		}
	}

	protected IndexChanges<ID> getIndexChanges(
			IndexDataSource<ID, ?> dataSource, Query query) throws IOException {
		Set<ID> notIndexedIds = collectToHashSet(dataSource.loadAllIds());
		Set<ID> indexedButRemovedFromDS = indexReaderTemplate
				.useReader(reader -> docsToRemove(query, notIndexedIds, reader));
		return new IndexChanges<>(notIndexedIds, indexedButRemovedFromDS);
	}

	protected void applyIndexChanges(
			IndexDataSource<ID, T> dataSource,
			IndexChanges<ID> changes) throws IOException {
		log.debug("\nremoving {} surplus documents from the index",
				changes.indexIdsMissingDataSize());
		// no IndexWriter flush
		indexRemover.removeMany(changes.indexedButRemovedFromDS());
		log.debug("\nextracting metadata for {} documents", changes.notIndexedSize());
		Stream<T> items = dataSource.loadByIds(changes.notIndexedIds());
		log.debug("\nadding missing documents to the index");
		// with IndexWriter flush
		typedIndexAdderTemplate.useAdder(writer -> writer.addMany(items));
		log.debug("\nIndex updated (shallow)!");
	}

	/**
	 * @return ids(reader) - ids
	 */
	protected Set<ID> docsToRemove(Query query, Set<ID> ids,
			TypedIndexReader<ID, ?> reader) throws IOException {
		if (query == null) {
			return collectToHashSet(reader.getAllIds().filter(id -> !ids.remove(id)));
		} else {
			return collectToHashSet(reader.findIds(query).filter(id -> !ids.remove(id)));
		}
	}
}
