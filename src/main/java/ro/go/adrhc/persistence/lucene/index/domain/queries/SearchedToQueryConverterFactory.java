package ro.go.adrhc.persistence.lucene.index.domain.queries;

import com.rainerhahnekamp.sneakythrow.functional.SneakyFunction;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;

import java.util.Optional;
import java.util.function.Function;

import static ro.go.adrhc.util.fn.FunctionUtils.toOptionalResult;

@Slf4j
@UtilityClass
public class SearchedToQueryConverterFactory {
	public static <S, E extends Exception> SearchedToQueryConverter<S> ofSneaky(SneakyFunction<S, Query, E> converter) {
		return s -> {
			try {
				return Optional.ofNullable(converter.apply(s));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			return Optional.empty();
		};
	}

	public static <S> SearchedToQueryConverter<S> of(Function<S, Query> converter) {
		return s -> toOptionalResult(converter).apply(s);
	}
}
