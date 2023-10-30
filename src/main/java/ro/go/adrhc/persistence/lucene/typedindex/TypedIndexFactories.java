package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.index.core.analysis.AnalyzerFactory;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.index.domain.queries.SearchedToQueryConverter;
import ro.go.adrhc.persistence.lucene.index.restore.DocumentsIndexRestoreService;
import ro.go.adrhc.persistence.lucene.index.search.BestMatchingStrategy;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedFieldEnum;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.TypedSearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.TypedSearchResultFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public class TypedIndexFactories<ID, T extends Identifiable<ID>, E extends Enum<E> & TypedFieldEnum<T>> {
	private final int numHits;
	private final Analyzer analyzer;
	private final Class<T> tClass;
	private final Class<E> tFieldEnumClass;

	/**
	 * constructor parameters union
	 */
	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedFieldEnum<T>>
	TypedIndexFactories<ID, T, E> create(int numHits, Class<T> tClass,
			Class<E> typedFieldEnumClass, TokenizerProperties tokenizerProperties) throws IOException {
		AnalyzerFactory analyzerFactory = new AnalyzerFactory(tokenizerProperties);
		return new TypedIndexFactories<>(numHits,
				analyzerFactory.create(), tClass, typedFieldEnumClass);
	}

	/**
	 * SearchedToQueryConverter: Optional::of (aka searching by Query)
	 * BestMatchingStrategy: Stream::findFirst
	 * TypedSearchResultFilter: it -> true (aka no filter)
	 */
	public IndexSearchService<Query, TypedSearchResult<Query, T>>
	createTypedIndexSearchService(Path indexPath) {
		return createTypedIndexSearchService(Optional::of, Stream::findFirst, it -> true, indexPath);
	}

	public <S> IndexSearchService<S, TypedSearchResult<S, T>>
	createTypedIndexSearchService(SearchedToQueryConverter<S> toQueryConverter,
			BestMatchingStrategy<TypedSearchResult<S, T>> bestMatchingStrategy,
			TypedSearchResultFilter<S, T> searchResultFilter, Path indexPath) {
		return new IndexSearchService<>(
				new DocumentIndexReaderTemplate(numHits, indexPath),
				toQueryConverter,
				TypedSearchResultFactory.create(tClass),
				bestMatchingStrategy,
				searchResultFilter
		);
	}

	public TypedIndexCreateService<T> createTypedIndexCreateService(Path indexPath) {
		return TypedIndexCreateService.create(analyzer, tFieldEnumClass, indexPath);
	}

	public DocumentsIndexRestoreService createDocumentsIndexRestoreService(Path indexPath) {
		return DocumentsIndexRestoreService.create(getIdField(), analyzer, indexPath);
	}

	public TypedIndexUpdateService<ID, T> createTypedIndexUpdateService(Path indexPath) {
		return TypedIndexUpdateService.create(analyzer, tFieldEnumClass, indexPath);
	}

	private E getIdField() {
		return TypedFieldEnum.getIdField(tFieldEnumClass);
	}
}
