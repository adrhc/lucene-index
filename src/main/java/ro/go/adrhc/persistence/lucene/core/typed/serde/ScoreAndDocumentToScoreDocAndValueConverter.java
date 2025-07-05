package ro.go.adrhc.persistence.lucene.core.typed.serde;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import ro.go.adrhc.persistence.lucene.core.bare.read.ScoreDocAndDocument;
import ro.go.adrhc.persistence.lucene.core.typed.read.ScoreDocAndValue;

import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ScoreAndDocumentToScoreDocAndValueConverter<T>
	implements Converter<ScoreDocAndDocument, Optional<ScoreDocAndValue<T>>> {
	private final DocumentToTypedConverter<T> docToTypedConverter;

	public Stream<ScoreDocAndValue<T>> convertStream(Stream<ScoreDocAndDocument> stream) {
		return stream.map(this::convert).flatMap(Optional::stream);
	}

	@Override
	@NonNull
	public Optional<ScoreDocAndValue<T>> convert(@NonNull ScoreDocAndDocument scoreAndDocument) {
		if (scoreAndDocument.isBroken()) {
			return Optional.empty();
		} else {
			return docToTypedConverter.convert(scoreAndDocument.document())
				.map(t -> new ScoreDocAndValue<>(scoreAndDocument.scoreDoc(), t));
		}
	}
}
