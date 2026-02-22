package ro.go.adrhc.persistence.lucene.core.bare.read.storedfieldvisitor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.StoredFieldVisitor;

@RequiredArgsConstructor
public abstract class AbstractOneStoredFieldVisitor<V> extends StoredFieldVisitor {
	private final String name;
	@Getter
	protected V value;
	protected boolean done;

	@Override
	public Status needsField(FieldInfo fieldInfo) {
		return done ? Status.STOP : (fieldInfo.name.equals(name) ? Status.YES : Status.NO);
	}

	public void reset() {
		value = null;
		done = false;
	}
}
