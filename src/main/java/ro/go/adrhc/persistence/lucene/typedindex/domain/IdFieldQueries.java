package ro.go.adrhc.persistence.lucene.typedindex.domain;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.index.domain.queries.FieldQueries;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;

public class IdFieldQueries extends FieldQueries {
	private final TypedField<?> idField;

	public IdFieldQueries(StandardQueryParser standardQueryParser, TypedField<?> idField) {
		super(standardQueryParser, idField.name());
		this.idField = idField;
	}

	public static IdFieldQueries
	createIdFieldQueries(Analyzer analyzer, TypedField<?> idField) {
		return new IdFieldQueries(new StandardQueryParser(analyzer), idField);
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
			case KEYWORD -> wordEquals((String) idValue);
			case LONG -> longEquals((Long) idValue);
			case INT -> intEquals((Integer) idValue);
			default -> throw new IllegalStateException(
					"Unexpected type %s for %s! "
							.formatted(idField.fieldType(), idField));
		};
	}
}
