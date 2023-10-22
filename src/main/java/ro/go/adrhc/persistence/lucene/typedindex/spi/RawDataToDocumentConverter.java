package ro.go.adrhc.persistence.lucene.typedindex.spi;

import lombok.NonNull;
import org.apache.lucene.document.Document;

import java.util.Optional;
import java.util.function.Function;

import static ro.go.adrhc.util.fn.FunctionUtils.toOptionalResult;

public interface RawDataToDocumentConverter<T> {
	static <T> RawDataToDocumentConverter<T> of(Function<T, Document> converter) {
		return t -> toOptionalResult(converter).apply(t);
	}

	@NonNull Optional<Document> convert(T t);
}
