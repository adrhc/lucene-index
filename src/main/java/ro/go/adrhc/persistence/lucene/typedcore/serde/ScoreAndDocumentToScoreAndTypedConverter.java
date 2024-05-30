package ro.go.adrhc.persistence.lucene.typedcore.serde;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import ro.go.adrhc.persistence.lucene.core.read.ScoreDocAndDocument;
import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreDocAndValue;

import java.util.Optional;

@RequiredArgsConstructor
public class ScoreAndDocumentToScoreAndTypedConverter<T>
		implements Converter<ScoreDocAndDocument, Optional<ScoreDocAndValue<T>>> {
	private final DocumentToTypedConverter<T> docToTypedConverter;

	@Override
	@NonNull
	public Optional<ScoreDocAndValue<T>> convert(@NonNull ScoreDocAndDocument scoreAndDocument) {
		return docToTypedConverter.convert(scoreAndDocument.document())
				.map(t -> new ScoreDocAndValue<>(scoreAndDocument.scoreDoc(), t));
	}
}
