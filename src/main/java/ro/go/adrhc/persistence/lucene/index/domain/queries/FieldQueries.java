package ro.go.adrhc.persistence.lucene.index.domain.queries;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.KeywordField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

@RequiredArgsConstructor
public class FieldQueries {
	private final StandardQueryParser standardQueryParser;
	private final String fieldName;

	public static FieldQueries create(Analyzer analyzer, Enum<?> field) {
		return new FieldQueries(new StandardQueryParser(analyzer), field.name());
	}

	public TermQuery tokenEquals(String value) {
		return TermQueryFactory.create(fieldName, value);
	}

	public PrefixQuery tokenStartsWith(String value) {
		return PrefixQueryFactory.create(fieldName, value);
	}

	public Query wordEquals(String value) {
		return KeywordField.newExactQuery(fieldName, value);
	}

	public Query intEquals(int value) {
		return IntPoint.newExactQuery(fieldName, value);
	}

	public Query longEquals(long value) {
		return LongField.newExactQuery(fieldName, value);
	}

	public PrefixQuery wordStartsWith(String prefix) {
		return PrefixQueryFactory.create(fieldName, prefix);
	}

	public Query parse(String query) throws QueryNodeException {
		return standardQueryParser.parse(query, fieldName);
	}
}
