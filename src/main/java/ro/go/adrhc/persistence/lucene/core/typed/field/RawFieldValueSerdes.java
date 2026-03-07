package ro.go.adrhc.persistence.lucene.core.typed.field;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static ro.go.adrhc.util.fn.FunctionUtils.failToEmpty;

@RequiredArgsConstructor
@Slf4j
public class RawFieldValueSerdes<T> {
	private final SneakyFunction<T, String, ?> serializer;
	private final SneakyFunction<String, T, ?> deserializer;

	public static <T> RawFieldValueSerdes<T> create(Class<T> tClass) {
		ObjectMapper jsonMapper = ObjectMapperFactory.createJsonMapper();
		ObjectReader deserializer = ObjectMapperFactory.readerFor(tClass);
		return new RawFieldValueSerdes<>(jsonMapper::writeValueAsString, deserializer::readValue);
	}

	public Optional<String> serialize(T tValue) {
		return failToEmpty(serializer, tValue);
	}

	public Optional<T> deserialize(String value) {
		return failToEmpty(deserializer, value);
	}
}
