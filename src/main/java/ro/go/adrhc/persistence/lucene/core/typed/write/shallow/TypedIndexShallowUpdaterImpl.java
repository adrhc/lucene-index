package ro.go.adrhc.persistence.lucene.core.typed.write.shallow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReader;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexAdderImpl;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexRemover;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexRemoverImpl;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Stream;

import static ro.go.adrhc.util.stream.StreamUtils.collectToHashSet;

@RequiredArgsConstructor
@Slf4j
public class TypedIndexShallowUpdaterImpl<I, T> implements TypedIndexShallowUpdater<I, T> {
	private final TypedIndexReaderTemplate<I, ?> typedIndexReaderTemplate;
	private final TypedIndexRemover<I> typedIndexRemover;
	private final TypedIndexAdderImpl<T> typedIndexAdder;

	/**
	 * constructor parameters union
	 */
	public static <I, T> TypedIndexShallowUpdaterImpl<I, T>
	create(TypedIndexShallowUpdaterParams<T> params) {
		return new TypedIndexShallowUpdaterImpl<>(
			TypedIndexReaderTemplate.create(params.typedIndexReaderParams()),
			TypedIndexRemoverImpl.create(params), TypedIndexAdderImpl.create(params));
	}

	/**
	 * All dataSource documents not present in the index are added and
	 * all indexed documents not present in the dataSource are removed.
	 * <p>
	 * No existing document is updated!
	 */
	@Override
	public void shallowUpdate(TypedIndexDataSource<I, T> dataSource) throws IOException {
		TypedIndexChanges<I> changes = getIndexChanges(dataSource, null);
		if (changes.hasChanges()) {
			applyIndexChanges(dataSource, changes);
		} else {
			log.debug("\nNo changes detected!");
		}
	}

	/**
	 * All dataSource documents not present in the index are added and
	 * all indexed documents not present in the dataSource are removed.
	 * <p>
	 * The existing documents that are not part of the query result,
	 * will be updated, if they are present in the data source!
	 *
	 * @param query determines which indexed documents are compared with the data source, if null all indexed documents are compared
	 */
	@Override
	public void shallowUpdateSubset(TypedIndexDataSource<I, T> dataSource, Query query)
		throws IOException {
		TypedIndexChanges<I> changes = getIndexChanges(dataSource, query);
		if (changes.hasChanges()) {
			applyIndexChanges(dataSource, changes);
		} else {
			log.debug("\nNo changes detected!");
		}
	}

	/**
	 * @param query determines which indexed documents are compared with the data source, if null all indexed documents are compared
	 */
	protected TypedIndexChanges<I> getIndexChanges(
		TypedIndexDataSource<I, ?> dataSource, Query query) throws IOException {
		Set<I> notIndexedIds = collectToHashSet(dataSource.loadAllIds());
		Set<I> indexedButRemovedFromDS = typedIndexReaderTemplate
			.useReader(reader -> docsToRemove(query, notIndexedIds, reader));
		return new TypedIndexChanges<>(notIndexedIds, indexedButRemovedFromDS);
	}

	protected void applyIndexChanges(
		TypedIndexDataSource<I, T> dataSource,
		TypedIndexChanges<I> changes) throws IOException {
		log.debug("\nremoving {} surplus documents from the index",
			changes.indexIdsMissingDataSize());
		typedIndexRemover.removeMany(changes.indexedButRemovedFromDS());
		log.debug("\nextracting metadata for {} new documents", changes.notIndexedSize());
		Stream<T> items = dataSource.loadByIds(changes.notIndexedIds());
		log.debug("\nadding new documents to the index");
		typedIndexAdder.addMany(items);
		log.debug("\ncommiting changes to the index");
		typedIndexAdder.commit();
		log.debug("\nIndex updated (shallow)!");
	}

	/**
	 * The found documents (all if query is null) are removed from @ids!
	 *
	 * @return the document ids not present in @ids, i.e., reader.findIds(query) - ids
	 */
	protected Set<I> docsToRemove(Query query, Set<I> ids,
		TypedIndexReader<I, ?> reader) throws IOException {
		if (query == null) {
			return collectToHashSet(reader.getAllIds().filter(id -> !ids.remove(id)));
		} else {
			return collectToHashSet(reader.findIds(query).filter(id -> !ids.remove(id)));
		}
	}
}
