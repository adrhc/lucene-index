package ro.go.adrhc.persistence.lucene.index.person;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.generatePerson;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class IndexRestoreServiceTest extends AbstractPersonsIndexTest {
	@Test
	void restoreTest() throws IOException {
		createAdderService().addOne(generatePerson(4));

		TypedIndexRemoveService<Long> indexRemoveService = createIndexRemoveService();
		indexRemoveService.removeById(3L);

		TypedSearchByIdService<Long, Person> searchByIdService = createSearchByIdService();
		assertThat(searchByIdService.findById(3L)).isEmpty();
		assertThat(searchByIdService.findById(4L)).isPresent();

		TypedIndexRestoreService<Long, Person> restoreService = createIndexRestoreService();
		restoreService.restore(createPeopleDataSource());

		assertThat(searchByIdService.findById(3L)).isPresent();
		assertThat(searchByIdService.findById(4L)).isEmpty();
	}
}
