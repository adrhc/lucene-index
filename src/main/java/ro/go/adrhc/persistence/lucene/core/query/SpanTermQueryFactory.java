package ro.go.adrhc.persistence.lucene.core.query;

import org.apache.lucene.index.Term;
import org.apache.lucene.queries.spans.SpanTermQuery;

public class SpanTermQueryFactory {
    public static SpanTermQuery create(String fieldName, String text) {
        return new SpanTermQuery(new Term(fieldName, text));
    }
}
