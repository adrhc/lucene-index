package ro.go.adrhc.persistence.lucene.index.person;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortedNumericSortField;
import org.apache.lucene.search.SortedSetSortField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.adrhc.persistence.lucene.typedindex.search.SortedValues;

import java.io.IOException;

import static org.apache.lucene.search.SortField.Type.LONG;
import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.generatePeopleList;
import static ro.go.adrhc.persistence.lucene.index.person.PersonFieldType.cnp;
import static ro.go.adrhc.persistence.lucene.index.person.PersonFieldType.instantField;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class PersonSortTest extends AbstractPersonsIndexTest {
	@Test
	void findPages() throws IOException {
		Sort sort = new Sort(new SortedNumericSortField(instantField.name(), LONG));
		Sort reverseSort = new Sort(new SortedNumericSortField(
				instantField.name(), LONG, true));

		// 1st page
		SortedValues<Person> page1 = indexRepository.findMany(
				new MatchAllDocsQuery(), 10, sort);
		assertThat(page1.values()).hasSize(10);
		assertThat(page1.values()).map(Person::id).containsSequence(0L, 1L, 2L);

		// 2nd page
		SortedValues<Person> page2 = indexRepository.findManyAfter(
				page1.lastPosition(), new MatchAllDocsQuery(), 10, sort);
		assertThat(page2.values()).hasSize(10);
		assertThat(page2.values()).map(Person::id).containsSequence(10L, 11L, 12L);

		// back to 1st page
		SortedValues<Person> page3 = indexRepository.findManyAfter(page2.firstPosition(),
				new MatchAllDocsQuery(), 10, reverseSort).reverse();
		assertThat(page3.values()).hasSize(10);
		assertThat(page3.values()).map(Person::id).containsSequence(0L, 1L, 2L);
	}

	@Test
	void findManySortInstantField() throws IOException {
		Sort sort = new Sort(new SortedNumericSortField(instantField.name(), LONG));
		SortedValues<Person> result = indexRepository.findMany(
				new MatchAllDocsQuery(), 10, sort);
		assertThat(result.values()).hasSize(10);
		assertThat(result.values()).map(Person::id).containsSequence(0L, 1L, 2L);
	}

	@Test
	void findManySortCnp() throws IOException {
		Sort sort = new Sort(new SortedSetSortField(cnp.name(), false));
		SortedValues<Person> result = indexRepository.findMany(
				new MatchAllDocsQuery(), 10, sort);
		assertThat(result.values()).hasSize(10);
		assertThat(result.values()).map(Person::id).containsSequence(0L, 1L, 10L);
	}

	protected void indexRepositoryReset() throws IOException {
		indexRepository.reset(generatePeopleList(100));
	}
}
