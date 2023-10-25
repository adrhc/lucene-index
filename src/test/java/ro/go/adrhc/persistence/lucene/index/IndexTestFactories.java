package ro.go.adrhc.persistence.lucene.index;

import org.apache.lucene.analysis.Analyzer;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexCreateService;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.core.analysis.AnalyzerFactory;
import ro.go.adrhc.persistence.lucene.index.core.docds.DocumentsDataSource;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizationUtils;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchResultFactory;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.index.search.SearchedToQueryConverter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.rainerhahnekamp.sneakythrow.Sneaky.sneak;
import static ro.go.adrhc.persistence.lucene.index.core.tokenizer.PatternsAndReplacement.caseInsensitive;

public class IndexTestFactories {
	public static final Analyzer ANALYZER = sneak(IndexTestFactories::createAnalyzer);
	public static final TokenizationUtils TOKENIZATION_UTILS = new TokenizationUtils(ANALYZER);

	public static <T> IndexSearchService<String, T> createIndexSearchService(
			SearchedToQueryConverter<String> toQueryConverter,
			IndexSearchResultFactory<String, T> toFoundConverter,
			Path indexPath) {
		return new IndexSearchService<>(
				createDocumentIndexReaderTemplate(indexPath),
				toQueryConverter, toFoundConverter,
				Stream::findFirst
		);
	}

	public static FSIndexCreateService createFSIndexCreateService(
			DocumentsDataSource documentsDatasource, Path indexPath) {
		return new FSIndexCreateService(documentsDatasource,
				FSIndexUpdateService.create(ANALYZER, indexPath));
	}

	public static FSIndexUpdateService createFSIndexUpdateService(Enum<?> idField, Path indexPath) {
		return FSIndexUpdateService.create(idField, ANALYZER, indexPath);
	}

	private static DocumentIndexReaderTemplate createDocumentIndexReaderTemplate(Path indexPath) {
		return new DocumentIndexReaderTemplate(10, indexPath);
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
