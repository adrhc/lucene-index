package ro.go.adrhc.persistence.lucene.person;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import ro.go.adrhc.persistence.lucene.FileSystemDocIndex;
import ro.go.adrhc.persistence.lucene.FileSystemDocIndexImpl;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.core.typed.read.OneHitIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.operations.params.IndexServicesParamsFactory;

import java.io.IOException;
import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.TypedIndexParamsTestFactory.createTypedIndexSpec;
import static ro.go.adrhc.persistence.lucene.person.PeopleGenerator.PEOPLE;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractPersonsIndexTest {
	@TempDir
	protected static Path tmpDir;
	protected IndexServicesParamsFactory<Person> peopleIndexSpec;
	protected FileSystemDocIndex<Long, Person> indexRepository;

	protected void initObjects() {
		peopleIndexSpec = createTypedIndexSpec(Person.class, PersonSchema.class, tmpDir);
		indexRepository = FileSystemDocIndexImpl.of(peopleIndexSpec);
	}

	protected HitsLimitedIndexReaderTemplate<Long, Person> createPersonIndexReaderTemplate() {
		return HitsLimitedIndexReaderTemplate.create(
			peopleIndexSpec.allHitsTypedIndexReaderParams());
	}

	protected OneHitIndexReaderTemplate<Person> createPersonIdIndexReaderTemplate() {
		return OneHitIndexReaderTemplate.create(peopleIndexSpec.oneHitIndexReaderParams());
	}

	protected void indexRepositoryReset() throws IOException {
		indexRepository.reset(PEOPLE);
	}

	@BeforeAll
	void beforeAll() throws IOException {
		initObjects();
		indexRepositoryReset();
	}

	@AfterAll
	void afterAll() throws IOException {
		peopleIndexSpec.close();
	}
}
