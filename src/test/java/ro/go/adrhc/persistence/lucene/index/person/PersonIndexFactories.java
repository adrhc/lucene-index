package ro.go.adrhc.persistence.lucene.index.person;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import org.apache.lucene.document.*;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexCreateService;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.index.search.SearchedToQueryConverterFactory;
import ro.go.adrhc.persistence.lucene.index.spi.DocumentsDatasource;
import ro.go.adrhc.persistence.lucene.typedindex.core.DefaultDocumentsDatasource;
import ro.go.adrhc.persistence.lucene.typedindex.spi.RawDataIdToStringConverter;
import ro.go.adrhc.persistence.lucene.typedindex.spi.RawDataToDocumentConverter;
import ro.go.adrhc.persistence.lucene.typedindex.spi.StringToRawDataIdConverter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

import static ro.go.adrhc.persistence.lucene.index.IndexTestFactories.*;

public class PersonIndexFactories {
	public static IndexSearchService<String, Person> createSearchService(
			SneakyFunction<String, Query, QueryNodeException> stringQueryConverter, Path indexPath) {
		return createIndexSearchService(
				SearchedToQueryConverterFactory.ofSneaky(stringQueryConverter),
				(s, sad) -> Person.of(sad),
				indexPath
		);
	}


	public static FSIndexCreateService createCreateService(
			Collection<Person> personsDatasource, Path indexPath) throws IOException {
		return createFSIndexCreateService(
				createPersonDocumentsDatasource(personsDatasource), PersonFields.id, indexPath);
	}

	public static FSIndexUpdateService createUpdateService(Path indexPath) throws IOException {
		return createFSIndexUpdateService(PersonFields.id, indexPath);
	}

	private static DocumentsDatasource createPersonDocumentsDatasource(Collection<Person> persons) {
		return new DefaultDocumentsDatasource<>(
				createDatasource(Person::id, persons),
				RawDataIdToStringConverter.of(it -> it),
				StringToRawDataIdConverter.of(it -> it),
				RawDataToDocumentConverter.of(PersonIndexFactories::personToDocument)
		);
	}

	private static Document personToDocument(Person person) {
		Document document = new Document();
		document.add(new StringField(PersonFields.id.name(), person.id(), Field.Store.YES));
		document.add(new TextField(PersonFields.name.name(), person.name(), Field.Store.YES));
		document.add(new KeywordField(PersonFields.cnp.name(), person.cnp(), Field.Store.YES));
		return document;
	}
}
