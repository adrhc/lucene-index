package ro.go.adrhc.persistence.lucene.person;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import ro.go.adrhc.persistence.lucene.LuceneIndex;
import ro.go.adrhc.persistence.lucene.LuceneIndexImpl;
import ro.go.adrhc.persistence.lucene.core.typed.read.HitsLimitedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.core.typed.read.OneHitIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.operations.params.IndexServicesParamsFactory;

import java.io.IOException;
import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.IndexServicesParamsFactoryTestFactory.createFSTypedIndexSpec;
import static ro.go.adrhc.persistence.lucene.person.PeopleGenerator.PEOPLE;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractPersonTmpIndexTest {
	@TempDir
	protected static Path tmpDir;
	protected IndexServicesParamsFactory<Person> peopleIndexSpec;
	protected LuceneIndex<Long, Person> index;

	protected void initObjects() {
		peopleIndexSpec = createFSTypedIndexSpec(Person.class, PersonSchema.class, tmpDir);
		index = LuceneIndexImpl.of(peopleIndexSpec);
	}

	protected HitsLimitedIndexReaderTemplate<Long, Person> createHitsLimitedIndexReaderTemplate() {
		return HitsLimitedIndexReaderTemplate.create(
			peopleIndexSpec.allHitsIndexReaderParams());
	}

	protected OneHitIndexReaderTemplate<Person> createOneHitIndexReaderTemplate() {
		return OneHitIndexReaderTemplate.create(peopleIndexSpec.oneHitIndexReaderParams());
	}

	protected void indexReset() throws IOException {
		index.reset(PEOPLE);
	}

	@BeforeAll
	void beforeAll() throws IOException {
		initObjects();
		indexReset();
	}

	@AfterAll
	void afterAll() throws IOException {
		peopleIndexSpec.close();
	}
}
