package ro.go.adrhc.persistence.lucene.core.bare.read;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ScoreDocAndDocumentAccessors {
	public static String fieldValue(ScoreDocAndDocument scoreDocAndDocument, Enum<?> field) {
		return scoreDocAndDocument.document().get(field.name());
	}
}
