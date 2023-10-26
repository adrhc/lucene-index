package ro.go.adrhc.persistence.lucene.index.core.docds.rawidserde;

import lombok.experimental.UtilityClass;

import java.util.function.Function;

@UtilityClass
public class RawIdSerdeFactory {
	public static final RawIdSerde<String> STRING_RAW_ID_SERDE = of(it -> it, it -> it);

	public static <ID> DefaultRawIdSerde<ID> of(
			Function<String, ID> stringToId, Function<ID, String> idToString) {
		return new DefaultRawIdSerde<>(
				StringToRawIdConverter.of(stringToId),
				RawIdToStringConverter.of(idToString));
	}
}
