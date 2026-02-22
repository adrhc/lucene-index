package ro.go.adrhc.persistence.lucene.core.bare.read;

import lombok.Getter;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.StoredFieldVisitor;

import java.util.ArrayList;
import java.util.List;

public class StoredObjectFieldValuesVisitor extends StoredFieldVisitor {
	private final String name;
	@Getter
	private final List<Object> values = new ArrayList<>();

	public StoredObjectFieldValuesVisitor(String name) {
		this.name = name;
	}

	public void reset() {
		values.clear();
	}

	@Override
	public Status needsField(FieldInfo fieldInfo) {
		return fieldInfo.name.equals(name) ? Status.YES : Status.NO;
	}

	@Override
	public void binaryField(FieldInfo fieldInfo, byte[] value) {
		values.add(value);
	}

	@Override
	public void stringField(FieldInfo fieldInfo, String value) {
		values.add(value);
	}

	@Override
	public void intField(FieldInfo fieldInfo, int value) {
		values.add(value);
	}

	@Override
	public void longField(FieldInfo fieldInfo, long value) {
		values.add(value);
	}

	@Override
	public void floatField(FieldInfo fieldInfo, float value) {
		values.add(value);
	}

	@Override
	public void doubleField(FieldInfo fieldInfo, double value) {
		values.add(value);
	}
}

