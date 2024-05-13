package ro.go.adrhc.persistence.lucene.index.person;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.PEOPLE;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.generatePerson;
import static ro.go.adrhc.persistence.lucene.index.person.PersonFieldType.ALIAS_KEYWORD_QUERIES;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class PersonCrudTest extends AbstractPersonsIndexTest {
    @Test
    void crudTest() throws IOException {
        int count = indexRepository.count(ALIAS_KEYWORD_QUERIES.startsWith("alias_Keyword"));
        log.info("\ncount: {}", count);
        assertThat(count).isEqualTo(PEOPLE.size());

        indexRepository.addOne(generatePerson(4));
        assertThat(indexRepository.findById(4L)).isPresent();

        indexRepository.removeById(4L);
        assertThat(indexRepository.findById(4L)).isEmpty();
    }

    @Test
    void updateTest() throws IOException {
        Optional<Person> optionalPerson = indexRepository.findById(1L);
        assertThat(optionalPerson).isPresent();

        String newStoredOnlyField = Instant.now().toString();
        Person person = optionalPerson.get().storedOnlyField(newStoredOnlyField);
        indexRepository.upsert(person);

        optionalPerson = indexRepository.findById(person.getId());
        assertThat(optionalPerson).isPresent();
        assertThat(optionalPerson.get().storedOnlyField()).isEqualTo(newStoredOnlyField);
    }
}
