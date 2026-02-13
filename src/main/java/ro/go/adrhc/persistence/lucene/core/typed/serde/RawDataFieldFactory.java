package ro.go.adrhc.persistence.lucene.core.typed.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import java.util.Optional;
import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.core.bare.field.FieldFactory.storedField;
import static ro.go.adrhc.util.fn.FunctionFactory.emptyFailResultFn;

@RequiredArgsConstructor
public class RawDataFieldFactory<T> {

	private static final String RAW_DATA_FIELD = "raw";

	private final Function<T, Optional<String>> rawStringifier;

	public static <T> RawDataFieldFactory<T> create() {
		ObjectMapper jsonMapper = ObjectMapperFactory.createJsonMapper();
		Function<T, Optional<String>> rawStringifier =
			emptyFailResultFn(jsonMapper::writeValueAsString);
		return new RawDataFieldFactory<>(rawStringifier);
	}

	public static String getRawData(Document doc) {
		return doc.get(RAW_DATA_FIELD);
	}

	public Optional<Field> createField(T tValue) {
		return rawStringifier.apply(tValue)
			.map(json -> storedField(RAW_DATA_FIELD, json));
	}
}
