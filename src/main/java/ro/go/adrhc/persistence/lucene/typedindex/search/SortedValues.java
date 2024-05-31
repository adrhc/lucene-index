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

	public <Y> SortedValues<Y> map(Function<? super List<T>, List<Y>> mapper) {
		return new SortedValues<>(mapper.apply(values), first, last);
	}

	@Override
	public Stream<T> rawStream() {
		return values.stream();
	}
}
