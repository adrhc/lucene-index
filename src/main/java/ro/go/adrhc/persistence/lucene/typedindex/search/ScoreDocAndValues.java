package ro.go.adrhc.persistence.lucene.typedindex.search;

import org.apache.lucene.search.ScoreDoc;
import ro.go.adrhc.util.stream.StreamAware;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public record ScoreDocAndValues<T>(List<T> values, List<ScoreDoc> scoreDocs)
		implements StreamAware<T> {
	public ScoreDoc firstPosition() {
		return scoreDocs.getFirst();
	}

	public ScoreDoc lastPosition() {
		return scoreDocs.getLast();
	}

	public ScoreDocAndValues<T> removeFirst() {
		return new ScoreDocAndValues<>(values.subList(1, values.size()),
				scoreDocs.subList(1, scoreDocs.size()));
	}

	public ScoreDocAndValues<T> removeLast() {
		return new ScoreDocAndValues<>(values.subList(0, values.size() - 2),
				scoreDocs.subList(0, scoreDocs.size() - 2));
	}

	public <U> ScoreDocAndValues<U> map(Function<? super List<T>, List<U>> mapper) {
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
	public Stream<T> rawStream() {
		return values.stream();
	}
}
