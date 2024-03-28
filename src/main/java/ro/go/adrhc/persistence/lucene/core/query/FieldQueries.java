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

import static org.apache.lucene.util.automaton.LevenshteinAutomata.MAXIMUM_SUPPORTED_DISTANCE;

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
    public SpanNearQuery closeFuzzyTokens(Collection<String> tokens) {
        SpanQuery[] clausesIn = tokens.stream()
                .map(t -> t.length() <= 2 ? spanTermQuery(t)
                        : new SpanMultiTermQueryWrapper<>(fuzzy(t)))
                .toArray(SpanQuery[]::new);
        return new SpanNearQuery(clausesIn, 0, true);
    }

    public FuzzyQuery fuzzy(String value) {
        return fuzzy(MAXIMUM_SUPPORTED_DISTANCE, value);
    }

    public SpanTermQuery spanTermQuery(String value) {
        return SpanTermQueryFactory.create(fieldName, value);
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
