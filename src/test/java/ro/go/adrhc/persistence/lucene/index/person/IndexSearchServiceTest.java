package ro.go.adrhc.persistence.lucene.index.person;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.search.Query;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.IndexTestFactories.QUERY_PARSER;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.PEOPLE;
import static ro.go.adrhc.persistence.lucene.index.person.PersonIndexFactories.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IndexSearchServiceTest {
	@TempDir
	private static Path tmpDir;

	@BeforeAll
	void beforeAll() throws IOException {
		createCreateService(tmpDir).createOrReplace(PEOPLE);
	}

	@Test
	void parse() throws IOException, QueryNodeException {
		List<Person> result = findAllMatches(QUERY_PARSER.parse(PersonFieldType.name, "pers*2*"));
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
	void wordStartsWith() throws IOException {
		List<Person> result = findAllMatches(NAME_WORD_QUERIES.wordStartsWith("(original)person"));

		assertThat(result).hasSize(1);
	}

	@Test
	void wordEquals() throws IOException {
		List<Person> result = findAllMatches(CNP_QUERIES.wordEquals("#Person3"));
//		result = findAllMatches(CNP_QUERIES.tokenEquals("#Person3"));

		assertThat(result).hasSize(1);
	}

	private List<Person> findAllMatches(Query query) throws IOException {
		return PersonIndexFactories.findAllMatches(tmpDir, query);
	}
}