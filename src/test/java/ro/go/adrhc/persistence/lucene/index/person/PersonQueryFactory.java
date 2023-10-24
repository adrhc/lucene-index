package ro.go.adrhc.persistence.lucene.index.person;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import ro.go.adrhc.persistence.lucene.index.IndexTestFactories;
import ro.go.adrhc.persistence.lucene.index.domain.queries.PrefixQueryFactory;
import ro.go.adrhc.persistence.lucene.index.domain.queries.TermQueryFactory;

import static com.rainerhahnekamp.sneakythrow.Sneaky.sneak;

public class PersonQueryFactory {
	private static final Analyzer ANALYZER = sneak(IndexTestFactories::createAnalyzer);

	public static TermQuery nameEquals(String name) {
		return TermQueryFactory.create(PersonFields.name, name);
	}

	public static PrefixQuery nameStartsWith(String name) {
		return PrefixQueryFactory.create(PersonFields.name, name);
	}

	public static Query nameTextQuery(String nameQuery) throws QueryNodeException {
		StandardQueryParser parser = new StandardQueryParser(ANALYZER);
		return parser.parse(nameQuery, PersonFields.name.name());
	}
}
