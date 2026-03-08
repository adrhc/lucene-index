package ro.go.adrhc.persistence.lucene.operations.restore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReader;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexAdder;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexRemover;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Stream;

import static ro.go.adrhc.util.stream.StreamUtils.collectToHashSet;

@RequiredArgsConstructor
@Slf4j
public class IndexShallowUpdateServiceImpl<I, T> implements IndexShallowUpdateService<I, T> {
	private final HitsLimitedIndexReaderTemplate<I, ?> hitsLimitedIndexReaderTemplate;
	private final TypedIndexRemover<I> typedIndexRemover;
	private final TypedIndexAdder<T> typedIndexAdder;

	/**
	 * constructor parameters union
	 */
	public static <I, T> IndexShallowUpdateServiceImpl<I, T>
	create(IndexShallowUpdateServiceParams<T> params) {
		return new IndexShallowUpdateServiceImpl<>(
			HitsLimitedIndexReaderTemplate.create(params.allHitsTypedIndexReaderParams()),
			TypedIndexRemover.create(params.typedIndexRemoverParams()),
			TypedIndexAdder.create(params));
	}

	@Override
	public void shallowUpdate(IndexDataSource<I, T> dataSource) throws IOException {
		IndexChanges<I> changes = getIndexChanges(dataSource, null);
		if (changes.hasChanges()) {
			applyIndexChanges(dataSource, changes);
		} else {
			log.debug("\nNo changes detected!");
		}
	}

	@Override
	public void shallowUpdateSubset(IndexDataSource<I, T> dataSource, Query query)
		throws IOException {
		IndexChanges<I> changes = getIndexChanges(dataSource, query);
		if (changes.hasChanges()) {
			applyIndexChanges(dataSource, changes);
		} else {
			log.debug("\nNo changes detected!");
		}
	}

	protected IndexChanges<I> getIndexChanges(
		IndexDataSource<I, ?> dataSource, Query query) throws IOException {
		Set<I> notIndexedIds = collectToHashSet(dataSource.loadAllIds());
		Set<I> indexedButRemovedFromDS = hitsLimitedIndexReaderTemplate
			.useReader(reader -> docsToRemove(query, notIndexedIds, reader));
		return new IndexChanges<>(notIndexedIds, indexedButRemovedFromDS);
	}

	protected void applyIndexChanges(
		IndexDataSource<I, T> dataSource,
		IndexChanges<I> changes) throws IOException {
		log.debug("\nremoving {} surplus documents from the index",
			changes.indexIdsMissingDataSize());
		typedIndexRemover.removeMany(changes.indexedButRemovedFromDS());
		log.debug("\nextracting metadata for {} documents", changes.notIndexedSize());
		Stream<T> items = dataSource.loadByIds(changes.notIndexedIds());
		log.debug("\nadding missing documents to the index");
		typedIndexAdder.addMany(items);
		log.debug("\ncommiting changes to the index");
		typedIndexAdder.commit();
		log.debug("\nIndex updated (shallow)!");
	}

	/**
	 * @return ids(reader) - ids
	 */
	protected Set<I> docsToRemove(Query query, Set<I> ids,
		HitsLimitedIndexReader<I, ?> reader) throws IOException {
		if (query == null) {
			return collectToHashSet(reader.getAllIds().filter(id -> !ids.remove(id)));
		} else {
			return collectToHashSet(reader.findIds(query).filter(id -> !ids.remove(id)));
		}
	}
}
