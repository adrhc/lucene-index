package ro.go.adrhc.persistence.lucene;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import ro.go.adrhc.persistence.lucene.core.bare.analysis.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.core.bare.query.DefaultFieldAwareQueryParser;
import ro.go.adrhc.persistence.lucene.core.bare.token.TokenizationUtils;
import ro.go.adrhc.persistence.lucene.core.typed.Identifiable;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.bare.token.TokenStreamToStreamConverter;
import ro.go.adrhc.persistence.lucene.operation.IndexOperationsParams;
import ro.go.adrhc.persistence.lucene.operation.IndexOperationsParamsBuilder;
import ro.go.adrhc.persistence.lucene.person.PersonSchema;

import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.core.bare.analysis.AnalyzerFactory.defaultAnalyzer;
import static ro.go.adrhc.persistence.lucene.operation.IndexOperationsParamsBuilder.of;

@Slf4j
public class IndexOperationsParamsGenerator {
	public static final Analyzer ANALYZER = defaultAnalyzer(new TokenizerProperties()).orElseThrow();
	public static final TokenizationUtils TOKENIZATION_UTILS =
		new TokenizationUtils(new TokenStreamToStreamConverter(), ANALYZER);
	public static final DefaultFieldAwareQueryParser NAME_QUERY_PARSER =
		DefaultFieldAwareQueryParser.create(ANALYZER, PersonSchema.name);

	public static <T extends Identifiable<?>, E extends Enum<E> & LuceneFieldSpec<T>>
	IndexOperationsParams<T> createFSIndexParams(Class<T> tClass, Class<E> schemaClass, Path indexPath) {
		return of(tClass, schemaClass, indexPath).build()
			.orElseThrow(() -> new IllegalStateException("Can't create IndexOperationsParams!"));
	}

	public static <T extends Identifiable<?>, E extends Enum<E> & LuceneFieldSpec<T>>
	IndexOperationsParams<T> createRAMIndexParams(Class<T> tClass, Class<E> schemaClass) {
		return IndexOperationsParamsBuilder.of(tClass, schemaClass).build().orElseThrow(
			() -> new IllegalStateException("Can't create IndexOperationsParams!"));
	}
}
