package ro.go.adrhc.persistence.lucene.core.read.storedfieldvisitor;

import org.apache.lucene.index.FieldInfo;

public class OneStoredStringFieldVisitor extends AbstractOneStoredFieldVisitor<String> {
	public OneStoredStringFieldVisitor(String name) {
		super(name);
	}

	/**
	 * Process a string field.
	 */
	public void stringField(FieldInfo fieldInfo, String value) {
		this.done = true;
		this.value = value;
	}
}
