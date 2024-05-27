package ro.go.adrhc.persistence.lucene.typedcore.serde;

import java.util.Optional;
import java.util.function.Function;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import static ro.go.adrhc.persistence.lucene.core.field.FieldFactory.storedField;
import static ro.go.adrhc.util.fn.FunctionUtils.sneakyToOptionalResult;

@RequiredArgsConstructor
public class RawDataFieldFactory<T> {

    private static final String RAW_DATA_FIELD = "raw";

    private final Function<T, Optional<String>> rawStringifier;

    public Optional<Field> createField(T tValue) {
        return rawStringifier.apply(tValue)
                .map(json -> storedField(RAW_DATA_FIELD, json));
    }

    public static <T> RawDataFieldFactory<T> create() {
        ObjectMapper jsonMapper = ObjectMapperFactory.createJsonMapper();
        Function<T, Optional<String>> rawStringifier =
                sneakyToOptionalResult(jsonMapper::writeValueAsString);
        return new RawDataFieldFactory<>(rawStringifier);
    }

    public static String getRawData(Document doc) {
        return doc.get(RAW_DATA_FIELD);
    }
}
