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
	public static Query cnpEquals(String cnp) {
		return KeywordField.newExactQuery(PersonFieldType.cnp.name(), cnp);
	}

	public static TermQuery nameTokenEquals(String name) {
		return TermQueryFactory.create(PersonFieldType.name, name);
	}

	public static PrefixQuery nameTokenStartsWith(String name) {
		return PrefixQueryFactory.create(PersonFieldType.name, name);
	}

	public static PrefixQuery nameStartsWith(String name) {
		return PrefixQueryFactory.create(PersonFieldType.nameAsWord, name);
	}

	public static Query parse(String query) throws QueryNodeException {
		StandardQueryParser parser = new StandardQueryParser(ANALYZER);
		return parser.parse(query, PersonFieldType.id.name());
	}
}
