package ro.go.adrhc.persistence.lucene.core.query;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

public class BooleanQueryBuilder {
    private final BooleanQuery.Builder builder = new BooleanQuery.Builder();

    public void setMinimumNumberShouldMatch(int min) {
        builder.setMinimumNumberShouldMatch(min);
    }

    public void mustSatisfy(Query query) {
        builder.add(query, BooleanClause.Occur.MUST);
    }

    public void shouldSatisfy(Query query) {
        builder.add(query, BooleanClause.Occur.SHOULD);
    }

    public BooleanQuery build() {
        return builder.build();
    }
}
