package ro.go.adrhc.persistence.lucene.index.person;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
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
public class SearchPerformanceTest extends AbstractPersonsIndexTest {
	@BeforeAll
	void beforeAll() throws IOException {
		tmpDir = Path.of("C:/Users/adpetre/Temp/test-index/people");
//		createCreateService().createOrReplace(generatePeopleStream(0, 10000));
//		createUpdateService(TMP).addAll(generatePeopleStream(5000, 10_000_001));
//		createUpdateService(TMP).addAll(generatePeopleStream(2_500_001, 10_000_001));
	}

	@RepeatedTest(2)
	void keywordTest() throws IOException {
		StopWatch stopWatch = StopWatchUtils.start();
		int count = count(ALIAS_KEYWORD_QUERIES.keywordEquals("alias_Keyword0"));
		stopWatch.stop();
		log.info("\ncount time: {}", stopWatch.formatTime());
		log.info("\ncount: {}", count);
		assertThat(count).isGreaterThan(1000);
		stopWatch = StopWatchUtils.start();
		List<Person> people = findAllMatches(ALIAS_KEYWORD_QUERIES.keywordEquals("alias_Keyword0"));
		stopWatch.stop();
		log.info("\npeople time: {}", stopWatch.formatTime());
		log.info("\npeople count: {}", people.size());
	}

	@RepeatedTest(2)
	void wordTest() throws IOException {
		StopWatch stopWatch = StopWatchUtils.start();
		int count = count(ALIAS_WORD_QUERIES.keywordEquals("alias word0"));
		stopWatch.stop();
		log.info("\ntime: {}", stopWatch.formatTime());
		log.info("\ncount: {}", count);
		assertThat(count).isGreaterThan(1000);
		stopWatch = StopWatchUtils.start();
		List<Person> people = findAllMatches(ALIAS_WORD_QUERIES.keywordEquals("alias word0"));
		stopWatch.stop();
		log.info("\npeople time: {}", stopWatch.formatTime());
		log.info("\npeople count: {}", people.size());
	}

	@RepeatedTest(2)
	void phraseTest() throws IOException {
		StopWatch stopWatch = StopWatchUtils.start();
		int count = count(ALIAS_PHRASE_QUERIES.keywordEquals("phrase0"));
		stopWatch.stop();
		log.info("\ntime: {}", stopWatch.formatTime());
		log.info("\ncount: {}", count);
		assertThat(count).isGreaterThan(1000);
		stopWatch = StopWatchUtils.start();
		List<Person> people = findAllMatches(ALIAS_PHRASE_QUERIES.keywordEquals("phrase0"));
		stopWatch.stop();
		log.info("\npeople time: {}", stopWatch.formatTime());
		log.info("\npeople count: {}", people.size());
	}

	@RepeatedTest(2)
	void intTest() throws IOException {
		StopWatch stopWatch = StopWatchUtils.start();
		int count = count(ID_QUERIES.longEquals(1111));
		stopWatch.stop();
		log.info("\ntime: {}", stopWatch.formatTime());
		log.info("\ncount: {}", count);
		assertThat(count).isEqualTo(1);
		stopWatch = StopWatchUtils.start();
		List<Person> people = findAllMatches(ID_QUERIES.longEquals(1111));
		stopWatch.stop();
		log.info("\npeople time: {}", stopWatch.formatTime());
		log.info("\npeople count: {}", people.size());
	}
}
