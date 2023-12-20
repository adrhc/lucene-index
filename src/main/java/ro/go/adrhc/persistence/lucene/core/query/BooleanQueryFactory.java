package ro.go.adrhc.persistence.lucene.core.query;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

import java.util.Collection;
import java.util.stream.Stream;

public class BooleanQueryFactory {
    public static BooleanQuery mustSatisfy(Collection<? extends Query> queries) {
        BooleanQueryBuilder builder = new BooleanQueryBuilder();
        queries.forEach(builder::mustSatisfy);
        return builder.build();
    }

    public static BooleanQuery shouldSatisfy(Collection<? extends Query> queries) {
        BooleanQueryBuilder builder = new BooleanQueryBuilder();
        queries.forEach(builder::shouldSatisfy);
        return builder.build();
    }

    public static BooleanQuery shouldSatisfy(Stream<? extends Query> queries) {
        BooleanQueryBuilder builder = new BooleanQueryBuilder();
        queries.forEach(builder::shouldSatisfy);
        return builder.build();
    }
}
