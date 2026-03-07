package ro.go.adrhc.persistence.lucene.core.bare.field;

public enum FieldType {
	TEXT, // indexed as a many, normalized, tokens; doesn't support sorting!
	WORD, // indexed as a single token, normalized (i.e., char-filtered)
	KEYWORD, // indexed as a single token, NOT normalized!
	KEYWORD_ARRAY, // same as KEYWORD but with many values per document; doesn't support sorting!
	INT,
	LONG, // used to for Long and Instant
	STORED
}
