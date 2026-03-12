package ro.go.adrhc.persistence.lucene.person;

import org.apache.lucene.analysis.Analyzer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import ro.go.adrhc.persistence.lucene.LuceneIndex;
import ro.go.adrhc.persistence.lucene.LuceneIndexImpl;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.operation.IndexOperationsParams;

import java.io.IOException;

import static ro.go.adrhc.persistence.lucene.IndexOperationsParamsGenerator.createRAMIndexParams;
import static ro.go.adrhc.persistence.lucene.person.PeopleGenerator.generatePeopleList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractPersonRAMIndexTest {
	protected IndexOperationsParams<Person> peopleIndexSpec;
	protected LuceneIndex<Long, Person> index;

	protected Analyzer analyzer() {
		return peopleIndexSpec.analyzer();
	}

	protected TypedIndexReaderTemplate<Long, Person> typedIndexReaderTemplate() {
		return TypedIndexReaderTemplate.create(peopleIndexSpec.typedIndexReaderParams());
	}

	protected void indexReset() throws IOException {
		index.reset(generatePeopleList(peopleListSize()));
	}

	protected int peopleListSize() {
		return 30;
	}

	@BeforeAll
	void beforeAll() throws IOException {
		peopleIndexSpec = createRAMIndexParams(Person.class, PersonSchema.class);
		index = LuceneIndexImpl.of(peopleIndexSpec);
		indexReset();
	}

	@AfterAll
	void afterAll() throws IOException {
		peopleIndexSpec.close();
	}
}
