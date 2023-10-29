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
//		createCreateService(TMP).createOrReplace(generatePeopleList(1));
//		createUpdateService(TMP).addAll(generatePeopleStream(2_500_001, 10_000_001));
	}

	@RepeatedTest(3)
	void keywordTest() throws IOException {
		StopWatch stopWatch = StopWatchUtils.start();
		int count = count(ALIAS_KEYWORD_QUERIES.wordEquals("alias0"));
		stopWatch.stop();
		log.info("\ncount time: {}", stopWatch.formatTime());
		log.info("\ncount: {}", count);
		assertThat(count).isGreaterThan(1000);
		stopWatch = StopWatchUtils.start();
		List<Person> people = findAllMatches(ALIAS_KEYWORD_QUERIES.wordEquals("alias0"));
		stopWatch.stop();
		log.info("\npeople time: {}", stopWatch.formatTime());
		log.info("\npeople count: {}", people.size());
	}

	@RepeatedTest(3)
	void wordTest() throws IOException {
		StopWatch stopWatch = StopWatchUtils.start();
		int count = count(ALIAS_WORD_QUERIES.wordEquals("alias0"));
		stopWatch.stop();
		log.info("\ntime: {}", stopWatch.formatTime());
		log.info("\ncount: {}", count);
		assertThat(count).isGreaterThan(1000);
		stopWatch = StopWatchUtils.start();
		List<Person> people = findAllMatches(ALIAS_WORD_QUERIES.wordEquals("alias0"));
		stopWatch.stop();
		log.info("\npeople time: {}", stopWatch.formatTime());
		log.info("\npeople count: {}", people.size());
	}

	@RepeatedTest(3)
	void phraseTest() throws IOException {
		StopWatch stopWatch = StopWatchUtils.start();
		int count = count(ALIAS_PHRASE_QUERIES.wordEquals("alias0"));
		stopWatch.stop();
		log.info("\ntime: {}", stopWatch.formatTime());
		log.info("\ncount: {}", count);
		assertThat(count).isGreaterThan(1000);
		stopWatch = StopWatchUtils.start();
		List<Person> people = findAllMatches(ALIAS_PHRASE_QUERIES.wordEquals("alias0"));
		stopWatch.stop();
		log.info("\npeople time: {}", stopWatch.formatTime());
		log.info("\npeople count: {}", people.size());
	}

	private int count(Query query) throws IOException {
		return PersonIndexFactories.count(TMP, query);
	}

	private List<Person> findAllMatches(Query query) throws IOException {
		return PersonIndexFactories.findAllMatches(TMP, query);
	}
}
