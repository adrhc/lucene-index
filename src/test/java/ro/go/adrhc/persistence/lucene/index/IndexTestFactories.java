package ro.go.adrhc.persistence.lucene.index;

import org.apache.lucene.analysis.Analyzer;
import ro.go.adrhc.persistence.lucene.index.core.analysis.AnalyzerFactory;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizationUtils;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.index.core.write.FSIndexWriterHolder;
import ro.go.adrhc.persistence.lucene.index.domain.queries.DefaultAwareQueryParser;
import ro.go.adrhc.persistence.lucene.index.person.PersonFieldType;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexSpec;
import ro.go.adrhc.persistence.lucene.typedindex.domain.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static com.rainerhahnekamp.sneakythrow.Sneaky.sneak;
import static ro.go.adrhc.persistence.lucene.index.core.tokenizer.PatternsAndReplacement.caseInsensitive;

public class IndexTestFactories {
	public static final Analyzer ANALYZER = sneak(IndexTestFactories::createAnalyzer);
	public static final TokenizationUtils TOKENIZATION_UTILS = new TokenizationUtils(ANALYZER);
	public static final DefaultAwareQueryParser NAME_QUERY_PARSER =
			DefaultAwareQueryParser.create(ANALYZER, PersonFieldType.name);

	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>>
	TypedIndexSpec<ID, T, E> createTypedIndexSpec(
			Class<T> tClass, Class<E> typedFieldEnumClass, Path indexPath) throws IOException {
		FSIndexWriterHolder fsWriterHolder = FSIndexWriterHolder.create(ANALYZER, indexPath);
		return TypedIndexSpec.create(tClass, typedFieldEnumClass, fsWriterHolder);
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
