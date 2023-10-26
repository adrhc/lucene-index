package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexCreateService;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.core.analysis.AnalyzerFactory;
import ro.go.adrhc.persistence.lucene.index.core.docds.datasource.DocumentsDataSource;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.index.search.BestMatchingStrategy;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.index.search.SearchedToQueryConverter;
import ro.go.adrhc.persistence.lucene.typedindex.core.DocumentToTypedConverter;
import ro.go.adrhc.persistence.lucene.typedindex.core.TypedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchResultFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TypedIndexFactories<T> {
	private final int maxResultsPerSearchedItem;
	private final Class<T> tClass;
	@Getter
	private final Analyzer analyzer;

	public static <T> TypedIndexFactories<T> of(int maxResultsPerSearchedItem,
			Class<T> foundClass, TokenizerProperties tokenizerProperties) throws IOException {
		AnalyzerFactory analyzerFactory = new AnalyzerFactory(tokenizerProperties);
		return new TypedIndexFactories<>(maxResultsPerSearchedItem, foundClass, analyzerFactory.create());
	}

	public <S> IndexSearchService<S, TypedSearchResult<S, T>> createTypedFSIndexSearchService(
			SearchedToQueryConverter<S> toQueryConverter, Path indexPath) {
		return createTypedFSIndexSearchService(toQueryConverter, Stream::findFirst, indexPath);
	}

	public <S> IndexSearchService<S, TypedSearchResult<S, T>> createTypedFSIndexSearchService(
			SearchedToQueryConverter<S> toQueryConverter,
			BestMatchingStrategy<TypedSearchResult<S, T>> bestMatchingStrategy,
			Path indexPath) {
		return new IndexSearchService<>(
				createDocumentIndexReaderTemplate(indexPath), toQueryConverter,
				createTypedSearchResultFactory(),
				bestMatchingStrategy
		);
	}

	public FSIndexCreateService createFSIndexCreateService(
			DocumentsDataSource documentsDatasource, Path indexPath) {
		return new FSIndexCreateService(documentsDatasource,
				FSIndexUpdateService.create(analyzer, indexPath));
	}

	public FSIndexUpdateService createFSIndexUpdateService(Enum<?> idField, Path indexPath) {
		return FSIndexUpdateService.create(idField, analyzer, indexPath);
	}

	public TypedIndexReaderTemplate<T> createTypedIndexReaderTemplate(Path indexPath) {
		return new TypedIndexReaderTemplate<>(DocumentToTypedConverter.of(tClass),
				createDocumentIndexReaderTemplate(indexPath));
	}

	public DocumentIndexReaderTemplate createDocumentIndexReaderTemplate(Path indexPath) {
		return new DocumentIndexReaderTemplate(maxResultsPerSearchedItem, indexPath);
	}

	private <S> TypedSearchResultFactory<S, T> createTypedSearchResultFactory() {
		return new TypedSearchResultFactory<>(DocumentToTypedConverter.of(tClass)::convert);
	}
}
