package ro.go.adrhc.persistence.lucene.typedcore.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.experimental.UtilityClass;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.nio.file.Path;

@UtilityClass
public class ObjectMapperFactory {
	public static final ObjectMapper JSON_MAPPER = createJsonMapper();

	public static ObjectMapper createJsonMapper() {
		return Jackson2ObjectMapperBuilder.json()
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.modulesToInstall(createPathToStringModule())
				.build();
	}

	/**
	 * Path is badly (JSON) serialized, see:
	 * <a href="https://stackoverflow.com/questions/40557821/jackson-2-incorrectly-serializing-java-java-nio-file-path">...</a>
	 */
	private static SimpleModule createPathToStringModule() {
		SimpleModule simpleModule = new SimpleModule("PathToString");
		simpleModule.addSerializer(Path.class, new ToStringSerializer());
		return simpleModule;
	}
}
