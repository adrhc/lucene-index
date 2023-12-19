package ro.go.adrhc.persistence.lucene.typedcore.serde;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedFieldsProvider;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedFieldsProviderParams;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class TypedToDocumentConverter<T> {
    private final TypedFieldsProvider<T> typedFieldsProvider;
    private final RawDataFieldProvider<T> rawDataFieldProvider;

    public static <T> TypedToDocumentConverter<T> create(TypedFieldsProviderParams<T> params) {
        TypedFieldsProvider<T> typedFieldsProvider = TypedFieldsProvider.create(params);
        return new TypedToDocumentConverter<>(typedFieldsProvider, RawDataFieldProvider.create());
    }

    @NonNull
    public Optional<Document> convert(T tValue) {
        if (tValue == null) {
            log.error("\nCan't add NULL!");
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
