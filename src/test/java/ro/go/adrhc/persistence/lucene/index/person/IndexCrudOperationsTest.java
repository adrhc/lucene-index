package ro.go.adrhc.persistence.lucene.index.person;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;
import ro.go.adrhc.util.StopWatchUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.PEOPLE;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.generatePerson;
import static ro.go.adrhc.persistence.lucene.index.person.PersonIndexFactories.ALIAS_KEYWORD_QUERIES;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class IndexCrudOperationsTest extends AbstractPersonsIndexTest {
	@Test
	void crudTest() throws IOException {
		int count = count(ALIAS_KEYWORD_QUERIES.startsWith("alias_Keyword"));
		log.info("\ncount: {}", count);
		assertThat(count).isEqualTo(PEOPLE.size());

		TypedIndexUpdateService<Person> indexUpdateService = createUpdateService();
		indexUpdateService.add(generatePerson(4));

		TypedSearchByIdService<Long, Person> searchByIdService = createSearchByIdService();
		assertThat(searchByIdService.findById(4L)).isPresent();

		TypedIndexRemoveService<Long> indexRemoveService = createIndexRemoveService();
		indexRemoveService.removeById(4L);
		assertThat(searchByIdService.findById(4L)).isEmpty();
	}

	@Test
	void updateTest() throws IOException {
		StopWatch stopWatch = StopWatchUtils.start();

		TypedSearchByIdService<Long, Person> searchByIdService = createSearchByIdService();
		Optional<Person> optionalPerson = searchByIdService.findById(1L);
		assertThat(optionalPerson).isPresent();

		String newStoredOnlyField = Instant.now().toString();
		Person person = optionalPerson.get().storedOnlyField(newStoredOnlyField);
		createUpdateService().update(person);

		optionalPerson = searchByIdService.findById(1L);
		assertThat(optionalPerson).isPresent();
		assertThat(optionalPerson.get().storedOnlyField()).isEqualTo(newStoredOnlyField);
		stopWatch.stop();
	}
}
