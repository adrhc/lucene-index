package ro.go.adrhc.persistence.lucene.core.typed.field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.nio.file.Path;

public class PathDeserializer extends JsonDeserializer<Path> {
	@Override
	public Path deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
		String path = jsonParser.getText();
		return path == null ? null : Path.of(path);
	}
}
