package ro.go.adrhc.persistence.lucene.index.core.docds.rawidserde;

import java.util.Optional;

public interface RawIdSerde<ID> {
	RawIdToStringConverter<ID> rawIdToStringConverter();

	StringToRawIdConverter<ID> stringToRawIdConverter();

	default Optional<ID> toId(String id) {
		return stringToRawIdConverter().convert(id);
	}

	default Optional<String> toString(ID id) {
		return rawIdToStringConverter().convert(id);
	}
}
