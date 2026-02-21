package ro.go.adrhc.persistence.lucene.core.typed.read;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.bare.read.HitsLimitedDocsIndexReader;
import ro.go.adrhc.persistence.lucene.core.bare.read.HitsLimitedDocsIndexReaderFactory;
import ro.go.adrhc.persistence.lucene.core.bare.read.ScoreDocAndValue;
import ro.go.adrhc.persistence.lucene.core.typed.serde.DocumentToTypedConverter;
import ro.go.adrhc.persistence.lucene.core.typed.serde.ScoreAndDocumentToScoreDocAndValueConverter;
import ro.go.adrhc.util.Breakable;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;

import static java.util.function.Predicate.not;

@RequiredArgsConstructor
public class OneHitIndexReader<T> implements Closeable {
	private final ScoreAndDocumentToScoreDocAndValueConverter<T> toScoreDocAndValueConverter;
	private final HitsLimitedDocsIndexReader indexReader;

	public static <T> OneHitIndexReader<T> create(OneHitIndexReaderParams<T> params)
		throws IOException {
		DocumentToTypedConverter<T> docToTypedConverter =
				DocumentToTypedConverter.create(params.getType());
		ScoreAndDocumentToScoreDocAndValueConverter<T> toScoreAndTypedConverter =
			new ScoreAndDocumentToScoreDocAndValueConverter<>(docToTypedConverter);
		return new OneHitIndexReader<>(toScoreAndTypedConverter,
				HitsLimitedDocsIndexReaderFactory.create(params.getIndexReaderPool(), 1));
	}

	public Optional<ScoreDocAndValue<T>> findFirst(Query query) throws IOException {
		return indexReader.findMany(query)
			.filter(not(Breakable::isBroken))
			.map(toScoreDocAndValueConverter::convert)
			.flatMap(Optional::stream)
			.findAny(); // DocsIndexReader is created with numHits = 1
	}

	@Override
	public void close() throws IOException {
		indexReader.close();
	}
}
