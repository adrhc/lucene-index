package ro.go.adrhc.persistence.lucene.typedindex.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;

import java.util.Optional;

import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;

@RequiredArgsConstructor
@Slf4j
public class DocumentToTypedConverter<T> {
	private final ObjectReader tReader;

	public static <T> DocumentToTypedConverter<T> of(Class<T> tClass) {
		return new DocumentToTypedConverter<>(json().build().readerFor(tClass));
	}

	public Optional<T> convert(Document doc) {
		String json = TypedToDocumentConverter.getRawData(doc);
		try {
			return Optional.of(tReader.readValue(json));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
			return Optional.empty();
		}
	}
}
