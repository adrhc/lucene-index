package ro.go.adrhc.persistence.lucene.index.person;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.search.Query;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.adrhc.persistence.lucene.index.core.TokenizationUtilsTest;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.person.PersonIndexFactories.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IndexSearchServiceTest {
	private static final List<Person> PEOPLE = List.of(
			new Person("1", 111L, "#Person1", TokenizationUtilsTest.TEXT),
			new Person("2", 222L, "#Person2", "IMG-20210725-WA0029 ccc_ddd CAșț.jpeg"),
			new Person("3", 333L, "#Person3", "(Original)person222 CAșț"));
	@TempDir
	private static Path tmpDir;

	@BeforeAll
	void beforeAll() throws IOException {
		createCreateService(PEOPLE, tmpDir).createOrReplace();
	}

	@Test
	void freeQuery() throws IOException, QueryNodeException {
		List<Person> result = findAllMatches(NAME_QUERIES.parse("pers*2*"));
		assertThat(result).hasSize(1);
	}

	@Test
	void tokenEquals() throws IOException {
		List<Person> result = findAllMatches(NAME_QUERIES.tokenEquals("cast"));

		assertThat(result).hasSize(2);
	}

	@Test
	void tokenStartsWith() throws IOException {
		List<Person> result = findAllMatches(NAME_QUERIES.tokenStartsWith("person2"));

		assertThat(result).hasSize(1);
	}

	@Test
	void startsWith() throws IOException {
		List<Person> result = findAllMatches(NAME_AS_WORD_QUERIES.wordStartsWith("(original)person"));

		assertThat(result).hasSize(1);
	}

	@Test
	void cnpEquals() throws IOException {
		List<Person> result = findAllMatches(CNP_QUERIES.wordEquals("#Person3"));

		assertThat(result).hasSize(1);
	}

	private List<Person> findAllMatches(Query query) throws IOException {
		return PersonIndexFactories.findAllMatches(tmpDir, query);
	}
}