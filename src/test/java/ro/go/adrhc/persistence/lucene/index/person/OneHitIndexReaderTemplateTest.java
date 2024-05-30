package ro.go.adrhc.persistence.lucene.index.person;

import org.junit.jupiter.api.Test;
import ro.go.adrhc.persistence.lucene.typedcore.read.OneHitIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreAndValue;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.person.PersonFieldType.ID_QUERIES;

public class OneHitIndexReaderTemplateTest extends AbstractPersonsIndexTest {
	@Test
	void readTest() throws IOException {
		OneHitIndexReaderTemplate<Person> readerTemplate = createPersonIdIndexReaderTemplate();
		Optional<Person> optionalPerson = readerTemplate.useOneHitReader(r -> r
				.findFirst(ID_QUERIES.longEquals(1L))
				.map(ScoreAndValue::value));
		assertThat(optionalPerson).isPresent();
		assertThat(optionalPerson.get().getId()).isEqualTo(1L);
	}
}
