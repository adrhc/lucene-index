package ro.go.adrhc.persistence.lucene.index.person;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexCreateService;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.person.PersonIndexFactories.createCreateService;
import static ro.go.adrhc.persistence.lucene.index.person.PersonIndexFactories.createSearchService;

@ExtendWith(MockitoExtension.class)
class IndexSearchServiceTest {
	@Test
	void findAllMatches(@TempDir Path tmpDir) throws IOException {
		FSIndexCreateService createService = createCreateService(createPeople(), tmpDir);
		createService.createOrReplace();

		IndexSearchService<String, Person> searchService =
				createSearchService(PersonQueryFactory::nameEquals, tmpDir);
		List<Person> result = searchService.findAllMatches("cast");

		assertThat(result).hasSize(2);
	}

	private static List<Person> createPeople() {
		return List.of(
				new Person("1", "IMG-20210725-WA0029 ccc_ddd CAșț.jpeg"),
				new Person("2", "(Original)person222 CAșț"));
	}
}