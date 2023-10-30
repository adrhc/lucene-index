package ro.go.adrhc.persistence.lucene.typedindex.domain.seach;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.index.core.read.ScoreAndDocument;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchResultFactory;
import ro.go.adrhc.persistence.lucene.typedindex.domain.docserde.DocumentToTypedConverter;

import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class QuerySearchResultFactory<F>
		implements IndexSearchResultFactory<QuerySearchResult<F>> {
	private final Function<Document, Optional<F>> documentToFound;

	public static <T> QuerySearchResultFactory<T> create(Class<T> tClass) {
		return new QuerySearchResultFactory<>(DocumentToTypedConverter.of(tClass)::convert);
	}

	@Override
	public Optional<QuerySearchResult<F>>
	create(Query query, ScoreAndDocument scoreAndDocument) {
		return documentToFound.apply(scoreAndDocument.document())
				.map(found -> new QuerySearchResult<>(query, scoreAndDocument.score(), found));
	}
}
