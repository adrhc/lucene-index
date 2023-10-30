package ro.go.adrhc.persistence.lucene.typedindex.domain.seach;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.core.read.ScoreAndDocument;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchResultFactory;
import ro.go.adrhc.persistence.lucene.typedindex.domain.docserde.DocumentToTypedConverter;

import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class TypedSearchResultFactory<S, F>
		implements IndexSearchResultFactory<S, TypedSearchResult<S, F>> {
	private final Function<Document, Optional<F>> documentToFound;

	public static <S, T> TypedSearchResultFactory<S, T> create(Class<T> tClass) {
		return new TypedSearchResultFactory<>(DocumentToTypedConverter.of(tClass)::convert);
	}


	@Override
	public Optional<TypedSearchResult<S, F>> create(S searched, ScoreAndDocument scoreAndDocument) {
		return documentToFound.apply(scoreAndDocument.document())
				.map(found -> new TypedSearchResult<>(scoreAndDocument.score(), searched, found));
	}
}
