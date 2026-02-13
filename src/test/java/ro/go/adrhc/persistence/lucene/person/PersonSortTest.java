package ro.go.adrhc.persistence.lucene.person;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.adrhc.persistence.lucene.operations.search.ScoreDocAndValues;

import java.io.IOException;
import java.util.List;

import static org.apache.lucene.search.SortField.Type.LONG;
import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.person.PeopleGenerator.generatePeopleList;
import static ro.go.adrhc.persistence.lucene.person.PersonFieldType.cnp;
import static ro.go.adrhc.persistence.lucene.person.PersonFieldType.instantField;

@ExtendWith(MockitoExtension.class)
@Slf4j
class PersonSortTest extends AbstractPersonsIndexTest {
	protected void indexRepositoryReset() throws IOException {
		indexRepository.reset(generatePeopleList(100));
	}

	@Test
	void findIdsSortedByCnp() throws IOException {
		Sort sort = new Sort(new SortedSetSortField(cnp.name(), true));
		List<Long> result = indexRepository.findIds(new MatchAllDocsQuery(), sort);
		assertThat(result).hasSize(100);
		assertThat(result).containsSequence(99L, 98L, 97L);
	}

	@Test
	void findIdsSortedByName() throws IOException {
		Sort sort = new Sort(new SortedSetSortField(cnp.name(), true));
		List<Long> result = indexRepository.findIds(new MatchAllDocsQuery(), sort);
		assertThat(result).hasSize(100);
		assertThat(result).containsSequence(99L, 98L, 97L);
	}

	@Test
	void findPages() throws IOException {
		Sort sort = new Sort(new SortedNumericSortField(instantField.name(), LONG));
		Sort reverseSort = new Sort(new SortedNumericSortField(
			instantField.name(), LONG, true));

		// 1st page
		ScoreDocAndValues<Person> page1 = indexRepository.findMany(
			new MatchAllDocsQuery(), 10, sort);
		assertThat(page1.values()).hasSize(10);
		assertThat(page1.values()).map(Person::id).containsSequence(0L, 1L, 2L);

		// 2nd page
		ScoreDocAndValues<Person> page2 = indexRepository.findManyAfter(
			page1.lastPosition(), new MatchAllDocsQuery(), 10, sort);
		assertThat(page2.values()).hasSize(10);
		assertThat(page2.values()).map(Person::id).containsSequence(10L, 11L, 12L);

		// back to 1st page
		ScoreDocAndValues<Person> page3 = indexRepository.findManyAfter(page2.firstPosition(),
			new MatchAllDocsQuery(), 10, reverseSort).reverse();
		assertThat(page3.values()).hasSize(10);
		assertThat(page3.values()).map(Person::id).containsSequence(0L, 1L, 2L);
	}

	@Test
	void findManySortInstantField() throws IOException {
		Sort sort = new Sort(new SortedNumericSortField(instantField.name(), LONG));
		ScoreDocAndValues<Person> result = indexRepository.findMany(
			new MatchAllDocsQuery(), 10, sort);
		assertThat(result.values()).hasSize(10);
		assertThat(result.values()).map(Person::id).containsSequence(0L, 1L, 2L);
	}

	@Test
	void findManySortCnp() throws IOException {
		Sort sort = new Sort(new SortedSetSortField(cnp.name(), false));
		ScoreDocAndValues<Person> result = indexRepository.findMany(
			new MatchAllDocsQuery(), 10, sort);
		assertThat(result.values()).hasSize(10);
		assertThat(result.values()).map(Person::id).containsSequence(0L, 1L, 10L);
	}
}
