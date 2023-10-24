package ro.go.adrhc.persistence.lucene.index.domain.queries;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.PrefixQuery;

public class PrefixQueryFactory {
	public static PrefixQuery create(Enum<?> fieldType, String text) {
		return create(fieldType.name(), text);
	}

	public static PrefixQuery create(String fieldName, String text) {
		return new PrefixQuery(new Term(fieldName, text));
	}
}
