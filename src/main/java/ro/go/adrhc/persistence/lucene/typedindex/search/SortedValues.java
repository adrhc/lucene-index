package ro.go.adrhc.persistence.lucene.typedindex.search;

import org.apache.lucene.search.ScoreDoc;
import ro.go.adrhc.util.stream.StreamAware;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public record SortedValues<T>(List<T> values, List<ScoreDoc> scoreDocs)
		implements StreamAware<T> {
	public ScoreDoc firstPosition() {
		return scoreDocs.getFirst();
	}

	public ScoreDoc lastPosition() {
		return scoreDocs.getLast();
	}

	public SortedValues<T> removeFirst() {
		return new SortedValues<>(values.subList(1, values.size()),
				scoreDocs.subList(1, scoreDocs.size()));
	}

	public SortedValues<T> removeLast() {
		return new SortedValues<>(values.subList(0, values.size() - 1),
				scoreDocs.subList(0, scoreDocs.size() - 1));
	}

	public <U> SortedValues<U> map(Function<? super List<T>, List<U>> mapper) {
		return new SortedValues<>(mapper.apply(values), scoreDocs);
	}

	public SortedValues<T> reverse() {
		return new SortedValues<>(values.reversed(), scoreDocs.reversed());
	}

	public int size() {
		return values.size();
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	@Override
	public Stream<T> rawStream() {
		return values.stream();
	}
}
