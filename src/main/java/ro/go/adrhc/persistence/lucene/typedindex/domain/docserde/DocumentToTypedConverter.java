package ro.go.adrhc.persistence.lucene.typedindex.domain.docserde;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;

import java.util.Optional;

import static ro.go.adrhc.persistence.lucene.typedindex.core.ObjectMapperFactory.JSON_MAPPER;

@RequiredArgsConstructor
@Slf4j
public class DocumentToTypedConverter<T> {
	private final ObjectReader tReader;

	public static <T> DocumentToTypedConverter<T> of(Class<T> tClass) {
		return new DocumentToTypedConverter<>(JSON_MAPPER.readerFor(tClass));
	}

	public Optional<T> convert(Document doc) {
		String json = RawDataFieldProvider.getRawData(doc);
		try {
			return Optional.of(tReader.readValue(json));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
			return Optional.empty();
		}
	}
}
