package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexCreateService;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.core.analysis.AnalyzerFactory;
import ro.go.adrhc.persistence.lucene.index.core.docds.DocumentsDataSource;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.index.search.BestMatchingStrategy;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.index.search.SearchedToQueryConverter;
import ro.go.adrhc.persistence.lucene.typedindex.core.DocumentToTypedConverter;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchResultFactory;

import java.io.IOException;
import java.nio.file.Path;

@RequiredArgsConstructor
public class TypedIndexFactories<F> {
	private final int maxResultsPerSearchedItem;
	private final Class<F> foundClass;
	private final Analyzer analyzer;

	public static <F> TypedIndexFactories<F> of(int maxResultsPerSearchedItem,
			Class<F> foundClass, TokenizerProperties tokenizerProperties) throws IOException {
		AnalyzerFactory analyzerFactory = new AnalyzerFactory(tokenizerProperties);
		return new TypedIndexFactories<>(maxResultsPerSearchedItem, foundClass, analyzerFactory.create());
	}

	public <S> IndexSearchService<S, TypedSearchResult<S, F>> createTypedFSIndexSearchService(
			SearchedToQueryConverter<S> toQueryConverter,
			BestMatchingStrategy<TypedSearchResult<S, F>> bestMatchingStrategy,
			Path indexPath) {
		return new IndexSearchService<>(
				createDocumentIndexReaderTemplate(indexPath), toQueryConverter,
				createTypedSearchResultFactoryFactory(foundClass),
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

	private DocumentIndexReaderTemplate createDocumentIndexReaderTemplate(Path indexPath) {
		return new DocumentIndexReaderTemplate(maxResultsPerSearchedItem, indexPath);
	}

	private static <S, F> TypedSearchResultFactory<S, F>
	createTypedSearchResultFactoryFactory(Class<F> fClass) {
		return new TypedSearchResultFactory<>(DocumentToTypedConverter.of(fClass)::convert);
	}
}
