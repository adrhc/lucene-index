package ro.go.adrhc.persistence.lucene.index;

import org.apache.lucene.analysis.Analyzer;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexCreateService;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.core.analysis.AnalyzerFactory;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.PatternsAndReplacement;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizationUtils;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchResultFactory;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.index.search.SearchedToQueryConverter;
import ro.go.adrhc.persistence.lucene.index.spi.DocumentsDatasource;
import ro.go.adrhc.persistence.lucene.typedindex.spi.RawDataDatasource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizerProperties.pairWithSpace;

public class IndexTestFactories {
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
			DocumentsDatasource documentsDatasource, Enum<?> idField, Path indexPath) {
		return new FSIndexCreateService(documentsDatasource, createFSIndexUpdateService(idField, indexPath));
	}

	public static FSIndexUpdateService createFSIndexUpdateService(Enum<?> idField, Path indexPath) {
		return FSIndexUpdateService.create(idField, createAnalyzer(), indexPath);
	}

	public static DocumentIndexReaderTemplate createDocumentIndexReaderTemplate(Path indexPath) {
		return new DocumentIndexReaderTemplate(10, indexPath);
	}

	public static TokenizationUtils createTokenizationUtils() {
		return new TokenizationUtils(createAnalyzerFactory());
	}

	public static Analyzer createAnalyzer() {
		return createAnalyzerFactory().create();
	}

	public static AnalyzerFactory createAnalyzerFactory() {
		return new AnalyzerFactory(TokenizerProperties.of(2,
				List.<String[]>of(pairWithSpace("_")),
				new PatternsAndReplacement("$1", List.of("([^\\s]*)\\.jpe?g"))));
	}

	public static <ID, T> RawDataDatasource<ID, T> createDatasource(Function<T, ID> idAccessor, Collection<T> tCollection) {
		return new RawDataDatasource<>() {
			@Override
			public Collection<ID> loadAllIds() {
				return new ArrayList<>(tCollection.stream().map(idAccessor).toList());
			}

			@Override
			public Collection<T> loadByIds(Collection<ID> strings) throws IOException {
				return new ArrayList<>(tCollection);
			}
		};
	}
}
