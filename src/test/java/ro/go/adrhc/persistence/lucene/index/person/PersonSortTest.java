package ro.go.adrhc.persistence.lucene.index.person;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.FieldExistsQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortedNumericSortField;
import org.apache.lucene.search.SortedSetSortField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.apache.lucene.search.SortField.Type.LONG;
import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.generatePeopleList;
import static ro.go.adrhc.persistence.lucene.index.person.PersonFieldType.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class PersonSortTest extends AbstractPersonsIndexTest {
	@Test
	void findManySortInstantField() throws IOException {
		Sort sort = new Sort(new SortedNumericSortField(instantField.name(), LONG));
		List<Person> result = indexRepository.findMany(
				new FieldExistsQuery(id.name()), 10, sort);
		assertThat(result).hasSize(10);
		assertThat(result).map(Person::id).containsSequence(0L, 9L, 18L);
	}

	@Test
	void findManySortCnp() throws IOException {
		Sort sort = new Sort(new SortedSetSortField(cnp.name(), false));
		List<Person> result = indexRepository.findMany(
				new FieldExistsQuery(id.name()), 10, sort);
		assertThat(result).hasSize(10);
		assertThat(result).map(Person::id).containsSequence(0L, 1L, 10L);
	}

	protected void indexRepositoryReset() throws IOException {
		indexRepository.reset(generatePeopleList(100));
	}
}
