package ro.go.adrhc.persistence.lucene.typedcore.read;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import com.rainerhahnekamp.sneakythrow.functional.SneakySupplier;
import lombok.RequiredArgsConstructor;
import ro.go.adrhc.util.Assert;

import java.io.IOException;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TypedIdIndexReaderTemplate<ID> {
	private final SneakySupplier<TypedIdIndexReader<ID>, IOException> idReaderFactory;

	public static <ID> TypedIdIndexReaderTemplate<ID>
	create(TypedIdIndexReaderParams params) {
		return new TypedIdIndexReaderTemplate<>(() -> TypedIdIndexReader.create(params));
	}

	public <R, E extends Exception> R useIdsReader(
			SneakyFunction<TypedIdIndexReader<ID>, R, E> idsReaderFn)
			throws IOException, E {
		try (TypedIdIndexReader<ID> idsReader = idReaderFactory.get()) {
			R result = idsReaderFn.apply(idsReader);
			Assert.isTrue(!(result instanceof Stream<?>), "Result must not be a stream!");
			return result;
		}
	}
}
