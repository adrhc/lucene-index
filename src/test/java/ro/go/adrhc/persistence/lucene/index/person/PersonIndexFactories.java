package ro.go.adrhc.persistence.lucene.index.person;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexCreateService;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.core.docds.datasource.DocumentsDataSource;
import ro.go.adrhc.persistence.lucene.index.domain.queries.FieldQueries;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import static ro.go.adrhc.persistence.lucene.index.IndexTestFactories.*;
import static ro.go.adrhc.persistence.lucene.index.search.SearchedToQueryConverterFactory.ofSneaky;
import static ro.go.adrhc.persistence.lucene.typedindex.core.DocsDataSourceFactory.createCachedTypedDs;

public class PersonIndexFactories {
	public static final FieldQueries NAME_AS_WORD_QUERIES =
			createFieldQuery(PersonFieldType.nameAsWord);
	public static final FieldQueries NAME_QUERIES = createFieldQuery(PersonFieldType.name);
	public static final FieldQueries CNP_QUERIES = createFieldQuery(PersonFieldType.cnp);

	public static List<Person> findAllMatches(Path indexPath, Query query) throws IOException {
		return PersonIndexFactories
				.createSearchService(indexPath)
				.findAllMatches(query)
				.stream().map(TypedSearchResult::getFound)
				.toList();
	}

	public static List<Person> findAllMatches(Path indexPath,
			SneakyFunction<String, Query, QueryNodeException> searchedToQueryConverter,
			String textToSearch) throws IOException {
		return PersonIndexFactories
				.createSearchService(searchedToQueryConverter, indexPath)
				.findAllMatches(textToSearch)
				.stream().map(TypedSearchResult::getFound)
				.toList();
	}

	public static IndexSearchService<Query, TypedSearchResult<Query, Person>>
	createSearchService(Path indexPath) {
		return createTypedFSIndexSearchService(Person.class, indexPath);
	}

	public static IndexSearchService<String, TypedSearchResult<String, Person>>
	createSearchService(
			SneakyFunction<String, Query, QueryNodeException> searchedToQueryConverter,
			Path indexPath) {
		return createTypedFSIndexSearchService(Person.class,
				ofSneaky(searchedToQueryConverter), indexPath);
	}

	public static FSIndexCreateService createCreateService(
			Collection<Person> people, Path indexPath) {
		return createTypedIndexFactories(Person.class)
				.createFSIndexCreateService(createDocsDs(people), indexPath);
	}

	public static FSIndexUpdateService createUpdateService(Path indexPath) {
		return createTypedIndexFactories(Person.class)
				.createFSIndexUpdateService(PersonFieldType.id, indexPath);
	}

	private static DocumentsDataSource createDocsDs(Collection<Person> persons) {
		return createCachedTypedDs(ANALYZER, PersonFieldType.class, persons);
	}
}
