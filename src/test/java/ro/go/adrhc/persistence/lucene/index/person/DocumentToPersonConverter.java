package ro.go.adrhc.persistence.lucene.index.person;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import ro.go.adrhc.persistence.lucene.typedindex.core.rawtodoc.TypedDataToDocumentConverter;

@RequiredArgsConstructor
public class DocumentToPersonConverter {
	private static final ObjectReader PERSON_READER =
			Jackson2ObjectMapperBuilder.json().build().readerFor(Person.class);

	public Person convert(Document doc) throws JsonProcessingException {
		String json = TypedDataToDocumentConverter.getRawData(doc);
		return PERSON_READER.readValue(json);
	}
}
