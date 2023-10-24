package ro.go.adrhc.persistence.lucene.index.core.analysis;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.function.TriFunction;
import org.apache.lucene.analysis.charfilter.MappingCharFilter;
import org.apache.lucene.analysis.charfilter.NormalizeCharMap;
import org.apache.lucene.analysis.pattern.PatternReplaceCharFilter;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.PatternsAndReplacement;
import ro.go.adrhc.util.value.MutableValue;

import java.io.Reader;
import java.util.List;
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
	public static Reader mappingCharFilter(List<String[]> pairs, Reader reader) {
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
	public static Reader patternReplaceCharFilter(PatternsAndReplacement patternsAndReplacement, Reader reader) {
		return patternReplaceCharFilter(patternsAndReplacement.replacement(), patternsAndReplacement.patterns(), reader);
	}

	/**
	 * Remove from source text the parts matching patterns.
	 */
	public static Reader patternReplaceCharFilter(String replacement, Iterable<String> patterns, Reader reader) {
		return replaceCharFilter(CharFilterFactory::patternReplaceCharFilter, replacement, patterns, reader);
	}

	/**
	 * Remove from source text the parts matching patterns.
	 */
	public static Reader patternRemoveCharFilter(Iterable<String> patterns, Reader reader) {
		return replaceCharFilter(CharFilterFactory::patternReplaceCharFilter, "", patterns, reader);
	}

	/**
	 * Remove from source text the parts equal to texts.
	 */
	public static Reader textRemoveCharFilter(Iterable<String> texts, Reader reader) {
		return replaceCharFilter(CharFilterFactory::textReplaceCharFilter, "", texts, reader);
	}

	private static Reader replaceCharFilter(
			TriFunction<Reader, String, String, PatternReplaceCharFilter> patternReplaceCharFilterFactory,
			String replacement, Iterable<String> patterns, Reader reader) {
		MutableValue<Reader> readerHolder = new MutableValue<>(reader);

		patterns.forEach(text -> readerHolder.setValue(
				patternReplaceCharFilterFactory.apply(readerHolder.getValue(), replacement, text)
		));

		return readerHolder.getValue();
	}

	private static PatternReplaceCharFilter textReplaceCharFilter(Reader reader, String replacement, String text) {
		return new PatternReplaceCharFilter(
				Pattern.compile(text, CASE_INSENSITIVE | Pattern.LITERAL), replacement, reader);
	}

	private static PatternReplaceCharFilter patternReplaceCharFilter(Reader reader, String replacement, String pattern) {
		return new PatternReplaceCharFilter(
				Pattern.compile(pattern, CASE_INSENSITIVE), replacement, reader);
	}
}
