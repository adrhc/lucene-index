package ro.go.adrhc.persistence.lucene.typedcore.serde;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedFieldsProvider;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedFieldsProviderParams;

import java.util.Optional;

@RequiredArgsConstructor
public class TypedToDocumentConverter<T extends Identifiable<?>> {
	private final TypedFieldsProvider<T> typedFieldsProvider;
	private final RawDataFieldProvider<T> rawDataFieldProvider;

	public static <T extends Identifiable<?>, E extends Enum<E> & TypedField<T>>
	TypedToDocumentConverter<T> create(TypedFieldsProviderParams<T, E> providerParams) {
		TypedFieldsProvider<T> typedFieldsProvider = TypedFieldsProvider.create(providerParams);
		return new TypedToDocumentConverter<>(typedFieldsProvider, RawDataFieldProvider.create());
	}

	@NonNull
	public Optional<Document> convert(T tValue) {
		if (tValue == null || !tValue.hasId()) {
			return Optional.empty();
		}

		Document doc = new Document();

		Optional<Field> rawField = rawDataFieldProvider.createField(tValue);
		if (rawField.isEmpty()) {
			return Optional.empty();
		}
		doc.add(rawField.get());

		typedFieldsProvider.createFields(tValue).forEach(doc::add);

		return Optional.of(doc);
	}
}