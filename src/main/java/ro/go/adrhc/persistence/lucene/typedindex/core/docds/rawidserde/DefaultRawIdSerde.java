package ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawidserde;

public record DefaultRawIdSerde<ID>(
		StringToRawIdConverter<ID> stringToRawIdConverter,
		RawIdToStringConverter<ID> rawIdToStringConverter)
		implements RawIdSerde<ID> {
}
