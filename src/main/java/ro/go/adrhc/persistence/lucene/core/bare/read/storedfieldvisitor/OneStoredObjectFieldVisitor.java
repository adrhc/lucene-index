package ro.go.adrhc.persistence.lucene.core.bare.read.storedfieldvisitor;

import org.apache.lucene.index.FieldInfo;

public class OneStoredObjectFieldVisitor extends AbstractOneStoredFieldVisitor<Object> {
	public OneStoredObjectFieldVisitor(String name) {
		super(name);
	}

	/**
	 * Process a binary field.
	 *
	 * @param value newly allocated byte array with the binary contents.
	 */
	public void binaryField(FieldInfo fieldInfo, byte[] value) {
		this.done = true;
		this.value = value;
	}

	/**
	 * Process a string field.
	 */
	public void stringField(FieldInfo fieldInfo, String value) {
		this.done = true;
		this.value = value;
	}

	/**
	 * Process a int numeric field.
	 */
	public void intField(FieldInfo fieldInfo, int value) {
		this.done = true;
		this.value = value;
	}

	/**
	 * Process a long numeric field.
	 */
	public void longField(FieldInfo fieldInfo, long value) {
		this.done = true;
		this.value = value;
	}

	/**
	 * Process a float numeric field.
	 */
	public void floatField(FieldInfo fieldInfo, float value) {
		this.done = true;
		this.value = value;
	}

	/**
	 * Process a double numeric field.
	 */
	public void doubleField(FieldInfo fieldInfo, double value) {
		this.done = true;
		this.value = value;
	}
}
