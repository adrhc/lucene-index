package ro.go.adrhc.persistence.lucene.typedcore.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.nio.file.Path;

public class ObjectMapperFactory {
	public static ObjectMapper JSON_MAPPER = createJsonMapper();

	public static ObjectMapper createJsonMapper() {
		return Jackson2ObjectMapperBuilder.json()
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.modulesToInstall(createPathToStringModule())
				.build();
	}

	private static SimpleModule createPathToStringModule() {
		SimpleModule simpleModule = new SimpleModule("PathToString");
		simpleModule.addSerializer(Path.class, new ToStringSerializer());
		return simpleModule;
	}
}
