package ro.go.adrhc.persistence.lucene.typedcore.serde;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import java.util.Optional;
import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.core.field.FieldFactory.storedField;
import static ro.go.adrhc.persistence.lucene.typedcore.serde.ObjectMapperFactory.JSON_MAPPER;
import static ro.go.adrhc.util.fn.FunctionUtils.sneakyToOptionalResult;

@RequiredArgsConstructor
public class RawDataFieldProvider<T> {
    private static final String RAW_DATA_FIELD = "raw";

    private final Function<T, Optional<String>> rawStringifier;

    public static <T> RawDataFieldProvider<T> create() {
        Function<T, Optional<String>> rawStringifier =
                sneakyToOptionalResult(JSON_MAPPER::writeValueAsString);
        return new RawDataFieldProvider<>(rawStringifier);
    }

    public static String getRawData(Document doc) {
        return doc.get(RAW_DATA_FIELD);
    }

    public Optional<Field> createField(T tValue) {
        return rawStringifier.apply(tValue)
                .map(json -> storedField(RAW_DATA_FIELD, json));
    }
}
