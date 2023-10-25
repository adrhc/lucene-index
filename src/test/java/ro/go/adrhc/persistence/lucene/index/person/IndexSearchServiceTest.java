package ro.go.adrhc.persistence.lucene.index.person;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.search.Query;
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
		List<PersonSearchResult> result = findAllMatches(
				PersonQueryFactory::nameTextQuery, "pers*2*");
		assertThat(result).hasSize(1);
	}

	@Test
	void nameEquals() throws IOException {
		List<PersonSearchResult> result = findAllMatches(
				PersonQueryFactory::nameEquals, "cast");

		assertThat(result).hasSize(2);
	}

	@Test
	void nameStartsWith() throws IOException {
		List<PersonSearchResult> result = findAllMatches(
				PersonQueryFactory::nameStartsWith, "person2");

		assertThat(result).hasSize(1);
	}

	@Test
	void oneTokenNameStartsWith() throws IOException {
		List<PersonSearchResult> result = findAllMatches(
				PersonQueryFactory::oneTokenNameStartsWith, "(original)person");

		assertThat(result).hasSize(1);
	}

	@Test
	void exactCnp() throws IOException {
		List<PersonSearchResult> result = findAllMatches(
				PersonQueryFactory::exactCnp, "#Person3");

		assertThat(result).hasSize(1);
	}

	private List<PersonSearchResult> findAllMatches(
			SneakyFunction<String, Query, QueryNodeException> stringQueryConverter,
			String textToSearch) throws IOException {
		return createSearchService(stringQueryConverter).findAllMatches(textToSearch);
	}

	private IndexSearchService<String, PersonSearchResult> createSearchService(
			SneakyFunction<String, Query, QueryNodeException> stringQueryConverter) {
		return PersonIndexFactories.createSearchService(stringQueryConverter, tmpDir);
	}

	private static List<Person> createPeople() {
		return List.of(
				new Person("1", "#Person1", TokenizationUtilsTest.TEXT),
				new Person("2", "#Person2", "IMG-20210725-WA0029 ccc_ddd CAșț.jpeg"),
				new Person("3", "#Person3", "(Original)person222 CAșț"));
	}
}