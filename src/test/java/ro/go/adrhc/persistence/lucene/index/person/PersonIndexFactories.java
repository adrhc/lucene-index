package ro.go.adrhc.persistence.lucene.index.person;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexCreateService;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.domain.DocumentsDataSource;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.index.search.SearchedToQueryConverterFactory;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.index.IndexTestFactories.*;
import static ro.go.adrhc.persistence.lucene.typedindex.core.DocumentsDataSourceFactories.createCachedTypedDocsDs;
import static ro.go.adrhc.util.fn.FunctionUtils.sneakyToOptionalResult;

public class PersonIndexFactories {
	private static final Function<Document, Optional<Person>> DOCUMENT_TO_PERSON_CONVERTER =
			sneakyToOptionalResult(new DocumentToPersonConverter()::convert);

	public static IndexSearchService<String, Person> createSearchService(
			SneakyFunction<String, Query, QueryNodeException> stringQueryConverter, Path indexPath) {
		return createIndexSearchService(
				SearchedToQueryConverterFactory.ofSneaky(stringQueryConverter),
				(s, sad) -> DOCUMENT_TO_PERSON_CONVERTER.apply(sad.document()),
				indexPath
		);
	}

	public static FSIndexCreateService createCreateService(
			Collection<Person> people, Path indexPath) {
		return createFSIndexCreateService(createPersonDocsDs(people), PersonFieldType.id, indexPath);
	}

	public static FSIndexUpdateService createUpdateService(Path indexPath) {
		return createFSIndexUpdateService(PersonFieldType.id, indexPath);
	}

	private static DocumentsDataSource createPersonDocsDs(Collection<Person> persons) {
		return createCachedTypedDocsDs(ANALYZER, PersonFieldType.class, persons);
	}
}
