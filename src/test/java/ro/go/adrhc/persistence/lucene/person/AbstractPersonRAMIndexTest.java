package ro.go.adrhc.persistence.lucene.person;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import ro.go.adrhc.persistence.lucene.FileSystemDocTypedIndex;
import ro.go.adrhc.persistence.lucene.FileSystemDocTypedIndexImpl;
import ro.go.adrhc.persistence.lucene.operations.params.IndexServicesParamsFactory;

import java.io.IOException;

import static ro.go.adrhc.persistence.lucene.TypedIndexParamsTestFactory.createRAMTypedIndexSpec;
import static ro.go.adrhc.persistence.lucene.person.PeopleGenerator.generatePeopleList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractPersonRAMIndexTest {
	protected IndexServicesParamsFactory<Person> peopleIndexSpec;
	protected FileSystemDocTypedIndex<Long, Person> indexRepository;

	protected void indexRepositoryReset() throws IOException {
		indexRepository.reset(generatePeopleList(peopleListSize()));
	}

	protected int peopleListSize() {
		return 30;
	}

	@BeforeAll
	void beforeAll() throws IOException {
		peopleIndexSpec = createRAMTypedIndexSpec(Person.class, PersonSchema.class);
		indexRepository = FileSystemDocTypedIndexImpl.of(peopleIndexSpec);
		indexRepositoryReset();
	}

	@AfterAll
	void afterAll() throws IOException {
		peopleIndexSpec.close();
	}
}
