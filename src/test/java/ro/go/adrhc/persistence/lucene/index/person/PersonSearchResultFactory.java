package ro.go.adrhc.persistence.lucene.index.person;

import ro.go.adrhc.persistence.lucene.index.core.read.ScoreAndDocument;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchResultFactory;

public class PersonSearchResultFactory<S> implements IndexSearchResultFactory<S, PersonSearchResult> {
	@Override
	public PersonSearchResult create(S searchedAudio, ScoreAndDocument scoreAndDocument) {
		return new PersonSearchResult(toPerson(scoreAndDocument), scoreAndDocument.getField(PersonFields.oneTokenName));
	}

	private static Person toPerson(ScoreAndDocument sad) {
		return new Person(sad.getField(PersonFields.id),
				sad.getField(PersonFields.cnp), sad.getField(PersonFields.name));
	}
}
