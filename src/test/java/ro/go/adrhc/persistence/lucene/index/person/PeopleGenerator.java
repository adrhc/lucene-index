package ro.go.adrhc.persistence.lucene.index.person;

import ro.go.adrhc.persistence.lucene.index.core.TokenizationUtilsTest;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PeopleGenerator {
	public static final List<Person> PEOPLE = List.of(
			new Person("1", 111, "#Person1", TokenizationUtilsTest.TEXT, "pers1", "pers1", "pers1"),
			new Person("2", 222, "#Person2", "IMG-20210725-WA0029 ccc_ddd CAșț.jpeg", "pers2", "pers2", "pers2"),
			new Person("3", 333, "#Person3", "(Original)person222 CAșț", "pers3", "pers3", "pers3"));

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
		return new Person(String.valueOf(i), i,
				"#Person" + i, TokenizationUtilsTest.TEXT + " nameșț" + i,
				"alias" + (i % 2), "alias" + (i % 2), "alias" + (i % 2));
	}
}
