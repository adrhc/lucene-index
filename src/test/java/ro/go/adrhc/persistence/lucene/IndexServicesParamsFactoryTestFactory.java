package ro.go.adrhc.persistence.lucene;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import ro.go.adrhc.persistence.lucene.core.bare.analysis.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.core.bare.query.DefaultFieldAwareQueryParser;
import ro.go.adrhc.persistence.lucene.core.bare.token.TokenizationUtils;
import ro.go.adrhc.persistence.lucene.core.bare.write.DocIndexWriterFactory;
import ro.go.adrhc.persistence.lucene.core.typed.Identifiable;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.lib.TokenStreamToStreamConverter;
import ro.go.adrhc.persistence.lucene.operations.IndexOperationsParams;
import ro.go.adrhc.persistence.lucene.operations.IndexOperationsParamsBuilder;
import ro.go.adrhc.persistence.lucene.person.PersonSchema;

import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.core.bare.analysis.AnalyzerFactory.defaultAnalyzer;
import static ro.go.adrhc.persistence.lucene.operations.IndexOperationsParamsBuilder.of;
import static ro.go.adrhc.util.fn.FunctionUtils.failToEmpty;

@Slf4j
public class IndexServicesParamsFactoryTestFactory {
	public static final Analyzer ANALYZER = defaultAnalyzer(new TokenizerProperties()).orElseThrow();
	public static final TokenizationUtils TOKENIZATION_UTILS =
		new TokenizationUtils(new TokenStreamToStreamConverter(), ANALYZER);
	public static final DefaultFieldAwareQueryParser NAME_QUERY_PARSER =
		DefaultFieldAwareQueryParser.create(ANALYZER, PersonSchema.name);

	public static <T extends Identifiable<?>, E extends Enum<E> & LuceneFieldSpec<T>>
	IndexOperationsParams<T>
	createFSTypedIndexSpec(Class<T> tClass, Class<E> schemaClass, Path indexPath) {
		return of(tClass, schemaClass, indexPath).build()
			.orElseThrow(() -> new IllegalStateException("Can't create IndexOperationsParams!"));
	}

	public static <T extends Identifiable<?>, E extends Enum<E> & LuceneFieldSpec<T>>
	IndexOperationsParams<T> createRAMTypedIndexSpec(Class<T> tClass, Class<E> schemaClass) {
		IndexOperationsParamsBuilder<T, E> builder =
			IndexOperationsParamsBuilder.of(tClass, schemaClass, Path.of("nowhere"));
		builder.indexWriterFactory(analyzer ->
			failToEmpty(DocIndexWriterFactory::ramWriter, analyzer));
		return builder.build().orElseThrow(() ->
			new IllegalStateException("Can't create IndexOperationsParams!"));
	}
}
