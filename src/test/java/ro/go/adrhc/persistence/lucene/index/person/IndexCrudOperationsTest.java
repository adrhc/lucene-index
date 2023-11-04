package ro.go.adrhc.persistence.lucene.index.person;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.PEOPLE;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.generatePerson;
import static ro.go.adrhc.persistence.lucene.index.person.PersonIndexFactories.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class IndexCrudOperationsTest extends AbstractPersonsIndexTest {
	@Test
	void crudTest() throws IOException {
		int count = count(ALIAS_KEYWORD_QUERIES.startsWith("alias_Keyword"));
		log.info("\ncount: {}", count);
		assertThat(count).isEqualTo(PEOPLE.size());

		TypedIndexUpdateService<Integer, Person> indexUpdateService = createUpdateService();
		indexUpdateService.add(generatePerson(4));

		TypedSearchByIdService<Integer, Person> searchByIdService = createSearchByIdService();
		Optional<Person> optionalPerson = searchByIdService.findById(4);
		assertThat(optionalPerson).isPresent();
		assertThat(count(ALIAS_KEYWORD_QUERIES.startsWith("alias_Keyword"))).isEqualTo(4);
		assertThat(count(ALIAS_WORD_QUERIES.startsWith("alias word"))).isEqualTo(4);
		assertThat(count(ALIAS_PHRASE_QUERIES.startsWith("phrase"))).isEqualTo(4);
	}
}
