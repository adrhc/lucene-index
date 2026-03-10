package ro.go.adrhc.persistence.lucene.person;

import org.apache.lucene.search.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ro.go.adrhc.persistence.lucene.core.typed.ExactQuery;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderTemplate;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.person.PeopleGenerator.PEOPLE;

class TypedIndexReaderTemplateTest extends AbstractPersonRAMIndexTest {
	@Override
	protected void indexReset() throws IOException {
		index.reset(PEOPLE);
	}

	@Test
	void readTest() throws IOException {
		TypedIndexReaderTemplate<Long, Person>
			readerTemplate = typedIndexReaderTemplate();
		List<Long> ids = readerTemplate.useReader(reader -> reader.getAllIds().toList());
		assertThat(ids).isNotEmpty();
		assertThat(ids).containsAll(PEOPLE.stream().map(Person::getId).toList());
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void findByBoolean(boolean male) throws IOException {
		TypedIndexReaderTemplate<Long, Person>
			readerTemplate = typedIndexReaderTemplate();
		Query query = ExactQuery.create(PersonSchema.male).newExactQuery(male);
		List<Long> ids = readerTemplate.useReader(reader -> reader.findIds(query).toList());
		assertThat(ids).isNotEmpty();
		assertThat(ids).containsAll(
			PEOPLE.stream().filter(p -> p.male() == male).map(Person::getId).toList());
	}
}
