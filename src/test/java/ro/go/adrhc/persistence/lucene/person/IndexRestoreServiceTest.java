package ro.go.adrhc.persistence.lucene.person;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.junit.jupiter.api.Test;
import ro.go.adrhc.persistence.lucene.core.typed.ExactQuery;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.core.typed.write.shallow.TypedIndexDataSourceFactory.createCachedDataSource;
import static ro.go.adrhc.persistence.lucene.person.PeopleGenerator.PEOPLE;
import static ro.go.adrhc.persistence.lucene.person.PeopleGenerator.generateGirl;

@Slf4j
class IndexRestoreServiceTest extends AbstractPersonRAMIndexTest {
	@Override
	protected void indexReset() throws IOException {
		index.reset(PEOPLE);
	}

	@Test
	void restoreTest() throws IOException {
		index.addOne(generateGirl(4));

		index.removeById(3L);

		assertThat(index.findById(3L)).isEmpty();
		assertThat(index.findById(4L)).isPresent();

		index.shallowUpdate(createCachedDataSource(PEOPLE));

		assertThat(index.count()).isEqualTo(PEOPLE.size());
		assertThat(index.getAllIds()).containsOnlyOnceElementsOf(
			PEOPLE.stream().map(Person::id).toList());
	}

	@Test
	void restoreSubsetTest() throws IOException {
		index.addOne(generateGirl(4));

		index.removeById(3L);

		assertThat(index.findById(3L)).isEmpty(); // boy
		assertThat(index.findById(4L)).isPresent();

		Query query = ExactQuery.create(PersonSchema.male).newExactQuery(true);
		index.shallowUpdateSubset(
			createCachedDataSource(PEOPLE.stream().filter(Person::male)),
			query);

		assertThat(index.count()).isEqualTo(PEOPLE.size() + 1);
		assertThat(index.getAllIds()).containsAll(
			PEOPLE.stream().map(Person::id).toList());
		assertThat(index.findById(4L)).isPresent();
	}
}
