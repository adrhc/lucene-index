package ro.go.adrhc.persistence.lucene.index.person;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.PEOPLE;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.generatePerson;
import static ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSourceFactory.createCachedDataSource;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class IndexRestoreServiceTest extends AbstractPersonsIndexTest {
    @Test
    void restoreTest() throws IOException {
        indexRepository.addOne(generatePerson(4));

        indexRepository.removeById(3L);

        assertThat(indexRepository.findById(3L)).isEmpty();
        assertThat(indexRepository.findById(4L)).isPresent();

        indexRepository.restore(createCachedDataSource(PEOPLE));

        assertThat(indexRepository.findById(3L)).isPresent();
        assertThat(indexRepository.findById(4L)).isEmpty();
    }
}
