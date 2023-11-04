package ro.go.adrhc.persistence.lucene.typedindex.domain;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.index.domain.queries.FieldQueries;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class ExactQuery {
	private final TypedField<?> idField;
	private final FieldQueries fieldQueries;

	public static ExactQuery create(TypedField<?> idField) {
		return new ExactQuery(idField, new FieldQueries(idField.name()));
	}

	public List<Query> newExactQueries(Collection<String> ids) {
		return ids.stream().map(this::newExactQuery).toList();
	}

	public Query newExactQuery(Document document) {
		return newExactQuery(document.getField(idField.name()));
	}

	public Query newExactQuery(IndexableField field) {
		return switch (idField.fieldType()) {
			case KEYWORD -> newExactQuery(field.stringValue());
			case LONG -> newExactQuery(field.numericValue().longValue());
			case INT -> newExactQuery(field.numericValue().intValue());
			default -> throw new IllegalStateException(
					"Unexpected type %s for %s! "
							.formatted(idField.fieldType(), idField.name()));
		};
	}

	public Query newExactQuery(Object idValue) {
		return switch (idField.fieldType()) {
			case KEYWORD -> fieldQueries.keywordEquals((String) idValue);
			case LONG -> fieldQueries.longEquals((Long) idValue);
			case INT -> fieldQueries.intEquals((Integer) idValue);
			default -> throw new IllegalStateException(
					"Unexpected type %s for %s! "
							.formatted(idField.fieldType(), idField));
		};
	}
}
