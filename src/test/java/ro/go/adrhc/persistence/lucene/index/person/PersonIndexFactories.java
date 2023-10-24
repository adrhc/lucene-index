package ro.go.adrhc.persistence.lucene.index.person;

import org.apache.lucene.document.Document;
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
import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.index.IndexTestFactories.*;
import static ro.go.adrhc.persistence.lucene.index.domain.field.FieldFactory.storedAndAnalyzed;
import static ro.go.adrhc.persistence.lucene.index.domain.field.FieldFactory.storedButNotAnalyzed;

public class PersonIndexFactories {
	public static IndexSearchService<String, Person> createSearchService(
			Function<String, Query> stringQueryConverter, Path indexPath) {
		return createIndexSearchService(
				SearchedToQueryConverterFactory.of(stringQueryConverter),
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
		document.add(storedButNotAnalyzed(PersonFields.id, person.id()));
		document.add(storedAndAnalyzed(PersonFields.name, person.name()));
		return document;
	}
}
