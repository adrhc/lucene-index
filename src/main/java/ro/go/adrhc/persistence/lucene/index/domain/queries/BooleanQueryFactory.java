package ro.go.adrhc.persistence.lucene.index.domain.queries;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

import java.util.stream.Stream;

public class BooleanQueryFactory {
	public static BooleanQuery shouldSatisfy(Stream<? extends Query> queries) {
		BooleanQueryBuilder builder = new BooleanQueryBuilder();
		queries.forEach(builder::shouldSatisfy);
		return builder.build();
	}
}
