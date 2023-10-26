package ro.go.adrhc.persistence.lucene.index.domain.queries;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.KeywordField;
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

	public TermQuery tokenEquals(String name) {
		return TermQueryFactory.create(fieldName, name);
	}

	public PrefixQuery tokenStartsWith(String name) {
		return PrefixQueryFactory.create(fieldName, name);
	}

	public Query wordEquals(String cnp) {
		return KeywordField.newExactQuery(fieldName, cnp);
	}

	public PrefixQuery wordStartsWith(String prefix) {
		return PrefixQueryFactory.create(fieldName, prefix);
	}

	public Query parse(String query) throws QueryNodeException {
		return standardQueryParser.parse(query, fieldName);
	}
}
