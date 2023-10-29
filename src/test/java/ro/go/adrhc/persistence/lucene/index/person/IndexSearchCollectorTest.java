package ro.go.adrhc.persistence.lucene.index.person;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.lucene.search.Query;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.adrhc.util.StopWatchUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.generatePeopleStream;
import static ro.go.adrhc.persistence.lucene.index.person.PersonIndexFactories.*;

@Disabled
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class IndexSearchCollectorTest {
	//	@TempDir
	private static final Path TMP = Path.of("C:/Users/adpetre/Temp/test-index/people");

	@BeforeAll
	void beforeAll() throws IOException {
//		createCreateService(generatePeopleList(2_500_001), TMP).createOrReplace();
		createUpdateService(TMP).addAll(generatePeopleStream(2_500_001));
	}

	@RepeatedTest(3)
	void keywordStartsWith() throws IOException {
		StopWatch stopWatch = StopWatchUtils.start();
		List<Person> result = findAllMatches(ALIAS_KEYWORD_QUERIES.wordStartsWith("alias0"));
		stopWatch.stop();
		log.info("\ntime: {}", stopWatch.formatTime());
		log.info("\nresults size: {}", result.size());
		assertThat(result).hasSizeGreaterThan(1000);
	}

	@RepeatedTest(3)
	void wordStartsWith() throws IOException {
		StopWatch stopWatch = StopWatchUtils.start();
		List<Person> result = findAllMatches(ALIAS_WORD_QUERIES.wordStartsWith("alias0"));
		stopWatch.stop();
		log.info("\ntime: {}", stopWatch.formatTime());
		log.info("\nresults size: {}", result.size());
		assertThat(result).hasSizeGreaterThan(1000);
	}

	@RepeatedTest(3)
	void phraseStartsWith() throws IOException {
		StopWatch stopWatch = StopWatchUtils.start();
		List<Person> result = findAllMatches(ALIAS_PHRASE_QUERIES.wordStartsWith("alias0"));
		stopWatch.stop();
		log.info("\ntime: {}", stopWatch.formatTime());
		log.info("\nresults size: {}", result.size());
		assertThat(result).hasSizeGreaterThan(1000);
	}

	private List<Person> findAllMatches(Query query) throws IOException {
		return PersonIndexFactories.findAllMatches(TMP, query);
	}
}
