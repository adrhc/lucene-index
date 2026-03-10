package ro.go.adrhc.persistence.lucene;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.bare.analysis.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.core.bare.query.DefaultFieldAwareQueryParser;
import ro.go.adrhc.persistence.lucene.core.bare.token.TokenizationUtils;
import ro.go.adrhc.persistence.lucene.core.bare.write.LuceneIndexWriterFactory;
import ro.go.adrhc.persistence.lucene.core.typed.Identifiable;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.lib.TokenStreamToStreamConverter;
import ro.go.adrhc.persistence.lucene.operations.params.IndexServicesParamsFactory;
import ro.go.adrhc.persistence.lucene.operations.params.IndexServicesParamsFactoryBuilder;
import ro.go.adrhc.persistence.lucene.person.PersonSchema;

import java.io.IOException;
import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.core.bare.analysis.AnalyzerFactory.defaultAnalyzer;
import static ro.go.adrhc.persistence.lucene.operations.params.IndexServicesParamsFactoryBuilder.of;
import static ro.go.adrhc.util.fn.FunctionUtils.failToEmpty;

@Slf4j
public class TypedIndexParamsTestFactory {
	public static final Analyzer ANALYZER = defaultAnalyzer(new TokenizerProperties()).orElseThrow();
	public static final TokenizationUtils TOKENIZATION_UTILS =
		new TokenizationUtils(new TokenStreamToStreamConverter(), ANALYZER);
	public static final DefaultFieldAwareQueryParser NAME_QUERY_PARSER =
		DefaultFieldAwareQueryParser.create(ANALYZER, PersonSchema.name);

	public static <T extends Identifiable<?>, E extends Enum<E> & LuceneFieldSpec<T>>
	IndexServicesParamsFactory<T>
	createFSTypedIndexSpec(Class<T> tClass, Class<E> schemaClass, Path indexPath) {
		return of(tClass, schemaClass, indexPath).build()
			.orElseThrow(() -> new IllegalStateException("Can't create IndexServicesParamsFactory!"));
	}

	public static <T extends Identifiable<?>, E extends Enum<E> & LuceneFieldSpec<T>>
	IndexServicesParamsFactory<T> createRAMTypedIndexSpec(Class<T> tClass, Class<E> schemaClass) {
		IndexServicesParamsFactoryBuilder<T, E> builder =
			IndexServicesParamsFactoryBuilder.of(tClass, schemaClass, Path.of("nowhere"));
		IndexWriter writer = null;
		try {
			writer = LuceneIndexWriterFactory.ramWriter();
			writer.commit(); // creates the index
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		builder.indexWriterFactory(analyzer ->
			failToEmpty(LuceneIndexWriterFactory::ramWriter, analyzer));
		return builder.build().orElseThrow(() ->
			new IllegalStateException("Can't create IndexServicesParamsFactory!"));
	}
}
