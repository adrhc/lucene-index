package ro.go.adrhc.persistence.lucene.core.typed.read;

import com.rainerhahnekamp.sneakythrow.functional.SneakyConsumer;
import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import com.rainerhahnekamp.sneakythrow.functional.SneakySupplier;
import lombok.RequiredArgsConstructor;
import ro.go.adrhc.util.Assert;

import java.io.IOException;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class HitsLimitedIndexReaderTemplate<ID, T> {
	private final SneakySupplier<HitsLimitedIndexReader<ID, T>, IOException> indexReaderFactory;

	public static <ID, T> HitsLimitedIndexReaderTemplate<ID, T>
	create(HitsLimitedIndexReaderParams<T> params) {
		return new HitsLimitedIndexReaderTemplate<>(() -> HitsLimitedIndexReader.create(params));
	}

	public <E extends Exception> void withReader(
		SneakyConsumer<HitsLimitedIndexReader<ID, T>, E> indexReaderConsumer)
		throws IOException, E {
		try (HitsLimitedIndexReader<ID, T> reader = indexReaderFactory.get()) {
			indexReaderConsumer.accept(reader);
		}
	}

	/**
	 * The result must NOT be a Stream!!!
	 */
	public <R, E extends Exception> R useReader(
		SneakyFunction<HitsLimitedIndexReader<ID, T>, R, E> indexReaderFn)
		throws IOException, E {
		try (HitsLimitedIndexReader<ID, T> reader = indexReaderFactory.get()) {
			R result = indexReaderFn.apply(reader);
			Assert.isTrue(!(result instanceof Stream<?>), "Result must not be a Stream!");
			return result;
		}
	}
}
