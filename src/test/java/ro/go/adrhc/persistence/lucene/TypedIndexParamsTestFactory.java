package ro.go.adrhc.persistence.lucene;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import ro.go.adrhc.persistence.lucene.core.bare.analysis.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.core.bare.query.DefaultFieldAwareQueryParser;
import ro.go.adrhc.persistence.lucene.core.bare.token.TokenizationUtils;
import ro.go.adrhc.persistence.lucene.core.typed.Identifiable;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.lib.TokenStreamToStreamConverter;
import ro.go.adrhc.persistence.lucene.operations.params.IndexServicesParamsFactory;
import ro.go.adrhc.persistence.lucene.person.PersonFieldType;

import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.core.bare.analysis.AnalyzerFactory.defaultAnalyzer;
import static ro.go.adrhc.persistence.lucene.operations.params.IndexServicesParamsFactoryBuilder.of;

@Slf4j
public class TypedIndexParamsTestFactory {
	public static final Analyzer ANALYZER = defaultAnalyzer(new TokenizerProperties()).orElseThrow();
	public static final TokenizationUtils TOKENIZATION_UTILS =
		new TokenizationUtils(new TokenStreamToStreamConverter(), ANALYZER);
	public static final DefaultFieldAwareQueryParser NAME_QUERY_PARSER =
		DefaultFieldAwareQueryParser.create(ANALYZER, PersonFieldType.name);

	public static <T extends Identifiable<?>, E extends Enum<E> & LuceneFieldSpec<T>>
	IndexServicesParamsFactory<T> createTypedIndexSpec(
		Class<T> tClass, Class<E> typedFieldEnumClass, Path indexPath) {
		return of(tClass, typedFieldEnumClass, indexPath)
			.build()
			.orElseThrow(() -> new RuntimeException("Can't create IndexServicesParamsFactory!"));
	}
}
