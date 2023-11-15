package ro.go.adrhc.persistence.lucene.index;

import org.apache.lucene.analysis.Analyzer;
import ro.go.adrhc.persistence.lucene.core.query.DefaultAwareQueryParser;
import ro.go.adrhc.persistence.lucene.core.token.TokenizationUtils;
import ro.go.adrhc.persistence.lucene.core.token.props.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.index.person.PersonFieldType;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexContext;
import ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexFactoriesParamsFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static ro.go.adrhc.persistence.lucene.core.token.props.PatternsAndReplacement.caseInsensitive;
import static ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexFactoriesParamsDefaults.defaultAnalyzer;

public class TestIndexContext {
	public static final Analyzer ANALYZER = defaultAnalyzer(createTokenizerProperties());
	public static final TokenizationUtils TOKENIZATION_UTILS = new TokenizationUtils(ANALYZER);
	public static final DefaultAwareQueryParser NAME_QUERY_PARSER =
			DefaultAwareQueryParser.create(ANALYZER, PersonFieldType.name);

	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>>
	TypedIndexContext<ID, T> createTypedIndexSpec(Class<T> tClass,
			Class<E> typedFieldEnumClass, Path indexPath) throws IOException {
		return TypedIndexFactoriesParamsFactory.create(tClass,
				typedFieldEnumClass, createTokenizerProperties(), indexPath);
	}

	private static TokenizerProperties createTokenizerProperties() {
		return new TokenizerProperties(2,
				List.of("Fixed Pattern To Remove"),
				List.of("\\(\\s*Regex\\s*Pattern\\s*To\\s*Remove\\)"),
				Map.of("_", " "),
				caseInsensitive("$1", "([^\\s]*)\\.jpe?g"));
	}
}
