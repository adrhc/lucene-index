package ro.go.adrhc.persistence.lucene.index.person;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexCreateService;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.domain.DocumentsDataSource;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.index.search.SearchedToQueryConverterFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

import static ro.go.adrhc.persistence.lucene.index.IndexTestFactories.*;
import static ro.go.adrhc.persistence.lucene.typedindex.core.DocumentsDataSourceFactories.createCachedDocsDs;

public class PersonIndexFactories {
	private static final PersonToDocumentConverter PERSON_TO_DOCUMENT_CONVERTER
			= new PersonToDocumentConverter(FIELD_FACTORY);

	public static IndexSearchService<String, PersonSearchResult> createSearchService(
			SneakyFunction<String, Query, QueryNodeException> stringQueryConverter, Path indexPath) {
		return createIndexSearchService(
				SearchedToQueryConverterFactory.ofSneaky(stringQueryConverter),
				(s, sad) -> PersonSearchResult.of(sad), indexPath
		);
	}

	public static FSIndexCreateService createCreateService(
			Collection<Person> people, Path indexPath) throws IOException {
		return createFSIndexCreateService(createPersonDocsDs(people), PersonFields.id, indexPath);
	}

	public static FSIndexUpdateService createUpdateService(Path indexPath) throws IOException {
		return createFSIndexUpdateService(PersonFields.id, indexPath);
	}

	private static DocumentsDataSource createPersonDocsDs(Collection<Person> persons) {
		return createCachedDocsDs(PERSON_TO_DOCUMENT_CONVERTER::convert, persons);
	}
}
