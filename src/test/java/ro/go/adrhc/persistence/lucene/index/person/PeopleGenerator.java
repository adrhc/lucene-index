package ro.go.adrhc.persistence.lucene.index.person;

import ro.go.adrhc.persistence.lucene.index.core.TokenizationUtilsTest;

import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class PeopleGenerator {
	public static final List<Person> PEOPLE = List.of(
			// int id, String cnp, String name, String aliasKeyword,
			// String aliasWord, String aliasPhrase, Integer intField, Long longField,
			// String storedOnlyField
			new Person(1L, "#Person1", TokenizationUtilsTest.TEXT,
					"alias_Keyword1", "alias_Word1", "alias_Phrase1",
					111, 111L, "misc1"),
			new Person(2L, "#Person2", "IMG-20210725-WA0029 ccc_ddd CAșț.jpeg",
					"alias_Keyword2", "alias_Word2", "alias_Phrase2",
					222, 222L, "misc2"),
			new Person(3L,
					"Ping_Pong #name (Original)Person222 ăĂîÎșȘțȚ123",
					"Ping_Pong #name (Original)Person222 ăĂîÎșȘțȚ123",
					"alias_KeywordăĂîÎșȘțȚ123", "alias_WordăĂîÎșȘțȚ123",
					"alias_PhraseăĂîÎșȘțȚ123", 333, 333L, "misc3"));

	public static List<Person> generatePeopleList(long size) {
		return LongStream.range(0, size)
				.mapToObj(PeopleGenerator::generatePerson)
				.toList();
	}

	public static Stream<Person> generatePeopleStream(int start, int end) {
		return LongStream.range(start, end)
				.mapToObj(PeopleGenerator::generatePerson);
	}

	public static Person generatePerson(long i) {
		return new Person(i,
				"#Person" + i, TokenizationUtilsTest.TEXT + " nameșț" + i,
				"alias_Keyword" + (i % 2), "alias_Word" + (i % 2),
				"alias_Phrase" + (i % 2), (int) i, i,
				"storedOnlyField" + (i % 100));
	}
}
