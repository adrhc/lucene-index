package ro.go.adrhc.persistence.lucene.index.person;

import ro.go.adrhc.persistence.lucene.index.IndexTestFactories;
import ro.go.adrhc.persistence.lucene.index.domain.queries.FieldQueries;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexCreateService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexFactories;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.TypedIndexUpdateService;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.QuerySearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.restore.DocumentsIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;

import java.nio.file.Path;

public class PersonIndexFactories {
	public static final FieldQueries NAME_WORD_QUERIES = FieldQueries.create(PersonFieldType.nameWord);
	public static final FieldQueries NAME_QUERIES = FieldQueries.create(PersonFieldType.name);
	public static final FieldQueries ALIAS_KEYWORD_QUERIES = FieldQueries.create(PersonFieldType.aliasKeyWord);
	public static final FieldQueries ALIAS_WORD_QUERIES = FieldQueries.create(PersonFieldType.aliasWord);
	public static final FieldQueries ALIAS_PHRASE_QUERIES = FieldQueries.create(PersonFieldType.aliasPhrase);
	public static final FieldQueries CNP_QUERIES = FieldQueries.create(PersonFieldType.cnp);
	public static final FieldQueries ID_QUERIES = FieldQueries.create(PersonFieldType.id);

	/**
	 * BestMatchingStrategy: Stream::findFirst
	 * QuerySearchResultFilter: it -> true (aka no filter)
	 */
	public static IndexSearchService<QuerySearchResult<Person>>
	createSearchService(Path indexPath) {
		return createTypedIndexFactories()
				.createTypedIndexSearchService(it -> true, indexPath);
	}

	public static TypedSearchByIdService<Long, Person> createSearchByIdService(Path indexPath) {
		return createTypedIndexFactories().createSearchByIdService(indexPath);
	}

	public static TypedIndexCreateService<Person> createCreateService(Path indexPath) {
		return createTypedIndexFactories().createTypedIndexCreateService(indexPath);
	}

	public static TypedIndexUpdateService<Person> createUpdateService(Path indexPath) {
		return createTypedIndexFactories().createTypedIndexUpdateService(indexPath);
	}

	public static TypedIndexRemoveService<Long> createIndexRemoveService(Path indexPath) {
		return createTypedIndexFactories().createIndexRemoveService(indexPath);
	}

	public static DocumentsIndexRestoreService<Long, Person> createIndexRestoreService(Path indexPath) {
		return createTypedIndexFactories().createDocumentsIndexRestoreService(indexPath);
	}

	private static TypedIndexFactories<Long, Person, PersonFieldType> createTypedIndexFactories() {
		return IndexTestFactories.createTypedIndexFactories(Person.class, PersonFieldType.class);
	}
}
