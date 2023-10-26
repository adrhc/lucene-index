package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexCreateService;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.core.analysis.AnalyzerFactory;
import ro.go.adrhc.persistence.lucene.index.core.docds.DocumentsDataSource;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.index.search.BestMatchingStrategy;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.index.search.SearchedToQueryConverter;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchResultFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class TypedIndexFactories {
	private final int maxResultsPerSearchedItem;
	private final Analyzer analyzer;

	public static TypedIndexFactories of(int maxResultsPerSearchedItem,
			TokenizerProperties tokenizerProperties) throws IOException {
		AnalyzerFactory analyzerFactory = new AnalyzerFactory(tokenizerProperties);
		return new TypedIndexFactories(maxResultsPerSearchedItem, analyzerFactory.create());
	}

	public <S, T> IndexSearchService<S, TypedSearchResult<S, T>> createTypedFSIndexSearchService(
			SearchedToQueryConverter<S> toQueryConverter,
			Function<Document, Optional<T>> docToTypeConverter,
			BestMatchingStrategy<TypedSearchResult<S, T>> bestMatchingStrategy,
			Path indexPath) {
		return new IndexSearchService<>(
				createDocumentIndexReaderTemplate(indexPath), toQueryConverter,
				createTypedSearchResultFactoryFactory(docToTypeConverter),
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
	createTypedSearchResultFactoryFactory(Function<Document, Optional<F>> documentToFound) {
		return new TypedSearchResultFactory<>(documentToFound);
	}
}
