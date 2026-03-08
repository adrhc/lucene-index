package ro.go.adrhc.persistence.lucene.core.typed.read;

import com.rainerhahnekamp.sneakythrow.functional.SneakyConsumer;
import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import com.rainerhahnekamp.sneakythrow.functional.SneakySupplier;
import lombok.RequiredArgsConstructor;
import ro.go.adrhc.util.Assert;

import java.io.IOException;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class HitsLimitedIndexReaderTemplate<I, T> {
	private final SneakySupplier<HitsLimitedIndexReader<I, T>, IOException> hitsLimitedIndexReaderFactory;

	public static <I, T> HitsLimitedIndexReaderTemplate<I, T>
	create(HitsLimitedIndexReaderParams<T> params) {
		return new HitsLimitedIndexReaderTemplate<>(() -> HitsLimitedIndexReader.create(params));
	}

	public <E extends Exception> void withReader(
		SneakyConsumer<HitsLimitedIndexReader<I, T>, E> indexReaderConsumer)
		throws IOException, E {
		try (HitsLimitedIndexReader<I, T> reader = hitsLimitedIndexReaderFactory.get()) {
			indexReaderConsumer.accept(reader);
		}
	}

	/**
	 * The result must NOT be a Stream!!!
	 */
	public <R, E extends Exception> R useReader(
		SneakyFunction<HitsLimitedIndexReader<I, T>, R, E> indexReaderFn)
		throws IOException, E {
		try (HitsLimitedIndexReader<I, T> reader = hitsLimitedIndexReaderFactory.get()) {
			R result = indexReaderFn.apply(reader);
			Assert.isTrue(!(result instanceof Stream<?>), "Result must not be a Stream!");
			return result;
		}
	}
}
