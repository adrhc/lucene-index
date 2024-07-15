package ro.go.adrhc.persistence.lucene.person;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.TypedIndexParamsTestFactory.ANALYZER;
import static ro.go.adrhc.persistence.lucene.TypedIndexParamsTestFactory.NAME_QUERY_PARSER;
import static ro.go.adrhc.persistence.lucene.person.PeopleGenerator.PEOPLE;
import static ro.go.adrhc.persistence.lucene.person.PersonFieldType.*;
import static ro.go.adrhc.util.fn.SneakyFunctionUtils.toNullResultFn;

@ExtendWith(MockitoExtension.class)
@Slf4j
class PersonQueriesTest extends AbstractPersonsIndexTest {
	private static final Person PERSON2 = PEOPLE.get(1);
	private static final Person PERSON3 = PEOPLE.get(2);

	@Test
	void parse() {
		// tokens (i.e. other than KeywordField) must be normalized!
		List<Person> result = NAME_QUERY_PARSER.parse("pers*2*")
				.map(toNullResultFn(indexRepository::findMany))
				.orElseGet(List::of);
		assertThat(result).hasSize(1);
		assertThat(result.getFirst().id()).isEqualTo(PERSON3.id());
	}

	@Test
	void closeFuzzyTokens() throws IOException {
		// tokens (i.e. other than KeywordField) must be normalized!
		List<Person> result = indexRepository.findMany(
				NAME_QUERIES.maxFuzzinessNearTokens(List.of("ddd", "an", "cast")));

		assertThat(result).hasSize(1);
		assertThat(result.getFirst().id()).isEqualTo(PERSON2.id());
	}

	@Test
	void tokenEquals() throws IOException {
		// tokens (i.e. other than KeywordField) must be normalized!
		List<Person> result = indexRepository.findMany(
				ALIAS_PHRASE_QUERIES.tokenEquals("aliasphraseaaiisstt123"));

		assertThat(result).hasSize(1);
		assertThat(result.getFirst().id()).isEqualTo(PERSON3.id());
	}

	@Test
	void wordEquals() throws IOException {
		String aliasWord = PERSON3.aliasWord();
		String normalized = ANALYZER.normalize(null, aliasWord).utf8ToString();
		log.info("\naliasWord is:\t\t{}\nnormalized is:\t{}", aliasWord, normalized);
		// tokens (i.e. other than KeywordField) must be normalized!
		List<Person> result = indexRepository.findMany(
				ALIAS_WORD_QUERIES.tokenEquals(normalized));

		assertThat(result).hasSize(1);
		assertThat(result.getFirst().id()).isEqualTo(PERSON3.id());
	}

	@Test
	void keywordEquals() throws IOException {
		String aliasKeyword = PERSON3.aliasKeyWord();
		log.info("\naliasKeyWord is: {}", aliasKeyword);
		// KeywordField shouldn't be normalized!
		List<Person> result = indexRepository.findMany(
				ALIAS_KEYWORD_QUERIES.keywordEquals(aliasKeyword));

		assertThat(result).hasSize(1);
		assertThat(result.getFirst().id()).isEqualTo(PERSON3.id());
	}

	@Test
	void tokenStartsWith() throws IOException {
		String token = PERSON3.aliasPhrase();
		String prefix = ANALYZER.normalize(null, token).utf8ToString();
		prefix = prefix.substring(0, prefix.length() - 1);
		log.info("\ntoken is:\t{}\nprefix is:\t{}", token, prefix);
		// tokens (i.e. other than KeywordField) must be normalized!
		List<Person> result = indexRepository.findMany(
				ALIAS_PHRASE_QUERIES.startsWith(prefix));

		assertThat(result).hasSize(1);
	}

	@Test
	void wordStartsWith() throws IOException {
		String name = PERSON3.name();
		String prefix = ANALYZER.normalize(null, name).utf8ToString();
		prefix = prefix.substring(0, prefix.length() - 1);
		log.info("\nname is:\t\t{}\nprefix is:\t{}", name, prefix);
		// tokens (i.e. other than KeywordField) must be normalized!
		List<Person> result = indexRepository.findMany(NAME_WORD_QUERIES.startsWith(prefix));

		assertThat(result).hasSize(1);
	}

	@Test
	void keywordStartsWith() throws IOException {
		String cnp = PERSON3.cnp();
		String prefix = cnp.substring(0, cnp.length() - 1);
		log.info("\ncnp is:\t\t{}\nprefix is:\t{}", cnp, prefix);
		// KeywordField shouldn't be normalized!
		List<Person> result = indexRepository.findMany(CNP_QUERIES.startsWith(prefix));

		assertThat(result).hasSize(1);
	}
}