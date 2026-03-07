package ro.go.adrhc.persistence.lucene.core.typed.read;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.bare.read.HitsLimitedDocIndexReader;
import ro.go.adrhc.persistence.lucene.core.bare.read.HitsLimitedDocIndexReaderFactory;
import ro.go.adrhc.persistence.lucene.core.bare.read.ScoreDocAndValue;
import ro.go.adrhc.util.Breakable;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;

import static java.util.function.Predicate.not;

@RequiredArgsConstructor
public class OneHitIndexReader<T> implements Closeable {
	private final ScoreAndDocumentToScoreDocAndValueConverter<T> toScoreDocAndValueConverter;
	private final HitsLimitedDocIndexReader indexReader;

	public static <T> OneHitIndexReader<T>
	create(TypedIndexReaderParams<T> params) throws IOException {
		ScoreAndDocumentToScoreDocAndValueConverter<T> toScoreAndTypedConverter =
			ScoreAndDocumentToScoreDocAndValueConverter.of(params.rawFieldValueSerdes());
		HitsLimitedDocIndexReader limitedDocIndexReader =
			HitsLimitedDocIndexReaderFactory.create(params, 1);
		return new OneHitIndexReader<>(toScoreAndTypedConverter, limitedDocIndexReader);
	}

	public Optional<ScoreDocAndValue<T>> findFirst(Query query) throws IOException {
		return indexReader.findMany(query)
			.filter(not(Breakable::isBroken))
			.map(toScoreDocAndValueConverter::convert)
			.flatMap(Optional::stream)
			.findAny(); // DocIndexReader is created with numHits = 1
	}

	@Override
	public void close() throws IOException {
		indexReader.close();
	}
}
