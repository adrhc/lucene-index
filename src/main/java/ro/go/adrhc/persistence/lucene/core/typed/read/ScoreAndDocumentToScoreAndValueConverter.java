package ro.go.adrhc.persistence.lucene.core.typed.read;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import ro.go.adrhc.persistence.lucene.core.bare.read.ScoreAndDocument;
import ro.go.adrhc.persistence.lucene.core.typed.field.RawFieldValueSerdes;

import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ScoreAndDocumentToScoreAndValueConverter<T> {
	private final DocumentToTypedConverter<T> docToTypedConverter;

	public static <T> ScoreAndDocumentToScoreAndValueConverter<T>
	of(RawFieldValueSerdes<T> rawFieldValueSerdes) {
		DocumentToTypedConverter<T> docToTypedConverter =
			DocumentToTypedConverter.create(rawFieldValueSerdes);
		return new ScoreAndDocumentToScoreAndValueConverter<>(docToTypedConverter);
	}

	public Stream<ScoreAndValue<T>> convertStream(Stream<ScoreAndDocument> stream) {
		return stream.map(this::convert).flatMap(Optional::stream);
	}

	@NonNull
	public Optional<ScoreAndValue<T>> convert(@NonNull ScoreAndDocument scoreAndDocument) {
		if (scoreAndDocument.isBroken()) {
			return Optional.empty();
		} else {
			return docToTypedConverter.convert(scoreAndDocument.document())
				.map(t -> new ScoreAndValue<>(scoreAndDocument.scoreDoc(), t));
		}
	}
}
