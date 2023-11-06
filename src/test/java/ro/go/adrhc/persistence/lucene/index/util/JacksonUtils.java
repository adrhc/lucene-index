package ro.go.adrhc.persistence.lucene.index.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import lombok.experimental.UtilityClass;

import java.nio.file.Path;

@UtilityClass
public class JacksonUtils extends JacksonJsonProvider {
	public static ObjectMapper registerPathToString(ObjectMapper mapper) {
		SimpleModule m = new SimpleModule("PathToString");
		m.addSerializer(Path.class, new ToStringSerializer());
		mapper.registerModule(m);
		return mapper;
	}
}
