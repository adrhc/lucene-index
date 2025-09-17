package ro.go.adrhc.persistence.lucene.core.bare.query;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;

import java.util.Optional;

import static ro.go.adrhc.util.optional.OptionalFactory.ofSilencedRiskySupplier;

@RequiredArgsConstructor
public class DefaultFieldAwareQueryParser {
	private final QueryParser queryParser;
	private final String defaultField;

	public static DefaultFieldAwareQueryParser create(Analyzer analyzer, Enum<?> defaultField) {
		return new DefaultFieldAwareQueryParser(QueryParser.create(analyzer), defaultField.name());
	}

	public Optional<Query> parse(String query) {
		return ofSilencedRiskySupplier(() -> queryParser.parse(defaultField, query));
	}
}
