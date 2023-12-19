package ro.go.adrhc.persistence.lucene.index.person;

import org.junit.jupiter.api.Test;
import ro.go.adrhc.persistence.lucene.typedcore.read.OneHitIndexReaderTemplate;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.person.PersonFieldType.ID_QUERIES;

public class OneHitIndexReaderTemplateTest extends AbstractPersonsIndexTest {
    @Test
    void readTest() throws IOException {
        OneHitIndexReaderTemplate<Person> readerTemplate = createPersonIdIndexReaderTemplate();
        Optional<Person> optionalPerson = readerTemplate
                .useOneHitReader(reader -> reader.findFirst(ID_QUERIES.longEquals(1L)));
        assertThat(optionalPerson).isPresent();
        assertThat(optionalPerson.get().getId()).isEqualTo(1L);
    }
}
