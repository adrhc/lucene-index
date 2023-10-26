package ro.go.adrhc.persistence.lucene.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.index.core.analysis.AnalyzerFactory;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizationUtils;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.index.domain.queries.FieldQuery;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.index.search.SearchedToQueryConverter;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexFactories;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.rainerhahnekamp.sneakythrow.Sneaky.sneak;
import static ro.go.adrhc.persistence.lucene.index.core.tokenizer.PatternsAndReplacement.caseInsensitive;

public class IndexTestFactories {
	public static final Analyzer ANALYZER = sneak(IndexTestFactories::createAnalyzer);
	public static final TokenizationUtils TOKENIZATION_UTILS = new TokenizationUtils(ANALYZER);
	public static final TypedIndexFactories INDEX_FACTORIES =
			new TypedIndexFactories(10, ANALYZER);

	public static <T> IndexSearchService<Query, TypedSearchResult<Query, T>>
	createTypedFSIndexSearchService(
			Function<Document, Optional<T>> docToTypeConverter,
			Path indexPath) {
		return INDEX_FACTORIES.createTypedFSIndexSearchService(
				Optional::of, docToTypeConverter, Stream::findFirst, indexPath
		);
	}

	public static <T> IndexSearchService<String, TypedSearchResult<String, T>>
	createTypedFSIndexSearchService(
			SearchedToQueryConverter<String> toQueryConverter,
			Function<Document, Optional<T>> docToTypeConverter,
			Path indexPath) {
		return INDEX_FACTORIES.createTypedFSIndexSearchService(
				toQueryConverter, docToTypeConverter, Stream::findFirst, indexPath
		);
	}

	public static FieldQuery createFieldQuery(Enum<?> field) {
		return FieldQuery.create(ANALYZER, field);
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
