package ro.go.adrhc.persistence.lucene.index.domain.field;

import lombok.experimental.UtilityClass;
import org.apache.lucene.document.Document;

@UtilityClass
public class FieldAccessors {
	public static Number numericValue(Enum<?> field, Document doc) {
		return doc.getField(field.name()).numericValue();
	}
}
