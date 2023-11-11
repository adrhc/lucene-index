package ro.go.adrhc.persistence.lucene.core.queries;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.KeywordField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

@RequiredArgsConstructor
public class FieldQueries {
	private final String fieldName;

	public static FieldQueries create(Enum<?> field) {
		return new FieldQueries(field.name());
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
