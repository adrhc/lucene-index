package ro.go.adrhc.persistence.lucene.typedcore.serde;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;

@RequiredArgsConstructor
@Slf4j
public class DocumentToTypedConverter<T> {

    private final ObjectReader tReader;

    public Optional<T> convert(Document doc) {
        String json = RawDataFieldFactory.getRawData(doc);
        try {
            return Optional.of(tReader.readValue(json));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    public static <T> DocumentToTypedConverter<T> create(Class<T> tClass) {
        return new DocumentToTypedConverter<>(ObjectMapperFactory.readerFor(tClass));
    }
}
