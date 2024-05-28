package ro.go.adrhc.persistence.lucene.core.read.storedfieldvisitor;

import org.apache.lucene.index.FieldInfo;

public class OneStoredIntegerFieldVisitor extends AbstractOneStoredFieldVisitor<Integer> {
	public OneStoredIntegerFieldVisitor(String name) {
		super(name);
	}

	/**
	 * Process an int numeric field.
	 */
	public void intField(FieldInfo fieldInfo, int value) {
		this.done = true;
		this.value = value;
	}
}
