package ro.go.adrhc.persistence.lucene.typedcore.read;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import com.rainerhahnekamp.sneakythrow.functional.SneakySupplier;
import lombok.RequiredArgsConstructor;
import ro.go.adrhc.util.Assert;

import java.io.IOException;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class OneHitIndexReaderTemplate<T> {
	private final SneakySupplier<OneHitIndexReader<T>, IOException> idReaderFactory;

	public static <T> OneHitIndexReaderTemplate<T>
	create(OneHitIndexReaderParams<T> params) {
		return new OneHitIndexReaderTemplate<>(() -> OneHitIndexReader.create(params));
	}

	public <R, E extends Exception> R useOneHitReader(
			SneakyFunction<OneHitIndexReader<T>, R, E> idsReaderFn)
			throws IOException, E {
		try (OneHitIndexReader<T> idsReader = idReaderFactory.get()) {
			R result = idsReaderFn.apply(idsReader);
			Assert.isTrue(!(result instanceof Stream<?>), "Result must not be a stream!");
			return result;
		}
	}
}
