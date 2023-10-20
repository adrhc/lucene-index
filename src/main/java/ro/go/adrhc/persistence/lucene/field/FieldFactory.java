package ro.go.adrhc.persistence.lucene.field;

import org.apache.lucene.document.Field;

import java.util.Set;

import static ro.go.adrhc.util.collection.SetUtils.sortThenJoin;

public class FieldFactory {
	public static Field storedButNotAnalyzed(Enum<?> field, Object value) {
		return new Field(field.name(), value.toString(), IndexOptions.STORED_AND_NOT_ANALYZED);
	}

	public static Field storedAndAnalyzed(Enum<?> field, Object value) {
		return new Field(field.name(), value.toString(), IndexOptions.STORED_AND_ANALYZED);
	}

	public static Field storedAndAnalyzed(Enum<?> field, Set<String> values) {
		return new Field(field.name(), sortThenJoin(values), IndexOptions.STORED_AND_ANALYZED);
	}
}
