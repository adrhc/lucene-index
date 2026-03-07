package ro.go.adrhc.persistence.lucene.core.typed.write;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import ro.go.adrhc.persistence.lucene.core.bare.field.LuceneFieldApplier;

import java.util.Optional;
import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.core.bare.field.FieldFactory.storedField;

@RequiredArgsConstructor
@Slf4j
public class TypedToDocumentConverter<T> {
	public static final String RAW_FIELD = "raw";
	private final Function<T, Optional<String>> serializer;
	private final LuceneFieldApplier<T> luceneFieldApplier;

	public static <T> TypedToDocumentConverter<T> create(
		TypedToDocumentConverterParams<T> params) {
		return new TypedToDocumentConverter<>(
			params.rawFieldValueSerdes()::serialize,
			new LuceneFieldApplier<>(params.typedFields()));
	}

	@NonNull
	public Optional<Document> convert(T tValue) {
		if (tValue == null) {
			log.error("\nCan't add NULL!");
			return Optional.empty();
		}

		Optional<Field> rawField = serializer.apply(tValue)
			.map(raw -> storedField(RAW_FIELD, raw));
		if (rawField.isEmpty()) {
			log.error("\nCan't create raw field for value: {}", tValue);
			return Optional.empty();
		}

		Document doc = new Document();
		doc.add(rawField.get());
		luceneFieldApplier.addFields(tValue, doc);
		return Optional.of(doc);
	}
}
