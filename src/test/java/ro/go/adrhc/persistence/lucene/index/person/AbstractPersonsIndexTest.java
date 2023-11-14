package ro.go.adrhc.persistence.lucene.index.person;

import org.apache.lucene.search.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import ro.go.adrhc.persistence.lucene.index.DocumentsCountService;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIdIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedcore.read.TypedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedIndexAdderService;
import ro.go.adrhc.persistence.lucene.typedindex.create.TypedIndexCreateService;
import ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexFactories;
import ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexFactoriesParams;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedIndexUpdateService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static ro.go.adrhc.persistence.lucene.index.TestIndexContext.createTypedIndexSpec;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.PEOPLE;
import static ro.go.adrhc.persistence.lucene.typedindex.restore.IndexDataSourceFactory.createCachedDataSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractPersonsIndexTest {
	@TempDir
	protected static Path tmpDir;
	protected TypedIndexFactoriesParams<Long, Person, PersonFieldType> peopleIndexSpec;
	protected TypedIndexFactories<Long, Person, PersonFieldType> peopleIndexFactories;

	@BeforeAll
	void beforeAll() throws IOException {
		initPeopleIndexFactories();
		createCreateService().createOrReplace(PEOPLE);
	}

	@AfterAll
	void afterAll() throws IOException {
		peopleIndexFactories.close();
	}

	protected int count(Query query) throws IOException {
		return createDocumentsCountService().count(query);
	}

	protected List<Person> findAllMatches(Query query) throws IOException {
		return createSearchService().findAllMatches(query);
	}

	protected TypedIndexSearchService<Person> createSearchService() {
		return peopleIndexFactories.createSearchService();
	}

	protected DocumentsCountService createDocumentsCountService() {
		return peopleIndexFactories.createCountService();
	}

	protected TypedIndexAdderService<Person> createAdderService() {
		return peopleIndexFactories.createAdderService();
	}

	protected TypedIndexUpdateService<Person> createUpdateService() {
		return peopleIndexFactories.createUpdateService();
	}

	protected TypedIndexRemoveService<Long> createIndexRemoveService() {
		return peopleIndexFactories.createRemoveService();
	}

	protected TypedIndexRestoreService<Long, Person> createIndexRestoreService() {
		return peopleIndexFactories.createRestoreService();
	}

	protected TypedSearchByIdService<Long, Person> createSearchByIdService() {
		return peopleIndexFactories.createSearchByIdService();
	}

	protected TypedIndexCreateService<Long, Person> createCreateService() {
		return peopleIndexFactories.createCreateService();
	}

	protected TypedIndexReaderTemplate<Person> createPersonIndexReaderTemplate() {
		return TypedIndexReaderTemplate.create(peopleIndexSpec);
	}

	protected TypedIdIndexReaderTemplate<Long> createPersonIdIndexReaderTemplate() {
		return TypedIdIndexReaderTemplate.create(peopleIndexSpec);
	}

	protected IndexDataSource<Long, Person> createPeopleDataSource() {
		return createCachedDataSource(PEOPLE);
	}

	protected void initPeopleIndexFactories() throws IOException {
		peopleIndexSpec = createTypedIndexSpec(Person.class, PersonFieldType.class, tmpDir);
		peopleIndexFactories = new TypedIndexFactories<>(peopleIndexSpec);
	}
}
