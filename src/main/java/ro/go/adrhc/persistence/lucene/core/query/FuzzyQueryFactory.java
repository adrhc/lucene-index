package ro.go.adrhc.persistence.lucene.core.query;

import lombok.experimental.UtilityClass;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;

import java.util.Collection;
import java.util.stream.Stream;

import static org.apache.lucene.util.automaton.LevenshteinAutomata.MAXIMUM_SUPPORTED_DISTANCE;

@UtilityClass
public class FuzzyQueryFactory {
    public static final int MAX_FUZZINESS = MAXIMUM_SUPPORTED_DISTANCE;
    public static final int LOW_FUZZINESS = 1;

    public static FuzzyQuery maxFuzziness(String fieldName, String text) {
        return new FuzzyQuery(new Term(fieldName, text), MAX_FUZZINESS);
    }

    public static FuzzyQuery lowFuzziness(String fieldName, String text) {
        return new FuzzyQuery(new Term(fieldName, text), LOW_FUZZINESS);
    }

    public static FuzzyQuery create(int levenshteinDistance, String fieldName, String text) {
        return new FuzzyQuery(new Term(fieldName, text), levenshteinDistance);
    }

    public static Stream<FuzzyQuery> create(
            int levenshteinDistance, String fieldName, Collection<String> tokens) {
        return tokens.stream().map(t -> create(levenshteinDistance, fieldName, t));
    }
}
