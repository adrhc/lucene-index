package ro.go.adrhc.persistence.lucene.core.typed;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.bare.query.FieldQueries;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class ExactQuery {
	private final LuceneFieldSpec<?> field;
	private final FieldQueries fieldQueries;

	public static ExactQuery create(LuceneFieldSpec<?> field) {
		return new ExactQuery(field, new FieldQueries(field.name()));
	}

	/**
	 * @param propertyValues will be converted to indexable values which then will be used for the query
	 */
	public List<Query> newExactQueries(Collection<?> propertyValues) {
		return propertyValues.stream().map(this::newExactQuery).toList();
	}

	/**
	 * The value to query for is the one of the field with the same name as "this".field.
	 *
	 * @param document is used to get the value to query for
	 */
	public Query newExactQuery(Document document) {
		return newExactQuery(document.getField(field.name()));
	}

	/**
	 * @param valueSource is used only to get the value to query for
	 */
	public Query newExactQuery(IndexableField valueSource) {
		return switch (this.field.fieldType()) {
			case KEYWORD -> newExactQuery(valueSource.stringValue());
			case LONG -> newExactQuery(valueSource.numericValue().longValue());
			case INT -> newExactQuery(valueSource.numericValue().intValue());
			default -> throw new IllegalStateException(
				"Unexpected type %s for %s! "
					.formatted(this.field.fieldType(), this.field.name()));
		};
	}

	/**
	 * @param propertyValue will be converted to an indexable value which then will be used for the query
	 */
	public Query newExactQuery(Object propertyValue) {
		Object idFieldValue = field.propToIndexableValue(propertyValue);
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
