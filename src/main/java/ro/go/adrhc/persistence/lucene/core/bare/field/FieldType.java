package ro.go.adrhc.persistence.lucene.core.bare.field;

public enum FieldType {
	KEYWORD, // indexed as a single token, NOT normalized!
	WORD, // indexed as a single token, normalized (aka, char-filtered)
	PHRASE, // indexed as a many tokens, normalized
	TAGS, // same as PHRASE
	LONG, // used to for Long and Instant
	INT,
	STORED
}
