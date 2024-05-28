package ro.go.adrhc.persistence.lucene.index.person;

import org.apache.lucene.search.Query;
import org.junit.jupiter.api.Test;
import ro.go.adrhc.persistence.lucene.typedcore.ExactQuery;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderTemplate;

import java.io.IOException;
import java.util.List;

import static java.util.function.Predicate.not;
import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.PEOPLE;

public class TypedIndexReaderTemplateTest extends AbstractPersonsIndexTest {
	@Test
	void readTest() throws IOException {
		TypedIndexReaderTemplate<Long, Person> readerTemplate = createPersonIndexReaderTemplate();
		List<Long> ids = readerTemplate.useReader(reader -> reader.getAllIds().toList());
		assertThat(ids).isNotEmpty();
		assertThat(ids).containsAll(PEOPLE.stream().map(Person::getId).toList());
	}

	@Test
	void findIds() throws IOException {
		TypedIndexReaderTemplate<Long, Person> readerTemplate = createPersonIndexReaderTemplate();
		Query query = ExactQuery.create(PersonFieldType.male).newExactQuery(false);
		List<Long> ids = readerTemplate.useReader(reader -> reader.findIds(query).toList());
		assertThat(ids).isNotEmpty();
		assertThat(ids).containsAll(PEOPLE.stream()
				.filter(not(Person::male)).map(Person::getId).toList());
	}
}
