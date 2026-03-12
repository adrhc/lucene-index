package ro.go.adrhc.persistence.lucene.service.search;

import org.apache.lucene.search.ScoreDoc;
import ro.go.adrhc.util.stream.StreamOwner;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public record ScoreDocAndValues<T>(List<T> values, List<ScoreDoc> scoreDocs)
	implements StreamOwner<T> {
	public ScoreDoc firstPosition() {
		return scoreDocs.getFirst();
	}

	public ScoreDoc lastPosition() {
		return scoreDocs.getLast();
	}

	/**
	 * @return a new ScoreDocAndValues containing only the first {@code count} elements of the original lists
	 */
	public ScoreDocAndValues<T> truncate(int count) {
		return new ScoreDocAndValues<>(values.subList(0, count), scoreDocs.subList(0, count));
	}

	public <U> ScoreDocAndValues<U> mapValues(Function<? super List<T>, List<U>> mapper) {
		return new ScoreDocAndValues<>(mapper.apply(values), scoreDocs);
	}

	public ScoreDocAndValues<T> reverse() {
		return new ScoreDocAndValues<>(values.reversed(), scoreDocs.reversed());
	}

	public int size() {
		return values.size();
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	@Override
	public Stream<T> stream() {
		return values.stream();
	}
}
