package ro.go.adrhc.persistence.lucene.person;

import org.apache.lucene.analysis.Analyzer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import ro.go.adrhc.persistence.lucene.FileSystemDocTypedIndex;
import ro.go.adrhc.persistence.lucene.FileSystemDocTypedIndexImpl;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.core.typed.read.OneHitIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.operations.params.IndexServicesParamsFactory;

import java.io.IOException;

import static ro.go.adrhc.persistence.lucene.TypedIndexParamsTestFactory.createRAMTypedIndexSpec;
import static ro.go.adrhc.persistence.lucene.person.PeopleGenerator.generatePeopleList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractPersonRAMIndexTest {
	protected IndexServicesParamsFactory<Person> peopleIndexSpec;
	protected FileSystemDocTypedIndex<Long, Person> index;

	protected Analyzer analyzer() {
		return peopleIndexSpec.analyzer();
	}

	protected OneHitIndexReaderTemplate<Person> createOneHitIndexReaderTemplate() {
		return OneHitIndexReaderTemplate.create(peopleIndexSpec.oneHitIndexReaderParams());
	}

	protected HitsLimitedIndexReaderTemplate<Long, Person> createHitsLimitedIndexReaderTemplate() {
		return HitsLimitedIndexReaderTemplate.create(peopleIndexSpec.allHitsIndexReaderParams());
	}

	protected void indexReset() throws IOException {
		index.reset(generatePeopleList(peopleListSize()));
	}

	protected int peopleListSize() {
		return 30;
	}

	@BeforeAll
	void beforeAll() throws IOException {
		peopleIndexSpec = createRAMTypedIndexSpec(Person.class, PersonSchema.class);
		index = FileSystemDocTypedIndexImpl.of(peopleIndexSpec);
		indexReset();
	}

	@AfterAll
	void afterAll() throws IOException {
		peopleIndexSpec.close();
	}
}
