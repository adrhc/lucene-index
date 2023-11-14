package ro.go.adrhc.persistence.lucene.typedindex.search;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.typedcore.ExactQuery;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIdIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TypedIdSearchService<ID, T> implements IdSearchService<ID, T> {
	private final ExactQuery exactQuery;
	private final TypedIndexReaderTemplate<T> indexReaderTemplate;
	private final TypedIdIndexReaderTemplate<ID> typedIdIndexReaderTemplate;

	public static <ID, T> TypedIdSearchService<ID, T>
	create(TypedIdSearchServiceParams<T> params) {
		return new TypedIdSearchService<>(
				ExactQuery.create(params.getIdField()),
				TypedIndexReaderTemplate.create(params),
				TypedIdIndexReaderTemplate.create(params));
	}

	public List<ID> getAllIds() throws IOException {
		return typedIdIndexReaderTemplate.useIdsReader(idsReader -> idsReader.getAllIds().toList());
	}

	public Optional<T> findById(ID id) throws IOException {
		return indexReaderTemplate.useReader(reader ->
				reader.findFirst(exactQuery.newExactQuery(id)));
	}
}
