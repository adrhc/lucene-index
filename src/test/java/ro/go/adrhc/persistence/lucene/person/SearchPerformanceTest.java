package ro.go.adrhc.persistence.lucene.person;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;
import ro.go.adrhc.util.StopWatchUtils;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.person.PersonQueryFactory.*;

@Slf4j
class SearchPerformanceTest extends AbstractPersonRAMIndexTest {
	private static final int SIZE = 1000;

	@Override
	protected int peopleListSize() {
		return SIZE;
	}

	@Test
	void keywordTest() throws IOException {
		StopWatch stopWatch = StopWatchUtils.start();
		int count = index.count(ALIAS_KEYWORD_QUERIES.keywordEquals("alias_Keyword0"));
		stopWatch.stop();
		log.info("\ncount time: {}", stopWatch.formatTime());
		log.info("\ncount: {}", count);
		assertThat(count).isGreaterThan(SIZE / 2 - 1);
		stopWatch = StopWatchUtils.start();
		List<Person> people = index.findMany(ALIAS_KEYWORD_QUERIES.keywordEquals("alias_Keyword0"));
		stopWatch.stop();
		log.info("\npeople time: {}", stopWatch.formatTime());
		log.info("\npeople count: {}", people.size());
	}

	@Test
	void wordTest() throws IOException {
		String normalized = analyzer().normalize(null, "alias_Word0").utf8ToString();
		StopWatch stopWatch = StopWatchUtils.start();
		int count = index.count(ALIAS_WORD_QUERIES.tokenEquals(normalized));
		stopWatch.stop();
		log.info("\ntime: {}", stopWatch.formatTime());
		log.info("\ncount: {}", count);
		assertThat(count).isGreaterThan(SIZE / 2 - 1);
		stopWatch = StopWatchUtils.start();
		List<Person> people = index.findMany(ALIAS_WORD_QUERIES.tokenEquals(normalized));
		stopWatch.stop();
		log.info("\npeople time: {}", stopWatch.formatTime());
		log.info("\npeople count: {}", people.size());
	}

	@Test
	void phraseTest() throws IOException {
		String normalized = analyzer().normalize(null, "alias_Phrase0").utf8ToString();
		StopWatch stopWatch = StopWatchUtils.start();
		int count = index.count(ALIAS_PHRASE_QUERIES.tokenEquals(normalized));
		stopWatch.stop();
		log.info("\ntime: {}", stopWatch.formatTime());
		log.info("\ncount: {}", count);
		assertThat(count).isGreaterThan(SIZE / 2 - 1);
		stopWatch = StopWatchUtils.start();
		List<Person> people = index.findMany(ALIAS_PHRASE_QUERIES.tokenEquals(normalized));
		stopWatch.stop();
		log.info("\npeople time: {}", stopWatch.formatTime());
		log.info("\npeople count: {}", people.size());
	}

	@Test
	void intTest() throws IOException {
		StopWatch stopWatch = StopWatchUtils.start();
		int count = index.count(ID_QUERIES.longEquals(SIZE / 2 - 1));
		stopWatch.stop();
		log.info("\ntime: {}", stopWatch.formatTime());
		log.info("\ncount: {}", count);
		assertThat(count).isEqualTo(1);
		stopWatch = StopWatchUtils.start();
		List<Person> people = index.findMany(ID_QUERIES.longEquals(SIZE / 2 - 1));
		stopWatch.stop();
		log.info("\npeople time: {}", stopWatch.formatTime());
		log.info("\npeople count: {}", people.size());
	}
}
