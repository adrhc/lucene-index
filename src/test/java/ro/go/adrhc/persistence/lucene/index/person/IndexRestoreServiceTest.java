package ro.go.adrhc.persistence.lucene.index.person;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.adrhc.persistence.lucene.typedcore.ExactQuery;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.operations.restore.IndexDataSourceFactory.createCachedDataSource;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.PEOPLE;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.generateGirl;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class IndexRestoreServiceTest extends AbstractPersonsIndexTest {
	@Test
	void restoreTest() throws IOException {
		indexRepository.addOne(generateGirl(4));

		indexRepository.removeById(3L);

		assertThat(indexRepository.findById(3L)).isEmpty();
		assertThat(indexRepository.findById(4L)).isPresent();

		indexRepository.shallowUpdate(createCachedDataSource(PEOPLE));

		assertThat(indexRepository.count()).isEqualTo(PEOPLE.size());
		assertThat(indexRepository.getAllIds()).containsOnlyOnceElementsOf(
				PEOPLE.stream().map(Person::id).toList());
	}

	@Test
	void restoreSubsetTest() throws IOException {
		indexRepository.addOne(generateGirl(4));

		indexRepository.removeById(3L);

		assertThat(indexRepository.findById(3L)).isEmpty(); // boy
		assertThat(indexRepository.findById(4L)).isPresent();

		Query query = ExactQuery.create(PersonFieldType.male).newExactQuery(true);
		indexRepository.shallowUpdateSubset(
				createCachedDataSource(PEOPLE.stream().filter(Person::male)),
				query);

		assertThat(indexRepository.count()).isEqualTo(PEOPLE.size() + 1);
		assertThat(indexRepository.getAllIds()).containsAll(
				PEOPLE.stream().map(Person::id).toList());
		assertThat(indexRepository.findById(4L)).isPresent();
	}
}
