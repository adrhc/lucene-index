package ro.go.adrhc.persistence.lucene.index.person;

import ro.go.adrhc.persistence.lucene.index.core.TokenizationUtilsTest;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PeopleGenerator {
	public static final List<Person> PEOPLE = List.of(
			new Person(1, 111L, "#Person1", TokenizationUtilsTest.TEXT,
					"alias_Keyword1", "alias_Word1", "alias_Phrase1", "misc1"),
			new Person(2, 222L, "#Person2", "IMG-20210725-WA0029 ccc_ddd CAșț.jpeg",
					"alias_Keyword2", "alias_Word2", "alias_Phrase2", "misc2"),
			new Person(3, 333L,
					"Ping_Pong #name (Original)Person222 ăĂîÎșȘțȚ123",
					"Ping_Pong #name (Original)Person222 ăĂîÎșȘțȚ123",
					"alias_KeywordăĂîÎșȘțȚ123",
					"alias_WordăĂîÎșȘțȚ123",
					"alias_PhraseăĂîÎșȘțȚ123",
					"misc3"));

	public static List<Person> generatePeopleList(int size) {
		return IntStream.range(0, size)
				.mapToObj(PeopleGenerator::generatePerson)
				.toList();
	}

	public static Stream<Person> generatePeopleStream(int start, int end) {
		return IntStream.range(start, end)
				.mapToObj(PeopleGenerator::generatePerson);
	}

	public static Person generatePerson(int i) {
		return new Person(i, (long) i,
				"#Person" + i, TokenizationUtilsTest.TEXT + " nameșț" + i,
				"alias_Keyword" + (i % 2), "alias_Word" + (i % 2),
				"alias_Phrase" + (i % 2), "misc" + (i % 100));
	}
}
