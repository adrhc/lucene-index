package ro.go.adrhc.persistence.lucene.index.person;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import ro.go.adrhc.persistence.lucene.typedcore.read.OneHitIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedindex.IndexRepository;
import ro.go.adrhc.persistence.lucene.typedindex.IndexRepositoryFactory;
import ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.TypedIndexParamsImpl;

import java.io.IOException;
import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.index.TestIndexContext.createTypedIndexSpec;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.PEOPLE;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractPersonsIndexTest {
	@TempDir
	protected static Path tmpDir;
	protected TypedIndexParamsImpl<Person> peopleIndexSpec;
	protected IndexRepository<Long, Person> indexRepository;

	@BeforeAll
	void beforeAll() throws IOException {
		initObjects();
		indexRepository.reset(PEOPLE);
	}

	@AfterAll
	void afterAll() throws IOException {
		peopleIndexSpec.close();
	}

	protected void initObjects() throws IOException {
		peopleIndexSpec = createTypedIndexSpec(Person.class, PersonFieldType.class, tmpDir);
		indexRepository = IndexRepositoryFactory.create(peopleIndexSpec);
	}

	protected TypedIndexReaderTemplate<Long, Person> createPersonIndexReaderTemplate() {
		return TypedIndexReaderTemplate.create(peopleIndexSpec);
	}

	protected OneHitIndexReaderTemplate<Person> createPersonIdIndexReaderTemplate() {
		return OneHitIndexReaderTemplate.create(peopleIndexSpec);
	}
}
