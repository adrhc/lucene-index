package ro.go.adrhc.persistence.lucene.person;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.person.PeopleGenerator.PEOPLE;
import static ro.go.adrhc.persistence.lucene.person.PeopleGenerator.generateGirl;
import static ro.go.adrhc.persistence.lucene.person.PersonQueryFactory.ALIAS_KEYWORD_QUERIES;

@Slf4j
class PersonCrudTest extends AbstractPersonRAMIndexTest {
	@Override
	protected void indexReset() throws IOException {
		index.reset(PEOPLE);
	}

	@Test
	void crudTest() throws IOException {
		int count = index.count(ALIAS_KEYWORD_QUERIES.startsWith("alias_Keyword"));
		log.info("\ncount: {}", count);
		assertThat(count).isEqualTo(PEOPLE.size());

		index.addOne(generateGirl(4));
		assertThat(index.findById(4L)).isPresent();

		index.removeById(4L);
		assertThat(index.findById(4L)).isEmpty();
	}

	@Test
	void nullInstantField() throws IOException {
		index.addOne(PeopleGenerator.generateGirl(PEOPLE.size() + 1, null));
		assertThat(index.findById(1L + PEOPLE.size())).isPresent();
	}

	@Test
	void updateTest() throws IOException {
		Optional<Person> optionalPerson = index.findById(1L);
		assertThat(optionalPerson).isPresent();

		String newStoredOnlyField = Instant.now().toString();
		Person person = optionalPerson.get().storedOnlyField(newStoredOnlyField);
		index.upsert(person);

		optionalPerson = index.findById(person.getId());
		assertThat(optionalPerson).isPresent();
		assertThat(optionalPerson.get().storedOnlyField()).isEqualTo(newStoredOnlyField);
	}
}
