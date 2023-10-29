package ro.go.adrhc.persistence.lucene.index.person;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexCreateService;
import ro.go.adrhc.persistence.lucene.index.IndexTestFactories;
import ro.go.adrhc.persistence.lucene.index.core.docds.datasource.DocumentsDataSource;
import ro.go.adrhc.persistence.lucene.index.domain.queries.FieldQueries;
import ro.go.adrhc.persistence.lucene.index.restore.DSIndexRestoreService;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexFactories;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ro.go.adrhc.persistence.lucene.index.IndexTestFactories.ANALYZER;
import static ro.go.adrhc.persistence.lucene.index.IndexTestFactories.createFieldQuery;
import static ro.go.adrhc.persistence.lucene.index.search.SearchedToQueryConverterFactory.ofSneaky;
import static ro.go.adrhc.persistence.lucene.typedindex.core.DocsDataSourceFactory.createCachedTypedDs;

public class PersonIndexFactories {
	public static final FieldQueries NAME_AS_WORD_QUERIES =
			createFieldQuery(PersonFieldType.nameAsWord);
	public static final FieldQueries NAME_QUERIES = createFieldQuery(PersonFieldType.name);
	public static final FieldQueries ALIAS_PHRASE_QUERIES = createFieldQuery(PersonFieldType.aliasPhrase);
	public static final FieldQueries ALIAS_KEYWORD_QUERIES = createFieldQuery(PersonFieldType.aliasKeyWord);
	public static final FieldQueries ALIAS_WORD_QUERIES = createFieldQuery(PersonFieldType.aliasWord);
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
		return createTypedIndexFactories()
				.createTypedFSIndexSearchService(Optional::of, indexPath);
	}

	public static IndexSearchService<String, TypedSearchResult<String, Person>>
	createSearchService(
			SneakyFunction<String, Query, QueryNodeException> searchedToQueryConverter,
			Path indexPath) {
		return createTypedIndexFactories()
				.createTypedFSIndexSearchService(ofSneaky(searchedToQueryConverter), indexPath);
	}

	public static FSIndexCreateService createCreateService(
			Collection<Person> people, Path indexPath) {
		return createTypedIndexFactories().createFSIndexCreateService(createDocsDs(people), indexPath);
	}

	public static TypedIndexUpdateService<String, Person> createUpdateService(Path indexPath) {
		return createTypedIndexFactories().createTypedIndexUpdateService(indexPath);
	}

	public static DSIndexRestoreService createRestoreService(
			Collection<Person> persons, Path indexPath) {
		return createTypedIndexFactories().createDSIndexRestoreService(createDocsDs(persons), indexPath);
	}

	private static TypedIndexFactories<String, Person, PersonFieldType> createTypedIndexFactories() {
		return IndexTestFactories.createTypedIndexFactories(Person.class, PersonFieldType.class);
	}

	private static DocumentsDataSource createDocsDs(Collection<Person> persons) {
		return createCachedTypedDs(ANALYZER, PersonFieldType.class, persons);
	}
}
