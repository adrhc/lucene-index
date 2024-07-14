package ro.go.adrhc.persistence.lucene.core.bare.read.storedfieldvisitor;

import org.apache.lucene.index.FieldInfo;

public class OneStoredLongFieldVisitor extends AbstractOneStoredFieldVisitor<Long> {
	public OneStoredLongFieldVisitor(String name) {
		super(name);
	}

	/**
	 * Process a long numeric field.
	 */
	public void longField(FieldInfo fieldInfo, long value) {
		this.done = true;
		this.value = value;
	}
}
