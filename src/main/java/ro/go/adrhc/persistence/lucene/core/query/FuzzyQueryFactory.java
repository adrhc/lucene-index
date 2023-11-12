package ro.go.adrhc.persistence.lucene.core.query;

import lombok.experimental.UtilityClass;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;

import java.util.Collection;
import java.util.stream.Stream;

@UtilityClass
public class FuzzyQueryFactory {
	public static FuzzyQuery create(int levenshteinDistance, String fieldName, String text) {
		return new FuzzyQuery(new Term(fieldName, text), levenshteinDistance);
	}

	public static Stream<FuzzyQuery> create(
			int levenshteinDistance, String fieldName, Collection<String> tokens) {
		return tokens.stream().map(t -> create(levenshteinDistance, fieldName, t));
	}
}
