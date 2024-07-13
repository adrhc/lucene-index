package ro.go.adrhc.persistence.lucene.typedcore;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.query.FieldQueries;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class ExactQuery {
	private final TypedField<?> field;
	private final FieldQueries fieldQueries;

	public static ExactQuery create(TypedField<?> field) {
		return new ExactQuery(field, new FieldQueries(field.name()));
	}

	public List<Query> newExactQueries(Collection<?> values) {
		return values.stream().map(this::newExactQuery).toList();
	}

	public Query newExactQuery(Document document) {
		return newExactQuery(document.getField(field.name()));
	}

	public Query newExactQuery(IndexableField field) {
		return switch (this.field.fieldType()) {
			case KEYWORD -> newExactQuery(field.stringValue());
			case LONG -> newExactQuery(field.numericValue().longValue());
			case INT -> newExactQuery(field.numericValue().intValue());
			default -> throw new IllegalStateException(
					"Unexpected type %s for %s! "
							.formatted(this.field.fieldType(), this.field.name()));
		};
	}

	public Query newExactQuery(Object typedValue) {
		Object idFieldValue = field.propToIndexableValue(typedValue);
		return switch (field.fieldType()) {
			case KEYWORD -> fieldQueries.keywordEquals((String) idFieldValue);
			case LONG -> fieldQueries.longEquals((Long) idFieldValue);
			case INT -> fieldQueries.intEquals((Integer) idFieldValue);
			default -> throw new IllegalStateException(
					"Unexpected type %s for %s! "
							.formatted(field.fieldType(), field));
		};
	}
}
