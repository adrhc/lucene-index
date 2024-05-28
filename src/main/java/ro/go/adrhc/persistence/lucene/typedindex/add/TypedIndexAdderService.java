package ro.go.adrhc.persistence.lucene.typedindex.add;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexAdderTemplate;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexWriterParams;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TypedIndexAdderService<T> implements IndexAdderService<T> {
	private final TypedIndexAdderTemplate<T> indexAdderTemplate;

	public static <T> TypedIndexAdderService<T> create(TypedIndexWriterParams<T> params) {
		return new TypedIndexAdderService<>(TypedIndexAdderTemplate.create(params));
	}

	@Override
	public void addOne(T t) throws IOException {
		indexAdderTemplate.useAdder(adder -> adder.addOne(t));
	}

	@Override
	public void addMany(Collection<T> tCollection) throws IOException {
		indexAdderTemplate.useAdder(adder -> adder.addMany(tCollection));
	}

	@Override
	public void addMany(Stream<T> tStream) throws IOException {
		indexAdderTemplate.useAdder(adder -> adder.addMany(tStream));
	}
}
