package ro.go.adrhc.persistence.lucene.operations.merge;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.typed.Indexable;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexAdder;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexUpsert;
import ro.go.adrhc.persistence.lucene.operations.retrieve.IndexRetrieveServiceImpl;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;

import static ro.go.adrhc.persistence.lucene.core.typed.Identifiable.toIds;

@RequiredArgsConstructor
public class IndexMergeServiceImpl<T extends Indexable<I, T>, I> implements IndexMergeService<T> {
	private final IndexRetrieveServiceImpl<I, T> retrieveService;
	private final TypedIndexAdder<T> typedIndexAdder;
	private final TypedIndexUpsert<T> indexUpsert;

	public void merge(T t) throws IOException {
		mergeWithStrategy(t, T::merge);
	}

	/**
	 * @param mergeStrategy 1st param is the stored value while the 2nd is @param t
	 * @param t             might be added (instead of merged) if is not stored yet
	 */
	public void mergeWithStrategy(T t, BinaryOperator<T> mergeStrategy) throws IOException {
		Optional<T> storedOptional = retrieveService.findById(t.id());
		if (storedOptional.isEmpty()) {
			typedIndexAdder.addOne(t);
		} else {
			indexUpsert.upsert(mergeStrategy.apply(storedOptional.get(), t));
		}
	}

	/**
	 * @param mergeStrategy 1st param is the stored value while the 2nd is a tCollection element
	 * @param tCollection   might be added (instead of merged) if is not stored yet
	 */
	public void mergeMany(Collection<T> tCollection,
		BinaryOperator<T> mergeStrategy) throws IOException {
		Map<I, T> stored = new HashMap<>();
		retrieveService.findByIds(toIds(tCollection)).forEach(t -> stored.put(t.id(), t));
		indexUpsert.upsertMany(
			tCollection.stream().map(t -> merge(mergeStrategy, stored, t)).toList());
	}

	private T merge(BinaryOperator<T> mergeStrategy, Map<I, T> stored, T another) {
		T storedT = stored.get(another.id());
		return storedT == null ? another : mergeStrategy.apply(storedT, another);
	}
}
