package ro.go.adrhc.persistence.lucene.person;

import ro.go.adrhc.persistence.lucene.core.TokenizationUtilsTest;

import java.time.Instant;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class PeopleGenerator {
	public static final List<Person> PEOPLE = List.of(
			// int id, String cnp, String name, String aliasKeyWord,
			// String aliasWord, String aliasPhrase, Integer intField, Long longField,
			// String storedOnlyField
			new Person(1L, "#Person1", TokenizationUtilsTest.TEXT,
					"alias_Keyword1", "alias_Word1", "alias_Phrase1",
					111, 111L, Instant.parse("2001-01-02T03:04:05.06Z"),
					"misc1", false),
			new Person(2L, "#Person2", "IMG-20210725-WA0029 ccc_ddd an CAșț.jpeg",
					"alias_Keyword2", "alias_Word2", "alias_Phrase2",
					222, 222L, Instant.parse("2002-01-02T03:04:05.06Z"),
					"misc2", false),
			new Person(3L,
					"Ping_Pong #name (Original)Person222 ăĂîÎșȘțȚ123",
					"Ping_Pong #name (Original)Person222 ăĂîÎșȘțȚ123",
					"alias_KeywordăĂîÎșȘțȚ123", "alias_WordăĂîÎșȘțȚ123",
					"aliasPhraseăĂîÎșȘțȚ123", 333, 333L,
					Instant.parse("2003-01-02T03:04:05.06Z"),
					"misc3", true));

	public static List<Person> generatePeopleList(long size) {
		return LongStream.range(0, size)
				.mapToObj(PeopleGenerator::generateGirl)
				.toList();
	}

	public static Stream<Person> generatePeopleStream(int start, int end) {
		return LongStream.range(start, end)
				.mapToObj(PeopleGenerator::generateGirl);
	}

	public static Person generateGirl(long i) {
		String millis = "%03d".formatted(i % 1000);
		return generateGirl(i, Instant.parse("2000-01-01T03:04:05.%sZ".formatted(millis)));
	}

	public static Person generateGirl(long i, Instant instantField) {
		return new Person(i,
				"#Person" + i, TokenizationUtilsTest.TEXT + " nameșț" + i,
				"alias_Keyword" + (i % 2), "alias_Word" + (i % 2),
				"alias_Phrase" + (i % 2), (int) i, i,
				instantField, "storedOnlyField" + (i % 100), false);
	}
}
