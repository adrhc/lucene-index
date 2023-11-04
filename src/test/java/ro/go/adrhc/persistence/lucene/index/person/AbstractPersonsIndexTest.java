package ro.go.adrhc.persistence.lucene.index.person;

import org.apache.lucene.search.Query;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import ro.go.adrhc.persistence.lucene.index.count.DocumentsCountService;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexCreateService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.QuerySearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.SearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static ro.go.adrhc.persistence.lucene.index.person.PeopleGenerator.PEOPLE;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractPersonsIndexTest {
	@TempDir
	protected static Path tmpDir;

	public static IndexSearchService<QuerySearchResult<Person>> createIndexSearchService() {
		return PersonIndexFactories.createSearchService(tmpDir);
	}

	@BeforeAll
	void beforeAll() throws IOException {
		createCreateService().createOrReplace(PEOPLE);
	}

	protected List<Person> findAllMatches(Query query) throws IOException {
		return createIndexSearchService()
				.findAllMatches(query)
				.stream()
				.map(SearchResult::getFound)
				.toList();
	}

	protected int count(Query query) throws IOException {
		return createDocumentsCountService().count(query);
	}

	protected DocumentsCountService createDocumentsCountService() {
		return DocumentsCountService.create(tmpDir);
	}

	protected TypedIndexUpdateService<Integer, Person> createUpdateService() {
		return PersonIndexFactories.createUpdateService(tmpDir);
	}

	protected TypedSearchByIdService<Integer, Person> createSearchByIdService() {
		return PersonIndexFactories.createSearchByIdService(tmpDir);
	}

	protected TypedIndexCreateService<Person> createCreateService() {
		return PersonIndexFactories.createCreateService(tmpDir);
	}
}
