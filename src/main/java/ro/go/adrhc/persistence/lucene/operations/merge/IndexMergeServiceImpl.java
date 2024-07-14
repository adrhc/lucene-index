package ro.go.adrhc.persistence.lucene.operations.merge;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.typed.Indexable;
import ro.go.adrhc.persistence.lucene.operations.add.IndexAddServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.retrieve.IndexRetrieveServiceImpl;
import ro.go.adrhc.persistence.lucene.operations.update.IndexUpsertServiceImpl;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;

import static ro.go.adrhc.persistence.lucene.core.typed.Identifiable.toIds;

@RequiredArgsConstructor
public class IndexMergeServiceImpl<T extends Indexable<ID, T>, ID>
		implements IndexMergeService<T> {
	private final IndexRetrieveServiceImpl<ID, T> retrieveService;
	private final IndexAddServiceImpl<T> addService;
	private final IndexUpsertServiceImpl<T> upsertService;

	public void merge(T t) throws IOException {
		merge(t, T::merge);
	}

	/**
	 * @param mergeStrategy 1st param is the stored value while the 2nd is @param t
	 * @param t             might be added (instead of merged) if is not stored yet
	 */
	public void merge(T t, BinaryOperator<T> mergeStrategy) throws IOException {
		Optional<T> storedOptional = retrieveService.findById(t.id());
		if (storedOptional.isEmpty()) {
			addService.addOne(t);
		} else {
			upsertService.upsert(mergeStrategy.apply(storedOptional.get(), t));
		}
	}

	/**
	 * @param mergeStrategy 1st param is the stored value while the 2nd is a tCollection element
	 * @param tCollection   might be added (instead of merged) if is not stored yet
	 */
	public void mergeMany(Collection<T> tCollection,
			BinaryOperator<T> mergeStrategy) throws IOException {
		Map<ID, T> stored = new HashMap<>();
		retrieveService.findByIds(toIds(tCollection)).forEach(t -> stored.put(t.id(), t));
		upsertService.upsertMany(
				tCollection.stream().map(t -> merge(mergeStrategy, stored, t)).toList());
	}

	private T merge(BinaryOperator<T> mergeStrategy, Map<ID, T> stored, T another) {
		T storedT = stored.get(another.id());
		return storedT == null ? another : mergeStrategy.apply(storedT, another);
	}
}
