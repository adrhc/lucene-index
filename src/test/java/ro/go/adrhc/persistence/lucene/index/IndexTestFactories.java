package ro.go.adrhc.persistence.lucene.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.index.core.analysis.AnalyzerFactory;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizationUtils;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.index.domain.queries.FieldQueries;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.index.search.SearchedToQueryConverter;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexFactories;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.rainerhahnekamp.sneakythrow.Sneaky.sneak;
import static ro.go.adrhc.persistence.lucene.index.core.tokenizer.PatternsAndReplacement.caseInsensitive;

public class IndexTestFactories {
	public static final int MAX_RESULTS_PER_SEARCHED_ITEM = Integer.MAX_VALUE;
	public static final Analyzer ANALYZER = sneak(IndexTestFactories::createAnalyzer);
	public static final TokenizationUtils TOKENIZATION_UTILS = new TokenizationUtils(ANALYZER);

	public static <F> IndexSearchService<Query, TypedSearchResult<Query, F>>
	createTypedFSIndexSearchService(Class<F> foundClass, Path indexPath) {
		return createTypedIndexFactories(foundClass)
				.createTypedFSIndexSearchService(Optional::of, indexPath);
	}

	public static <F> IndexSearchService<String, TypedSearchResult<String, F>>
	createTypedFSIndexSearchService(Class<F> foundClass,
			SearchedToQueryConverter<String> toQueryConverter, Path indexPath) {
		return createTypedIndexFactories(foundClass)
				.createTypedFSIndexSearchService(toQueryConverter, indexPath);
	}

	public static DocumentIndexReaderTemplate createDocumentIndexReaderTemplate(Path indexPath) {
		return new DocumentIndexReaderTemplate(MAX_RESULTS_PER_SEARCHED_ITEM, indexPath);
	}

	public static <F> TypedIndexFactories<F> createTypedIndexFactories(Class<F> foundClass) {
		return new TypedIndexFactories<>(MAX_RESULTS_PER_SEARCHED_ITEM, ANALYZER, foundClass);
	}

	public static FieldQueries createFieldQuery(Enum<?> field) {
		return FieldQueries.create(ANALYZER, field);
	}

	private static Analyzer createAnalyzer() throws IOException {
		return createAnalyzerFactory().create();
	}

	private static AnalyzerFactory createAnalyzerFactory() {
		return new AnalyzerFactory(createTokenizerProperties());
	}

	private static TokenizerProperties createTokenizerProperties() {
		return new TokenizerProperties(2,
				List.of("Fixed Pattern To Remove"),
				List.of("\\(\\s*Regex\\s*Pattern\\s*To\\s*Remove\\)"),
				Map.of("_", " "),
				caseInsensitive("$1", "([^\\s]*)\\.jpe?g"));
	}
}
