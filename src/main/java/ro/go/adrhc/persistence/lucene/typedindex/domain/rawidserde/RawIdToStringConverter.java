package ro.go.adrhc.persistence.lucene.typedindex.domain.rawidserde;

import lombok.NonNull;

import java.util.Optional;
import java.util.function.Function;

import static ro.go.adrhc.util.fn.FunctionUtils.toOptionalResult;

public interface RawIdToStringConverter<ID> {
	static <ID> RawIdToStringConverter<ID> of(Function<ID, String> converter) {
		return rawId -> toOptionalResult(converter).apply(rawId);
	}

	@NonNull Optional<String> convert(ID id);
}
