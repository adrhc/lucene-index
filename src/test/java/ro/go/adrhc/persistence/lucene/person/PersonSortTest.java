package ro.go.adrhc.persistence.lucene.person;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.LongField;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortedNumericSortField;
import org.junit.jupiter.api.Test;
import ro.go.adrhc.persistence.lucene.core.typed.search.ScoreDocAndValues;

import java.io.IOException;
import java.util.List;

import static org.apache.lucene.search.SortField.Type.INT;
import static org.apache.lucene.search.SortField.Type.LONG;
import static org.apache.lucene.search.SortedNumericSelector.Type.MIN;
import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.person.PeopleGenerator.generateName;
import static ro.go.adrhc.persistence.lucene.person.PeopleGenerator.generatePeopleList;
import static ro.go.adrhc.persistence.lucene.person.PersonSchema.*;

@Slf4j
class PersonSortTest extends AbstractPersonRAMIndexTest {
	@Override
	protected void indexReset() throws IOException {
		index.reset(generatePeopleList(100));
	}

	@Test
	void findIdsSortedByKeyword() throws IOException {
		Sort sort = new Sort(new SortField(cnp.name(), SortField.Type.STRING, true));
		List<Long> result = index.findIds(new MatchAllDocsQuery(), sort);
		assertThat(result).hasSize(100).containsSequence(99L, 98L, 97L);
	}

	@Test
	void findIdsSortedByWord() throws IOException {
		Sort sort = new Sort(new SortField(nameWord.name(), SortField.Type.STRING));
		List<Long> result = index.findIds(new MatchAllDocsQuery(), sort);
		assertThat(result).hasSize(100).containsSequence(0L, 1L, 10L);
	}

	@Test
	void findIdsSortedByStoredOnly() throws IOException {
		Sort sort = new Sort(new SortField(storedOnlyField.name(), SortField.Type.STRING));
		List<Long> result = index.findIds(new MatchAllDocsQuery(), sort);
		assertThat(result).hasSize(100).containsSequence(0L, 1L, 10L);
	}

	@Test
	void findIdsSortedByInt() throws IOException {
		Sort sort = new Sort(new SortField(intField.name(), INT));
		List<Long> result = index.findIds(new MatchAllDocsQuery(), sort);
		assertThat(result).hasSize(100).containsSequence(0L, 1L, 2L);
	}

	@Test
	void findIdsSortedByLong() throws IOException {
		Sort sort = new Sort(new SortField(longField.name(), LONG));
		List<Long> result = index.findIds(new MatchAllDocsQuery(), sort);
		assertThat(result).hasSize(100).containsSequence(0L, 1L, 2L);
	}

	@Test
	void findSortedByKeyword() throws IOException {
		Sort sort = new Sort(new SortField(cnp.name(), SortField.Type.STRING, false));
		ScoreDocAndValues<Person> result = index.findMany(new MatchAllDocsQuery(), sort);
		assertThat(result.values()).hasSize(100).map(Person::cnp)
			.containsSequence("#Person0", "#Person1", "#Person10");
	}

	@Test
	void findSortedByWord() throws IOException {
		Sort sort = new Sort(new SortField(nameWord.name(), SortField.Type.STRING));
		ScoreDocAndValues<Person> result = index.findMany(new MatchAllDocsQuery(), sort);
		assertThat(result.values()).hasSize(100).map(Person::name)
			.containsSequence(generateName(0L), generateName(1L), generateName(10L));
	}

	@Test
	void findSortedByStoredOnly() throws IOException {
		Sort sort = new Sort(new SortField(storedOnlyField.name(), SortField.Type.STRING));
		ScoreDocAndValues<Person> result = index.findMany(new MatchAllDocsQuery(), sort);
		assertThat(result.values()).hasSize(100).map(Person::storedOnlyField)
			.containsSequence("storedOnlyField0", "storedOnlyField1", "storedOnlyField10");
	}

	@Test
	void findSortedByInt() throws IOException {
		Sort sort = new Sort(new SortField(intField.name(), INT));
		ScoreDocAndValues<Person> result = index.findMany(new MatchAllDocsQuery(), sort);
		assertThat(result.values()).hasSize(100).map(Person::intField).containsSequence(0, 1, 2);
	}

	@Test
	void findSortedByLong() throws IOException {
		Sort sort = new Sort(new SortField(longField.name(), LONG));
		ScoreDocAndValues<Person> result = index.findMany(new MatchAllDocsQuery(), sort);
		assertThat(result.values()).hasSize(100).map(Person::longField).containsSequence(0L, 1L, 2L);
	}

	@Test
	void findSortedByInstant() throws IOException {
		// Any of SortField, SortedNumericSortField, or LongField.newSortField can be used!
		Sort sort = new Sort(LongField.newSortField(instantField.name(), false, MIN));
		ScoreDocAndValues<Person> result = index
			.findMany(new MatchAllDocsQuery(), 10, sort);
		assertThat(result.values()).hasSize(10);
		assertThat(result.values()).map(Person::id).containsSequence(0L, 1L, 2L);
	}

	@Test
	void findPages() throws IOException {
		// sort possible with any of SortField and SortedNumericSortField
		Sort instantFieldSort = new Sort(new SortField(instantField.name(), LONG));
		// equivalent of LongField.newSortField(instantField.name(), true, MIN)
		Sort instantFieldReverseSort = new Sort(
			new SortedNumericSortField(instantField.name(), LONG, true));

		// 1st page
		ScoreDocAndValues<Person> page1 = index.findMany(
			new MatchAllDocsQuery(), 10, instantFieldSort);
		assertThat(page1.values()).hasSize(10);
		assertThat(page1.values()).map(Person::id).containsSequence(0L, 1L, 2L);

		// 2nd page
		ScoreDocAndValues<Person> page2 = index.findManyAfter(
			page1.lastPosition(), new MatchAllDocsQuery(), 10, instantFieldSort);
		assertThat(page2.values()).hasSize(10);
		assertThat(page2.values()).map(Person::id).containsSequence(10L, 11L, 12L);

		// back to 1st page
		ScoreDocAndValues<Person> page3 = index.findManyAfter(page2.firstPosition(),
			new MatchAllDocsQuery(), 10, instantFieldReverseSort).reverse();
		assertThat(page3.values()).hasSize(10);
		assertThat(page3.values()).map(Person::id).containsSequence(0L, 1L, 2L);
	}
}
