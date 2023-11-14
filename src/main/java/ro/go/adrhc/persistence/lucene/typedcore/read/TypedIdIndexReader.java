package ro.go.adrhc.persistence.lucene.typedcore.read;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.read.DocumentsIndexReader;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.util.ObjectUtils;

import java.io.Closeable;
import java.io.IOException;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TypedIdIndexReader<ID> implements Closeable {
	private final TypedField<?> idField;
	private final DocumentsIndexReader indexReader;

	public static <ID> TypedIdIndexReader<ID> create(TypedIdIndexReaderParams params) throws IOException {
		return new TypedIdIndexReader<>(params.getIdField(),
				DocumentsIndexReader.create(Integer.MAX_VALUE, params.getIndexReaderPool()));
	}

	public Stream<ID> getAll() {
		return indexReader.getFieldOfAll(idField.name())
				.map(field -> idField.fieldValueAccessor().apply(field))
				.map(ObjectUtils::cast);
	}

	@Override
	public void close() throws IOException {
		indexReader.close();
	}
}
