package ro.go.adrhc.persistence.lucene.index.person;

import org.apache.lucene.search.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import ro.go.adrhc.persistence.lucene.core.read.DocumentsIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.index.count.DocumentsCountService;
import ro.go.adrhc.persistence.lucene.typedindex.*;
import ro.go.adrhc.persistence.lucene.typedindex.core.indexds.IndexDataSource;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.QuerySearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.SearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static ro.go.adrhc.persistence.lucene.index.TestIndexContext.createTypedIndexSpec;
import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.PEOPLE;
import static ro.go.adrhc.persistence.lucene.typedindex.core.indexds.IndexDataSourceFactory.createCachedDataSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractPersonsIndexTest {
	@TempDir
	protected static Path tmpDir;
	protected TypedIndexContext<Long, Person, PersonFieldType> peopleIndexSpec;
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
		return createSearchService()
				.findAllMatches(query)
				.stream()
				.map(SearchResult::getFound)
				.toList();
	}

	protected TypedIndexSearchService<QuerySearchResult<Person>> createSearchService() {
		return peopleIndexFactories.createSearchService();
	}

	protected DocumentsCountService createDocumentsCountService() {
		return peopleIndexFactories.createCountService();
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

	protected TypedIndexCreateService<Person> createCreateService() {
		return peopleIndexFactories.createCreateService();
	}

	protected DocumentsIndexReaderTemplate createDocumentsIndexReaderTemplate() {
		return DocumentsIndexReaderTemplate.create(peopleIndexSpec);
	}

	protected IndexDataSource<Long, Person> createPeopleDataSource() {
		return createCachedDataSource(PEOPLE);
	}

	protected void initPeopleIndexFactories() throws IOException {
		peopleIndexSpec = createTypedIndexSpec(Person.class, PersonFieldType.class, tmpDir);
		peopleIndexFactories = new TypedIndexFactories<>(peopleIndexSpec);
	}
}
