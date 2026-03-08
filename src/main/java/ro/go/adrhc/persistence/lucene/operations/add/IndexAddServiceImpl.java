package ro.go.adrhc.persistence.lucene.operations.add;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexAdder;
import ro.go.adrhc.persistence.lucene.core.typed.write.TypedIndexWriterParams;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class IndexAddServiceImpl<T> implements IndexAddService<T> {
	private final TypedIndexAdder<T> typedIndexAdder;

	public static <T> IndexAddServiceImpl<T> create(TypedIndexWriterParams<T> params) {
		return new IndexAddServiceImpl<>(TypedIndexAdder.create(params));
	}

	@Override
	public void addOne(T t) throws IOException {
		typedIndexAdder.addOne(t);
	}

	@Override
	public void addMany(Collection<T> tCollection) throws IOException {
		typedIndexAdder.addMany(tCollection);
	}

	@Override
	public void addMany(Stream<T> tStream) throws IOException {
		typedIndexAdder.addMany(tStream);
	}
}
