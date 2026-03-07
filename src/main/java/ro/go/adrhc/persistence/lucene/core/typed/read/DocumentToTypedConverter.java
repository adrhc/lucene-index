package ro.go.adrhc.persistence.lucene.core.typed.read;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.core.typed.field.RawFieldValueSerdes;

import java.util.Optional;
import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.core.typed.write.TypedToDocumentConverter.RAW_FIELD;

@RequiredArgsConstructor
@Slf4j
public class DocumentToTypedConverter<T> {
	private final Function<String, Optional<T>> deserializer;

	public static <T> DocumentToTypedConverter<T>
	create(RawFieldValueSerdes<T> rawFieldValueSerdes) {
		return new DocumentToTypedConverter<>(rawFieldValueSerdes::deserialize);
	}

	public Optional<T> convert(Document doc) {
		String rawFieldValue = doc.get(RAW_FIELD);
		return deserializer.apply(rawFieldValue);
	}
}
