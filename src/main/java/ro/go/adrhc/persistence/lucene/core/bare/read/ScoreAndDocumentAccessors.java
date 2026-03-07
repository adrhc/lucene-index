package ro.go.adrhc.persistence.lucene.core.bare.read;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ScoreAndDocumentAccessors {
	public static String fieldValue(ScoreAndDocument scoreAndDocument, Enum<?> field) {
		return scoreAndDocument.document().get(field.name());
	}
}
