package ro.go.adrhc.persistence.lucene.typedcore.docserde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class ObjectMapperFactory {
	public static ObjectMapper JSON_MAPPER = createJsonMapper();

	public static ObjectMapper createJsonMapper() {
		return Jackson2ObjectMapperBuilder.json()
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.build();
	}
}
