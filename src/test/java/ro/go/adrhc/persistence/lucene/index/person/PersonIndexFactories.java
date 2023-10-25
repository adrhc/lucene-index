package ro.go.adrhc.persistence.lucene.index.person;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexCreateService;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.core.docds.DocumentsDataSource;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchResultFactory;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.index.IndexTestFactories.*;
import static ro.go.adrhc.persistence.lucene.index.search.SearchedToQueryConverterFactory.ofSneaky;
import static ro.go.adrhc.persistence.lucene.typedindex.core.DocsDataSourceFactory.createCachedTypedDocsDs;
import static ro.go.adrhc.util.fn.FunctionUtils.sneakyToOptionalResult;

public class PersonIndexFactories {
	public static IndexSearchService<String, TypedSearchResult<String, Person>> createSearchService(
			SneakyFunction<String, Query, QueryNodeException> stringQueryConverter, Path indexPath) {
		return createIndexSearchService(ofSneaky(stringQueryConverter),
				createIndexSearchResultFactory(), indexPath);
	}

	public static FSIndexCreateService createCreateService(
			Collection<Person> people, Path indexPath) {
		return createFSIndexCreateService(createPersonDocsDs(people), indexPath);
	}

	public static FSIndexUpdateService createUpdateService(Path indexPath) {
		return createFSIndexUpdateService(PersonFieldType.id, indexPath);
	}

	private static TypedSearchResultFactory<String, Person> createIndexSearchResultFactory() {
		return new TypedSearchResultFactory<>(createDocumentToPersonConverter());
	}

	private static Function<Document, Optional<Person>> createDocumentToPersonConverter() {
		return sneakyToOptionalResult(new DocumentToPersonConverter()::convert);
	}

	private static DocumentsDataSource createPersonDocsDs(Collection<Person> persons) {
		return createCachedTypedDocsDs(ANALYZER, PersonFieldType.class, persons);
	}
}
