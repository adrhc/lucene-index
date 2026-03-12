package ro.go.adrhc.persistence.lucene.person;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import ro.go.adrhc.persistence.lucene.LuceneIndex;
import ro.go.adrhc.persistence.lucene.LuceneIndexImpl;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.operation.IndexOperationsParams;

import java.io.IOException;
import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.IndexOperationsParamsGenerator.createFSIndexParams;
import static ro.go.adrhc.persistence.lucene.person.PeopleGenerator.PEOPLE;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractPersonTmpIndexTest {
	@TempDir
	protected static Path tmpDir;
	protected IndexOperationsParams<Person> peopleIndexSpec;
	protected LuceneIndex<Long, Person> index;

	protected void initObjects() {
		peopleIndexSpec = createFSIndexParams(Person.class, PersonSchema.class, tmpDir);
		index = LuceneIndexImpl.of(peopleIndexSpec);
	}

	protected TypedIndexReaderTemplate<Long, Person> typedIndexReaderTemplate() {
		return TypedIndexReaderTemplate.create(peopleIndexSpec.typedIndexReaderParams());
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
