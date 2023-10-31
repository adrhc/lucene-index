package ro.go.adrhc.persistence.lucene.typedindex.domain.field.spec;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TypedFieldSpecsCollection<T> {
	private final List<TypedFieldSpec<T>> fieldSpecs;

	public static <T, E extends Enum<E> & TypedField<T>>
	TypedFieldSpecsCollection<T> create(Class<E> typedFieldEnumClass) {
		TypedFieldSpecsCollection<T> specCollection = create();
		EnumSet.allOf(typedFieldEnumClass)
				.forEach(e -> specCollection.add(TypedFieldSpec.of(e)));
		return specCollection;
	}

	public static <T> TypedFieldSpecsCollection<T> create() {
		return new TypedFieldSpecsCollection<>(new ArrayList<>());
	}

	public boolean add(TypedFieldSpec<T> field) {
		return fieldSpecs.add(field);
	}

	public <R> Stream<R> map(Function<TypedFieldSpec<T>, ? extends R> mapper) {
		return fieldSpecs.stream().map(mapper);
	}

	public void forEach(Consumer<? super TypedFieldSpec<T>> action) {
		fieldSpecs.forEach(action);
	}
}
