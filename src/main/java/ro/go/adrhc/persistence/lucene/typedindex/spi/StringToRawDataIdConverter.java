package ro.go.adrhc.persistence.lucene.typedindex.spi;

import lombok.NonNull;

import java.util.Optional;
import java.util.function.Function;

import static ro.go.adrhc.util.fn.FunctionUtils.toOptionalResult;

public interface StringToRawDataIdConverter<ID> {
	static <ID> StringToRawDataIdConverter<ID> of(Function<String, ID> converter) {
		return docid -> toOptionalResult(converter).apply(docid);
	}

	@NonNull Optional<ID> convert(String id);
}
