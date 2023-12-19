package ro.go.adrhc.persistence.lucene.core.query;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;

import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TermQueryFactory {
    public static TermQuery create(Enum<?> fieldType, String text) {
        return create(fieldType.name(), text);
    }

    public static TermQuery create(String fieldName, String text) {
        return new TermQuery(new Term(fieldName, text));
    }

    public static Stream<TermQuery> create(String fieldName, Collection<String> texts) {
        return texts.stream().map(t -> TermQueryFactory.create(fieldName, t));
    }

    public static Stream<TermQuery> create(Enum<?> fieldType, IntStream intStream) {
        return intStream.mapToObj(String::valueOf).map(t -> TermQueryFactory.create(fieldType, t));
    }
}
