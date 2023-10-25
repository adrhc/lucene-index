package ro.go.adrhc.persistence.lucene.index.core.docds.rawidserde;

import java.util.Optional;

public interface RawIdSerde<ID> {
	static RawIdSerde<String> createStringRawIdSerde() {
		return new RawIdSerde<>() {
			@Override
			public RawIdToStringConverter<String> getRawIdToStringConverter() {
				return RawIdToStringConverter.of(it -> it);
			}

			@Override
			public StringToRawIdConverter<String> getStringToRawIdConverter() {
				return StringToRawIdConverter.of(it -> it);
			}
		};
	}

	RawIdToStringConverter<ID> getRawIdToStringConverter();

	StringToRawIdConverter<ID> getStringToRawIdConverter();

	default Optional<ID> toId(String id) {
		return getStringToRawIdConverter().convert(id);
	}

	default Optional<String> toString(ID id) {
		return getRawIdToStringConverter().convert(id);
	}
}
