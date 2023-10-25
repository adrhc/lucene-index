package ro.go.adrhc.persistence.lucene.typedindex.core.rawtodoc;

import lombok.NonNull;
import org.apache.lucene.document.Document;

import java.util.Optional;
import java.util.function.Function;

import static ro.go.adrhc.util.fn.FunctionUtils.toOptionalResult;

public interface RawToDocumentConverter<T> {
	static <T> RawToDocumentConverter<T> of(Function<T, Document> converter) {
		return t -> toOptionalResult(converter).apply(t);
	}

	@NonNull Optional<Document> convert(T t);
}
