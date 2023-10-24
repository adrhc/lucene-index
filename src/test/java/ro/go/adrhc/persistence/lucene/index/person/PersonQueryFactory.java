package ro.go.adrhc.persistence.lucene.index.person;

import org.apache.lucene.search.TermQuery;
import ro.go.adrhc.persistence.lucene.index.domain.queries.TermQueryFactory;

public class PersonQueryFactory {
	public static TermQuery nameEquals(String name) {
		return TermQueryFactory.create(PersonFields.name, name);
	}
}
