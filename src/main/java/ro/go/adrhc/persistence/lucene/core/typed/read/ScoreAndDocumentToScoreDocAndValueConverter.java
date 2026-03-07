package ro.go.adrhc.persistence.lucene.core.typed.read;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import ro.go.adrhc.persistence.lucene.core.bare.read.ScoreDocAndDocument;
import ro.go.adrhc.persistence.lucene.core.bare.read.ScoreDocAndValue;
import ro.go.adrhc.persistence.lucene.core.typed.field.RawFieldValueSerdes;

import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ScoreAndDocumentToScoreDocAndValueConverter<T> {
	private final DocumentToTypedConverter<T> docToTypedConverter;

	public static <T> ScoreAndDocumentToScoreDocAndValueConverter<T>
	of(RawFieldValueSerdes<T> rawFieldValueSerdes) {
		DocumentToTypedConverter<T> docToTypedConverter =
			DocumentToTypedConverter.create(rawFieldValueSerdes);
		return new ScoreAndDocumentToScoreDocAndValueConverter<>(docToTypedConverter);
	}

	public Stream<ScoreDocAndValue<T>> convertStream(Stream<ScoreDocAndDocument> stream) {
		return stream.map(this::convert).flatMap(Optional::stream);
	}

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
