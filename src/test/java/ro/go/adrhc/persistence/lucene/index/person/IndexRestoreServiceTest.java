package ro.go.adrhc.persistence.lucene.index.person;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.DocumentsIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.generatePerson;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class IndexRestoreServiceTest extends AbstractPersonsIndexTest {
	@Test
	void restoreTest() throws IOException {
		TypedIndexUpdateService<Person> indexUpdateService = createUpdateService();
		indexUpdateService.add(generatePerson(4));

		TypedIndexRemoveService<Long> indexRemoveService = createIndexRemoveService();
		indexRemoveService.removeById(3L);

		TypedSearchByIdService<Long, Person> searchByIdService = createSearchByIdService();
		assertThat(searchByIdService.findById(3L)).isEmpty();
		assertThat(searchByIdService.findById(4L)).isPresent();

		DocumentsIndexRestoreService<Long, Person> restoreService = createIndexRestoreService();
		restoreService.restore(createIndexDataSource());

		assertThat(searchByIdService.findById(3L)).isPresent();
		assertThat(searchByIdService.findById(4L)).isEmpty();
	}
}
