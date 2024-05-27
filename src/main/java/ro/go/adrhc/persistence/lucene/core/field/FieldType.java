package ro.go.adrhc.persistence.lucene.core.field;

public enum FieldType {
	KEYWORD, // indexed as a single token but NOT normalized!
	WORD, // indexed as a single token; normalized (aka, filtered) before indexing
	PHRASE,
	LONG,
	INT,
	STORED
}
