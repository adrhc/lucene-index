package ro.go.adrhc.persistence.lucene.core;

import lombok.experimental.UtilityClass;
import org.apache.lucene.document.Document;

@UtilityClass
public class DocumentAccessors {
	public static Number numericValue(Enum<?> field, Document doc) {
		return doc.getField(field.name()).numericValue();
	}
}
