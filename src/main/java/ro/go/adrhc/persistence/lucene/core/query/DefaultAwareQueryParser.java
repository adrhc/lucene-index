package ro.go.adrhc.persistence.lucene.core.query;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.search.Query;

@RequiredArgsConstructor
public class DefaultAwareQueryParser {
	private final QueryParser queryParser;
	private final String defaultField;

	public static DefaultAwareQueryParser create(Analyzer analyzer, Enum<?> defaultField) {
		return new DefaultAwareQueryParser(QueryParser.create(analyzer), defaultField.name());
	}

	public Query parse(String query) throws QueryNodeException {
		return queryParser.parse(defaultField, query);
	}
}
