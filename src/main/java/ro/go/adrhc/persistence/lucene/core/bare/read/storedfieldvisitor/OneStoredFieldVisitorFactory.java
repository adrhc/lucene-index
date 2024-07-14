package ro.go.adrhc.persistence.lucene.core.bare.read.storedfieldvisitor;

public class OneStoredFieldVisitorFactory {
	public static <V> AbstractOneStoredFieldVisitor<V>
	create(Class<V> type, String fieldName) {
		if (type.isAssignableFrom(String.class)) {
			return (AbstractOneStoredFieldVisitor<V>)
					new OneStoredStringFieldVisitor(fieldName);
		} else if (type.isAssignableFrom(Integer.class)) {
			return (AbstractOneStoredFieldVisitor<V>)
					new OneStoredIntegerFieldVisitor(fieldName);
		} else if (type.isAssignableFrom(Long.class)) {
			return (AbstractOneStoredFieldVisitor<V>)
					new OneStoredLongFieldVisitor(fieldName);
		} else {
			return (AbstractOneStoredFieldVisitor<V>)
					new OneStoredObjectFieldVisitor(fieldName);
		}
	}
}
