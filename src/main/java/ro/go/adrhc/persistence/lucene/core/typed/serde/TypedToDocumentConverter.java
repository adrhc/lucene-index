package ro.go.adrhc.persistence.lucene.core.typed.serde;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import ro.go.adrhc.persistence.lucene.core.typed.field.ObjectPropsToLuceneFieldsConverter;
import ro.go.adrhc.persistence.lucene.core.typed.field.ObjectPropsToLuceneFieldsConverterParams;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class TypedToDocumentConverter<T> {
	private final ObjectPropsToLuceneFieldsConverter<T> typedFieldsProvider;
	private final RawDataFieldFactory<T> rawDataFieldFactory;

	public static <T> TypedToDocumentConverter<T> create(
		ObjectPropsToLuceneFieldsConverterParams<T> params) {
		ObjectPropsToLuceneFieldsConverter<T> typedFieldsProvider = ObjectPropsToLuceneFieldsConverter.create(
			params);
		return new TypedToDocumentConverter<>(typedFieldsProvider, RawDataFieldFactory.create());
	}

	@NonNull
	public Optional<Document> convert(T tValue) {
		if (tValue == null) {
			log.error("\nCan't add NULL!");
			return Optional.empty();
		}

		Document doc = new Document();

		Optional<Field> rawField = rawDataFieldFactory.createField(tValue);
		if (rawField.isEmpty()) {
			return Optional.empty();
		}
		doc.add(rawField.get());

		typedFieldsProvider.toFields(tValue).forEach(doc::add);

		return Optional.of(doc);
	}
}
