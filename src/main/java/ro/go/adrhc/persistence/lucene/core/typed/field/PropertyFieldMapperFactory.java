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
	public static <T, P> PropertyFieldMapper<T, P, String, String> stringMapper(
		Function<T, P> propertyAccessor, Function<String, P> indexedValueToPropertyValueConverter) {
		return new PropertyFieldMapper<>(propertyAccessor, PropertyFieldMapperFactory::toString,
			IndexableField::stringValue, indexedValueToPropertyValueConverter);
	}

	public static <T> PropertyFieldMapper<T, String, String, String>
	stringMapper(Function<T, String> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor,
			it -> it, IndexableField::stringValue, it -> it);
	}

	public static <T> PropertyFieldMapper<T, URI, String, String> uriMapper(
		Function<T, URI> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor, PropertyFieldMapperFactory::toString,
			IndexableField::stringValue, PropertyFieldMapperFactory::toURI);
	}

	public static <T> PropertyFieldMapper<T, Boolean, Integer, Integer> booleanMapper(
		Function<T, Boolean> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor, it -> it != null && it ? 1 : 0,
			PropertyFieldMapperFactory::getIntValue, it -> it != null && it != 0);
	}

	public static <T> PropertyFieldMapper<T, Integer, Integer, Integer> intMapper(
		Function<T, Integer> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor, it -> it,
			PropertyFieldMapperFactory::getIntValue, it -> it);
	}

	public static <T> PropertyFieldMapper<T, Long, Long, Long>
	longMapper(Function<T, Long> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor, it -> it,
			PropertyFieldMapperFactory::getLongValue, it -> it);
	}

	public static <T> PropertyFieldMapper<T, Instant, Long, Long>
	instantMapper(Function<T, Instant> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor,
			it -> it == null ? null : it.toEpochMilli(),
			PropertyFieldMapperFactory::getLongValue, it -> ofEpochMilli(it));
	}

	public static <T> PropertyFieldMapper<T, Path, String, String>
	pathMapper(Function<T, Path> propertyAccessor) {
		return stringMapper(propertyAccessor, s -> s == null ? null : Path.of(s));
	}

	/**
	 * See also TypedIndexReader.getFieldValues!
	 */
	public static <T> PropertyFieldMapper<T, Set<String>, Set<String>, String>
	stringSetMapper(Function<T, Set<String>> propertyAccessor) {
		return new PropertyFieldMapper<>(propertyAccessor,
			it -> it, IndexableField::stringValue,
			it -> it == null ? null : Set.of(it));
	}

	public static <T, E extends Enum<E>> PropertyFieldMapper<T, Enum<E>, String, String>
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
