package ro.go.adrhc.persistence.lucene.index;

import org.apache.lucene.analysis.Analyzer;
import ro.go.adrhc.persistence.lucene.index.core.analysis.AnalyzerFactory;
import ro.go.adrhc.persistence.lucene.index.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizationUtils;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.index.domain.queries.FieldQueries;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexFactories;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedFieldEnum;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.rainerhahnekamp.sneakythrow.Sneaky.sneak;
import static ro.go.adrhc.persistence.lucene.index.core.tokenizer.PatternsAndReplacement.caseInsensitive;

public class IndexTestFactories {
	public static final int MAX_RESULTS_PER_SEARCHED_ITEM = Integer.MAX_VALUE;
	public static final Analyzer ANALYZER = sneak(IndexTestFactories::createAnalyzer);
	public static final TokenizationUtils TOKENIZATION_UTILS = new TokenizationUtils(ANALYZER);

	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedFieldEnum<T>>
	TypedIndexFactories<ID, T, E> createTypedIndexFactories(Class<T> tClass, Class<E> typedFieldEnumClass) {
		return createTypedIndexFactories(MAX_RESULTS_PER_SEARCHED_ITEM, tClass, typedFieldEnumClass);
	}

	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedFieldEnum<T>>
	TypedIndexFactories<ID, T, E> createTypedIndexFactories(
			int maxResultsPerSearchedItem, Class<T> tClass, Class<E> typedFieldEnumClass) {
		return new TypedIndexFactories<>(maxResultsPerSearchedItem, ANALYZER, tClass, typedFieldEnumClass);
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
