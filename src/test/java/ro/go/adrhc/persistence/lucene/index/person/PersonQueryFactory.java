package ro.go.adrhc.persistence.lucene.index.person;

import org.apache.lucene.document.KeywordField;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import ro.go.adrhc.persistence.lucene.index.domain.queries.PrefixQueryFactory;
import ro.go.adrhc.persistence.lucene.index.domain.queries.TermQueryFactory;

import static ro.go.adrhc.persistence.lucene.index.IndexTestFactories.ANALYZER;

public class PersonQueryFactory {

	public static Query exactCnp(String cnp) {
		return KeywordField.newExactQuery(PersonFields.cnp.name(), cnp);
	}

	public static TermQuery nameEquals(String name) {
		return TermQueryFactory.create(PersonFields.name, name);
	}

	public static PrefixQuery nameStartsWith(String name) {
		return PrefixQueryFactory.create(PersonFields.name, name);
	}

	public static PrefixQuery oneTokenNameStartsWith(String name) {
		return PrefixQueryFactory.create(PersonFields.oneTokenName, name);
	}

	public static Query nameTextQuery(String nameQuery) throws QueryNodeException {
		StandardQueryParser parser = new StandardQueryParser(ANALYZER);
		return parser.parse(nameQuery, PersonFields.name.name());
	}
}
