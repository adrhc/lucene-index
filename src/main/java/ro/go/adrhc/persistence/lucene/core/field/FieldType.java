package ro.go.adrhc.persistence.lucene.core.field;

public enum FieldType {
	KEYWORD, // indexed as a single token, NOT normalized!
	WORD, // indexed as a single token, normalized (aka, filtered)
	PHRASE, // indexed as a many tokens, normalized
	LONG,
	INT,
	STORED
}
