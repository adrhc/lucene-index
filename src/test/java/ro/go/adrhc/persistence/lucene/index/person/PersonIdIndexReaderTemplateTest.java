package ro.go.adrhc.persistence.lucene.index.person;

import org.junit.jupiter.api.Test;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIdIndexReaderTemplate;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.PEOPLE;

public class PersonIdIndexReaderTemplateTest extends AbstractPersonsIndexTest {
	@Test
	void readTest() throws IOException {
		TypedIdIndexReaderTemplate<Long> readerTemplate = createPersonIdIndexReaderTemplate();
		List<Long> ids = readerTemplate.useIdsReader(idsReader -> idsReader.getAllIds().toList());
		assertThat(ids).isNotEmpty();
		assertThat(ids).containsAll(PEOPLE.stream().map(Person::getId).toList());
	}
}
