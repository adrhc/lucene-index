package ro.go.adrhc.persistence.lucene.core.bare.query;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.KeywordField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.queries.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.queries.spans.SpanNearQuery;
import org.apache.lucene.queries.spans.SpanQuery;
import org.apache.lucene.queries.spans.SpanTermQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import java.util.Collection;

import static ro.go.adrhc.persistence.lucene.core.bare.query.FuzzyQueryFactory.maxFuzzinessIsSupported;
import static ro.go.adrhc.persistence.lucene.core.bare.query.TermQueryFactory.shouldUseTermQuery;

@RequiredArgsConstructor
public class FieldQueries {
	private final String fieldName;

	public static FieldQueries create(Enum<?> field) {
		return new FieldQueries(field.name());
	}

	/**
	 * fuzzy search "token1 token2 ... tokenN" phrase (i.e. words placed next to each other)
	 * <p>
	 * tokens are normalized words!
	 */
	public SpanNearQuery maxFuzzinessNearTokens(Collection<String> tokens) {
		SpanQuery[] clausesIn = tokens.stream()
				.map(this::toMaxFuzzinessSpanQuery)
				.toArray(SpanQuery[]::new);
		return new SpanNearQuery(clausesIn, 0, true);
	}

	public SpanNearQuery lowFuzzinessNearTokens(Collection<String> tokens) {
		SpanQuery[] clausesIn = tokens.stream()
				.map(this::toLowFuzzinessSpanQuery)
				.toArray(SpanQuery[]::new);
		return new SpanNearQuery(clausesIn, 0, true);
	}

	public SpanNearQuery nearTokens(Collection<String> tokens) {
		SpanQuery[] clausesIn = tokens.stream()
				.map(this::spanTermQuery)
				.toArray(SpanQuery[]::new);
		return new SpanNearQuery(clausesIn, 0, true);
	}

	/**
	 * Useful with SpanNearQuery.
	 */
	public SpanMultiTermQueryWrapper<FuzzyQuery> maxFuzzinessSpanMultiTermQueryWrapper(
			String value) {
		return new SpanMultiTermQueryWrapper<>(maxFuzziness(value));
	}

	/**
	 * Useful with SpanNearQuery.
	 */
	public SpanMultiTermQueryWrapper<FuzzyQuery> lowFuzzinessSpanMultiTermQueryWrapper(
			String value) {
		return new SpanMultiTermQueryWrapper<>(lowFuzziness(value));
	}

	/**
	 * Useful with SpanNearQuery.
	 */
	public SpanTermQuery spanTermQuery(String value) {
		return SpanTermQueryFactory.create(fieldName, value);
	}

	public FuzzyQuery maxFuzziness(String value) {
		return FuzzyQueryFactory.maxFuzziness(fieldName, value);
	}

	public FuzzyQuery lowFuzziness(String value) {
		return FuzzyQueryFactory.lowFuzziness(fieldName, value);
	}

	public FuzzyQuery fuzzy(int levenshteinDistance, String value) {
		return FuzzyQueryFactory.create(levenshteinDistance, fieldName, value);
	}

	public PrefixQuery startsWith(String prefix) {
		return PrefixQueryFactory.create(fieldName, prefix);
	}

	public TermQuery tokenEquals(String value) {
		return TermQueryFactory.create(fieldName, value);
	}

	public Query keywordEquals(String value) {
		return KeywordField.newExactQuery(fieldName, value);
	}

	public Query booleanEquals(boolean value) {
		return IntPoint.newExactQuery(fieldName, value ? 1 : 0);
	}

	public Query intEquals(int value) {
		return IntPoint.newExactQuery(fieldName, value);
	}

	public Query longEquals(long value) {
		return LongField.newExactQuery(fieldName, value);
	}

	private SpanQuery toMaxFuzzinessSpanQuery(String token) {
		if (maxFuzzinessIsSupported(token)) {
			return maxFuzzinessSpanMultiTermQueryWrapper(token);
		} else {
			return toLowFuzzinessSpanQuery(token);
		}
	}

	private SpanQuery toLowFuzzinessSpanQuery(String token) {
		return shouldUseTermQuery(token) ? spanTermQuery(token)
				: lowFuzzinessSpanMultiTermQueryWrapper(token);
	}
}
