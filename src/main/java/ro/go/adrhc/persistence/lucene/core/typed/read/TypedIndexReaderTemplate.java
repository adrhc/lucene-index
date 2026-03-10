package ro.go.adrhc.persistence.lucene.core.typed.read;

import com.rainerhahnekamp.sneakythrow.functional.SneakyConsumer;
import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import com.rainerhahnekamp.sneakythrow.functional.SneakySupplier;
import lombok.RequiredArgsConstructor;
import ro.go.adrhc.util.Assert;

import java.io.IOException;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TypedIndexReaderTemplate<I, T> {
	private final SneakySupplier<TypedIndexReader<I, T>, IOException> hitsLimitedIndexReaderFactory;

	public static <I, T> TypedIndexReaderTemplate<I, T>
	create(TypedIndexReaderParams<T> params) {
		return new TypedIndexReaderTemplate<>(() -> TypedIndexReader.create(params));
	}

	public <E extends Exception> void withReader(
		SneakyConsumer<TypedIndexReader<I, T>, E> indexReaderConsumer)
		throws IOException, E {
		try (TypedIndexReader<I, T> reader = hitsLimitedIndexReaderFactory.get()) {
			indexReaderConsumer.accept(reader);
		}
	}

	/**
	 * The result must NOT be a Stream!!!
	 */
	public <R, E extends Exception> R useReader(
		SneakyFunction<TypedIndexReader<I, T>, R, E> indexReaderFn)
		throws IOException, E {
		try (TypedIndexReader<I, T> reader = hitsLimitedIndexReaderFactory.get()) {
			R result = indexReaderFn.apply(reader);
			Assert.isTrue(!(result instanceof Stream<?>), "Result must not be a Stream!");
			return result;
		}
	}
}
