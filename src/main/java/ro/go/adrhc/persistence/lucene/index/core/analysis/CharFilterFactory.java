package ro.go.adrhc.persistence.lucene.index.core.analysis;

import lombok.experimental.UtilityClass;
import org.apache.lucene.analysis.charfilter.MappingCharFilter;
import org.apache.lucene.analysis.charfilter.NormalizeCharMap;
import org.apache.lucene.analysis.pattern.PatternReplaceCharFilter;
import ro.go.adrhc.util.value.MutableValue;

import java.io.Reader;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * CharFilters may be chained to perform multiple pre-tokenization modifications.
 */
@UtilityClass
public class CharFilterFactory {
	/**
	 * Replace pairs[*][0] with pairs[*][1].
	 */
	public static Reader mappingCharFilter(Reader reader, List<String[]> pairs) {
		if (pairs.isEmpty()) {
			return reader;
		}
		NormalizeCharMap.Builder normalizeCharMapBuilder = new NormalizeCharMap.Builder();
		pairs.forEach(it -> normalizeCharMapBuilder.add(it[0], it[1]));
		return new MappingCharFilter(normalizeCharMapBuilder.build(), reader);
	}

	/**
	 * Remove from source text the parts matching patterns.
	 */
	public static Reader patternRemoveCharFilter(Reader reader, Iterable<String> patterns) {
		return patternReplaceCharFilter(reader, patterns, CharFilterFactory::patternRemoveCharFilter);
	}

	/**
	 * Remove from source text the parts equal to texts.
	 */
	public static Reader textRemoveCharFilter(Reader reader, Iterable<String> texts) {
		return patternReplaceCharFilter(reader, texts, CharFilterFactory::textRemoveCharFilter);
	}

	private static Reader patternReplaceCharFilter(Reader reader, Iterable<String> patterns,
			BiFunction<Reader, String, PatternReplaceCharFilter> patternReplaceCharFilterFactory) {
		MutableValue<Reader> readerHolder = new MutableValue<>(reader);

		patterns.forEach(text -> readerHolder.setValue(
				patternReplaceCharFilterFactory.apply(readerHolder.getValue(), text)
		));

		return readerHolder.getValue();
	}

	private static PatternReplaceCharFilter patternRemoveCharFilter(Reader reader, String pattern) {
		return new PatternReplaceCharFilter(
				Pattern.compile(pattern, CASE_INSENSITIVE), "", reader);
	}

	private static PatternReplaceCharFilter textRemoveCharFilter(Reader reader, String text) {
		return new PatternReplaceCharFilter(
				Pattern.compile(text, CASE_INSENSITIVE | Pattern.LITERAL), "", reader);
	}
}
