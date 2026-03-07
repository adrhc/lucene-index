package ro.go.adrhc.persistence.lucene.core.typed.field;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexableField;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Set;
import java.util.function.Function;

import static java.time.Instant.ofEpochMilli;

@UtilityClass
@Slf4j
public class PropertyFieldMapperFactory {
	public static <T, P> PropertyFieldMapper<T, P> stringMapper(
		Function<T, P> propertyAccessor, Function<String, P> indexedValueToPropertyValueConverter) {
		return new PropertyFieldMapper<>(propertyAccessor, PropertyFieldMapperFactory::toString,
			IndexableField::stringValue, indexedValueToPropertyValueConverter);
	}

	public static <T> PropertyFieldMapper<T, String>
	stringMapper(Function<T, String> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor,
			it -> it, IndexableField::stringValue, it -> (String) it);
	}

	public static <T> PropertyFieldMapper<T, URI> uriMapper(Function<T, URI> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor, PropertyFieldMapperFactory::toString,
			IndexableField::stringValue, PropertyFieldMapperFactory::toURI);
	}

	public static <T> PropertyFieldMapper<T, Boolean> booleanMapper(
		Function<T, Boolean> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor, it -> it != null && ((Boolean) it) ? 1 : 0,
			PropertyFieldMapperFactory::getIntValue, it -> it != null && ((Integer) it) != 0);
	}

	public static <T> PropertyFieldMapper<T, Integer> intMapper(
		Function<T, Integer> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor, it -> it,
			PropertyFieldMapperFactory::getIntValue, it -> (Integer) it);
	}

	public static <T> PropertyFieldMapper<T, Long>
	longMapper(Function<T, Long> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor, it -> it,
			PropertyFieldMapperFactory::getLongValue, it -> (Long) it);
	}

	public static <T> PropertyFieldMapper<T, Instant>
	instantMapper(Function<T, Instant> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor,
			it -> it == null ? null : ((Instant) it).toEpochMilli(),
			PropertyFieldMapperFactory::getLongValue, it -> ofEpochMilli((long) it));
	}

	public static <T> PropertyFieldMapper<T, Path>
	pathMapper(Function<T, Path> propertyAccessor) {
		return stringMapper(propertyAccessor, s -> s == null ? null : Path.of(s));
	}

	public static <T> PropertyFieldMapper<T, Set<String>>
	stringSetMapper(Function<T, Set<String>> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor,
			it -> it, IndexableField::stringValue,
			it -> it == null ? null : Set.of((String) it));
	}

	public static <T, E extends Enum<E>> PropertyFieldMapper<T, Enum<E>>
	enumMapper(Class<E> enumClass, Function<T, Enum<E>> propertyAccessor) {
		return stringMapper(propertyAccessor, s -> Enum.valueOf(enumClass, s));
	}

	private static Integer getIntValue(IndexableField field) {
		return field.storedValue().getIntValue();
	}

	private static Long getLongValue(IndexableField field) {
		return field.storedValue().getLongValue();
	}

	private static String toString(Object s) {
		return s == null ? null : s.toString();
	}

	private static URI toURI(Object value) {
		try {
			return value == null ? null : new URI((String) value);
		} catch (URISyntaxException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
