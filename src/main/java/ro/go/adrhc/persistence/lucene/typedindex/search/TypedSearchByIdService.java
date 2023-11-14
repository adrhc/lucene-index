package ro.go.adrhc.persistence.lucene.typedindex.search;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.typedcore.ExactQuery;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderTemplate;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class TypedSearchByIdService<ID, T> implements SearchByIdService<ID, T> {
	private final ExactQuery exactQuery;
	private final TypedIndexReaderTemplate<T> indexReaderTemplate;

	public static <ID, T> TypedSearchByIdService<ID, T>
	create(TypedSearchByIdServiceParams<T> params) {
		return new TypedSearchByIdService<>(
				ExactQuery.create(params.getIdField()),
				TypedIndexReaderTemplate.create(params));
	}

	public Optional<T> findById(ID id) throws IOException {
		return indexReaderTemplate.useReader(reader ->
				reader.findFirst(exactQuery.newExactQuery(id)));
	}
}
