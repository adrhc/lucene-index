package ro.go.adrhc.persistence.lucene.core.query;

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

@RequiredArgsConstructor
public class FieldQueries {
    public static final int MAX_TERM_LENGTH_FOR_EXACT_QUERY = 2;
    private final String fieldName;

    public static FieldQueries create(Enum<?> field) {
        return new FieldQueries(field.name());
    }

    /**
     * fuzzy search "token1 token2 ... tokenN" phrase (i.e. words placed next to each other)
     * <p>
     * tokens are normalized words!
     */
    public SpanNearQuery maxFuzzinessCloseTokens(Collection<String> tokens) {
        SpanQuery[] clausesIn = tokens.stream()
                .map(t -> t.length() <= MAX_TERM_LENGTH_FOR_EXACT_QUERY
                        ? spanTermQuery(t) : maxFuzzinessSpanMultiTermQueryWrapper(t))
                .toArray(SpanQuery[]::new);
        return new SpanNearQuery(clausesIn, 0, true);
    }

    public SpanNearQuery lowFuzzinessCloseTokens(Collection<String> tokens) {
        SpanQuery[] clausesIn = tokens.stream()
                .map(t -> t.length() <= MAX_TERM_LENGTH_FOR_EXACT_QUERY
                        ? spanTermQuery(t) : lowFuzzinessSpanMultiTermQueryWrapper(t))
                .toArray(SpanQuery[]::new);
        return new SpanNearQuery(clausesIn, 0, true);
    }

    public SpanNearQuery closeTokens(Collection<String> tokens) {
        SpanQuery[] clausesIn = tokens.stream()
                .map(this::spanTermQuery)
                .toArray(SpanQuery[]::new);
        return new SpanNearQuery(clausesIn, 0, true);
    }

    public SpanMultiTermQueryWrapper<FuzzyQuery> maxFuzzinessSpanMultiTermQueryWrapper(String value) {
        return new SpanMultiTermQueryWrapper<>(maxFuzziness(value));
    }

    public SpanMultiTermQueryWrapper<FuzzyQuery> lowFuzzinessSpanMultiTermQueryWrapper(String value) {
        return new SpanMultiTermQueryWrapper<>(lowFuzziness(value));
    }

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

    public Query intEquals(int value) {
        return IntPoint.newExactQuery(fieldName, value);
    }

    public Query longEquals(long value) {
        return LongField.newExactQuery(fieldName, value);
    }
}
