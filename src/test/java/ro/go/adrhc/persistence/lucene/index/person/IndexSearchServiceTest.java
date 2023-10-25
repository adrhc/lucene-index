package ro.go.adrhc.persistence.lucene.index.person;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.adrhc.persistence.lucene.index.core.TokenizationUtilsTest;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.person.PersonIndexFactories.createCreateService;
import static ro.go.adrhc.persistence.lucene.index.person.PersonIndexFactories.createSearchService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IndexSearchServiceTest {
	@TempDir
	private static Path tmpDir;

	@BeforeAll
	void beforeAll() throws IOException {
		createCreateService(createPeople(), tmpDir).createOrReplace();
	}

	@Test
	void parseNameQuery() throws IOException {
		IndexSearchService<String, Person> eqSearchService =
				createSearchService(PersonQueryFactory::nameTextQuery, tmpDir);

		List<Person> result = eqSearchService.findAllMatches("pers*2*");
		assertThat(result).hasSize(1);
	}

	@Test
	void nameEquals() throws IOException {
		IndexSearchService<String, Person> eqSearchService =
				createSearchService(PersonQueryFactory::nameEquals, tmpDir);

		assertThat(eqSearchService.findAllMatches("cast")).hasSize(2);
	}

	@Test
	void nameStartsWith() throws IOException {
		IndexSearchService<String, Person> prefixSearchService =
				createSearchService(PersonQueryFactory::nameStartsWith, tmpDir);

		assertThat(prefixSearchService.findAllMatches("person2")).hasSize(1);
	}

	@Test
	void exactCnp() throws IOException {
		IndexSearchService<String, Person> eqSearchService =
				createSearchService(PersonQueryFactory::exactCnp, tmpDir);

		assertThat(eqSearchService.findAllMatches("#Person3")).hasSize(1);
	}

	private static List<Person> createPeople() {
		return List.of(
				new Person("1", TokenizationUtilsTest.TEXT, "#Person1"),
				new Person("2", "IMG-20210725-WA0029 ccc_ddd CAșț.jpeg", "#Person2"),
				new Person("3", "(Original)person222 CAșț", "#Person3"));
	}
}