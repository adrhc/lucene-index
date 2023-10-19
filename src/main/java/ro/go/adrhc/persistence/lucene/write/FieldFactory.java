package ro.go.adrhc.persistence.lucene.write;

import org.apache.lucene.document.Field;
import ro.go.adrhc.persistence.lucene.IndexOptions;

public class FieldFactory {
	public static Field storedButNotAnalyzed(Enum<?> field, Object value) {
		return new Field(field.name(), value.toString(), IndexOptions.STORED_AND_NOT_ANALYZED);
	}

	public static Field storedAndAnalyzed(Enum<?> field, Object value) {
		return new Field(field.name(), value.toString(), IndexOptions.STORED_AND_ANALYZED);
	}

	public static Field storedAndAnalyzed(Enum<?> field, int value) {
		return new Field(field.name(), String.valueOf(value), IndexOptions.STORED_AND_ANALYZED);
	}
}
