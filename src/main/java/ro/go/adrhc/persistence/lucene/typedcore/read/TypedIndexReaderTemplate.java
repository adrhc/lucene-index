package ro.go.adrhc.persistence.lucene.typedcore.read;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import com.rainerhahnekamp.sneakythrow.functional.SneakySupplier;
import lombok.RequiredArgsConstructor;
import ro.go.adrhc.util.Assert;

import java.io.IOException;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TypedIndexReaderTemplate<ID, T> {
	private final SneakySupplier<TypedIndexReader<ID, T>, IOException> indexReaderFactory;

	public static <ID, T> TypedIndexReaderTemplate<ID, T> create(TypedIndexReaderParams<T> params) {
		return new TypedIndexReaderTemplate<>(() -> TypedIndexReader.create(params));
	}

	public <R, E extends Exception> R useReader(
			SneakyFunction<TypedIndexReader<ID, T>, R, E> indexReaderFn)
			throws IOException, E {
		try (TypedIndexReader<ID, T> reader = indexReaderFactory.get()) {
			R result = indexReaderFn.apply(reader);
			Assert.isTrue(!(result instanceof Stream<?>), "Result must not be a stream!");
			return result;
		}
	}

	/*public <R> R transform(Function<Stream<T>, R> transformer) throws IOException {
		return docIndexReaderTemplate.transformDocuments(curry(this::doTransform, transformer));
	}

	public <R> R transform(Set<String> fieldNames, Function<Stream<T>, R> transformer) throws IOException {
		return docIndexReaderTemplate.transformDocuments(fieldNames, curry(this::doTransform, transformer));
	}

	public <V, R> R transformFieldValues(TypedField<?> typedField,
			SneakyFunction<Stream<V>, R, IOException> transformer) throws IOException {
		return docIndexReaderTemplate.transformFields(typedField.name(),
				fields -> transformer.apply(fields.map(cast(typedField.fieldValueAccessor()))));
	}

	public <R> R transformFieldValues(String fieldName,
			SneakyFunction<Stream<String>, R, IOException> transformer) throws IOException {
		return docIndexReaderTemplate.transformFieldValues(fieldName, transformer);
	}

	private <R> R doTransform(
			Function<Stream<T>, R> transformer, Stream<Document> documents) {
		return transformer.apply(documents.map(docToTypedConverter::convert).flatMap(Optional::stream));
	}*/
}
