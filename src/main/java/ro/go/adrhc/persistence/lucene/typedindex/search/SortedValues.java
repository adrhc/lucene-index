package ro.go.adrhc.persistence.lucene.typedindex.search;

import org.apache.lucene.search.ScoreDoc;
import ro.go.adrhc.util.stream.StreamAware;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public record SortedValues<T>(List<T> values, ScoreDoc first, ScoreDoc last)
		implements StreamAware<T> {
	public boolean isEmpty() {
		return values.isEmpty();
	}

	public <U> SortedValues<U> map(Function<? super List<T>, List<U>> mapper) {
		return new SortedValues<>(mapper.apply(values), first, last);
	}

	public SortedValues<T> reverse() {
		return new SortedValues<>(values.reversed(), last, first);
	}

	@Override
	public Stream<T> rawStream() {
		return values.stream();
	}
}
