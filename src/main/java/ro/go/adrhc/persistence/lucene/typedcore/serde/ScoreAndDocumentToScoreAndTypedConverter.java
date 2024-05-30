package ro.go.adrhc.persistence.lucene.typedcore.serde;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import ro.go.adrhc.persistence.lucene.core.read.ScoreAndDocument;
import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreAndValue;

import java.util.Optional;

@RequiredArgsConstructor
public class ScoreAndDocumentToScoreAndTypedConverter<T>
		implements Converter<ScoreAndDocument, Optional<ScoreAndValue<T>>> {
	private final DocumentToTypedConverter<T> docToTypedConverter;

	@Override
	@NonNull
	public Optional<ScoreAndValue<T>> convert(@NonNull ScoreAndDocument scoreAndDocument) {
		return docToTypedConverter.convert(scoreAndDocument.document())
				.map(t -> new ScoreAndValue<>(scoreAndDocument.score(), t));
	}
}
