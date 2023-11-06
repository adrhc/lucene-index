package ro.go.adrhc.persistence.lucene.typedindex.domain.docserde;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.RawDataFieldProvider;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedFieldsProvider;

import java.util.Optional;

@RequiredArgsConstructor
public class TypedToDocumentConverter<T extends Identifiable<?>> {
	private final TypedFieldsProvider<T> typedFieldsProvider;
	private final RawDataFieldProvider<T> rawDataFieldProvider;

	public static <T extends Identifiable<?>, E extends Enum<E> & TypedField<T>>
	TypedToDocumentConverter<T> create(Analyzer analyzer, Class<E> typedFieldEnumClass) {
		TypedFieldsProvider<T> typedFieldsProvider =
				TypedFieldsProvider.create(analyzer, typedFieldEnumClass);
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
