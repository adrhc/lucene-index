package ro.go.adrhc.persistence.lucene.index.domain.queries;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.Query;

@RequiredArgsConstructor
public class QueryParser {
	private final StandardQueryParser standardQueryParser;

	public static QueryParser create(Analyzer analyzer) {
		return new QueryParser(new StandardQueryParser(analyzer));
	}

	public Query parse(Enum<?> defaultField, String query) throws QueryNodeException {
		return standardQueryParser.parse(query, defaultField.name());
	}
}
