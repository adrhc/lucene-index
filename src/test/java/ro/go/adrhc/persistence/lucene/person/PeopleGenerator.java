package ro.go.adrhc.persistence.lucene.person;

import ro.go.adrhc.persistence.lucene.core.bare.token.TestData;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.LongStream;

public class PeopleGenerator {
	public static final List<Person> PEOPLE = List.of(
		// int id, String cnp, String name, String aliasKeyWord,
		// String aliasWord, String aliasPhrase, Integer intField, Long longField,
		// String storedOnlyField
		new Person(1L, "#Person1", TestData.TEXT,
			"alias_Keyword1", "alias_Word1", "alias_Phrase1",
			111, 111L, Instant.parse("2001-01-02T03:04:05.06Z"),
			"misc1", false, new HashSet<>(Set.of("LIKED", "LOVED"))),
		new Person(2L, "#Person2", "IMG-20210725-WA0029 ccc_ddd an CAșț.jpeg",
			"alias_Keyword2", "alias_Word2", "alias_Phrase2",
			222, 222L, Instant.parse("2002-01-02T03:04:05.06Z"),
			"misc2", false, new HashSet<>(Set.of("LIKED", "(LOVED"))),
		new Person(3L,
			"Ping_Pong #name (Original)Person222 ăĂîÎșȘțȚ123",
			"Ping_Pong #name (Original)Person222 ăĂîÎșȘțȚ123",
			"alias_KeywordăĂîÎșȘțȚ123", "alias_WordăĂîÎșȘțȚ123",
			"aliasPhraseăĂîÎșȘțȚ123", 333, 333L,
			Instant.parse("2003-01-02T03:04:05.06Z"),
			"misc3", true, new HashSet<>(Set.of("#LIKED", "LOVED", "LIKED=LOVED")))
//		addTags(generateGirl(4), "#LIKED"),
//		addTags(generateGirl(5), "(LOVED")
//		addTags(generateGirl(6), "TEST=DATA"),
//		addTags(generateGirl(7), "DATA")
	);
	private static final Instant MOMENT = Instant.parse("2000-01-01T03:04:05.32Z");

	public static List<Person> generatePeopleList(long size) {
		return LongStream.range(0, size)
			.mapToObj(i -> generatePerson(i, i % 2 == 0))
			.toList();
	}

	public static Person generateBoy(long id) {
		return generatePerson(id, true);
	}

	public static Person generateGirl(long id) {
		return generatePerson(id, false);
	}

	public static Person generateGirl(long id, Instant instantField) {
		return generatePerson(id, instantField, false);
	}

	public static Person generatePerson(long id, boolean isBoy) {
		return generatePerson(id, MOMENT.plus(id, ChronoUnit.SECONDS), isBoy);
	}

	public static Person generatePerson(long id, Instant instantField, boolean isBoy) {
		return new Person(id,
			"#Person" + id, generateName(id),
			"alias_Keyword" + (id % 2), "alias_Word" + (id % 2),
			"alias_Phrase" + (id % 2), (int) id, id,
			instantField, "storedOnlyField" + (id % 100),
			isBoy, new HashSet<>());
	}

	public static String generateName(long id) {
		return TestData.TEXT + " nameșț" + id;
	}
}
