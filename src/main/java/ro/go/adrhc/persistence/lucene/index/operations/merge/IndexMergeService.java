package ro.go.adrhc.persistence.lucene.index.operations.merge;

import java.io.IOException;
import java.util.Collection;
import java.util.function.BinaryOperator;

public interface IndexMergeService<T> {
	void merge(T t) throws IOException;

	/**
	 * @param mergeStrategy 1st param is the stored value while the 2nd is @param t
	 * @param t             might be added (instead of merged) if is not stored yet
	 */
	void merge(T t, BinaryOperator<T> mergeStrategy) throws IOException;

	/**
	 * @param mergeStrategy 1st param is the stored value while the 2nd is a tCollection element
	 * @param tCollection   might be added (instead of merged) if is not stored yet
	 */
	void mergeMany(Collection<T> tCollection, BinaryOperator<T> mergeStrategy) throws IOException;
}
