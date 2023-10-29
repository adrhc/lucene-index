package ro.go.adrhc.persistence.lucene.index.person;

import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.index.IndexCreateService;
import ro.go.adrhc.persistence.lucene.index.IndexTestFactories;
import ro.go.adrhc.persistence.lucene.index.domain.queries.FieldQueries;
import ro.go.adrhc.persistence.lucene.index.restore.DSIndexRestoreService;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchCountService;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexFactories;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static ro.go.adrhc.persistence.lucene.index.IndexTestFactories.createFieldQuery;

public class PersonIndexFactories {
	public static final FieldQueries NAME_AS_WORD_QUERIES =
			createFieldQuery(PersonFieldType.nameAsWord);
	public static final FieldQueries NAME_QUERIES = createFieldQuery(PersonFieldType.name);
	public static final FieldQueries ALIAS_PHRASE_QUERIES = createFieldQuery(PersonFieldType.aliasPhrase);
	public static final FieldQueries ALIAS_KEYWORD_QUERIES = createFieldQuery(PersonFieldType.aliasKeyWord);
	public static final FieldQueries ALIAS_WORD_QUERIES = createFieldQuery(PersonFieldType.aliasWord);
	public static final FieldQueries CNP_QUERIES = createFieldQuery(PersonFieldType.cnp);

	public static int count(Path indexPath, Query query) throws IOException {
		return createSearchCountService(indexPath).count(query);
	}

	public static IndexSearchCountService<Query>
	createSearchCountService(Path indexPath) {
		return IndexSearchCountService.create(Optional::of, indexPath);
	}

	public static List<Person> findAllMatches(Path indexPath, Query query) throws IOException {
		return createSearchService(indexPath)
				.findAllMatches(query)
				.stream().map(TypedSearchResult::getFound)
				.toList();
	}

	public static IndexSearchService<Query, TypedSearchResult<Query, Person>>
	createSearchService(Path indexPath) {
		return createTypedIndexFactories()
				.createTypedFSIndexSearchService(Optional::of, indexPath);
	}

	/*public static List<Person> findAllMatches(Path indexPath,
			SneakyFunction<String, Query, QueryNodeException> searchedToQueryConverter,
			String textToSearch) throws IOException {
		return createSearchService(searchedToQueryConverter, indexPath)
				.findAllMatches(textToSearch)
				.stream().map(TypedSearchResult::getFound)
				.toList();
	}*/

	/*public static IndexSearchService<String, TypedSearchResult<String, Person>>
	createSearchService(
			SneakyFunction<String, Query, QueryNodeException> searchedToQueryConverter,
			Path indexPath) {
		return createTypedIndexFactories()
				.createTypedFSIndexSearchService(ofSneaky(searchedToQueryConverter), indexPath);
	}*/

	public static IndexCreateService<Person> createCreateService(Path indexPath) {
		return createTypedIndexFactories().createFSTypedIndexCreateService(indexPath);
	}

	public static TypedIndexUpdateService<String, Person> createUpdateService(Path indexPath) {
		return createTypedIndexFactories().createTypedIndexUpdateService(indexPath);
	}

	public static DSIndexRestoreService createRestoreService(Path indexPath) {
		return createTypedIndexFactories().createDSIndexRestoreService(indexPath);
	}

	private static TypedIndexFactories<String, Person, PersonFieldType> createTypedIndexFactories() {
		return IndexTestFactories.createTypedIndexFactories(Person.class, PersonFieldType.class);
	}
}
