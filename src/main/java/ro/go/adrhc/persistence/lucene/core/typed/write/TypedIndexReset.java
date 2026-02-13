package ro.go.adrhc.persistence.lucene.core.typed.write;

import ro.go.adrhc.persistence.lucene.core.bare.write.DocsIndexWriter;
import ro.go.adrhc.persistence.lucene.core.typed.serde.TypedToDocumentConverter;

import java.io.IOException;
import java.util.stream.Stream;

public class TypedIndexReset<T> extends AbstractTypedIndexWriter<T> {
	public TypedIndexReset(TypedToDocumentConverter<T> toDocumentConverter,
		DocsIndexWriter indexWriter) {
		super(toDocumentConverter, indexWriter);
	}

	public static <T> TypedIndexReset<T> create(TypedIndexWriterParams<T> params) {
		return new TypedIndexReset<>(TypedToDocumentConverter.create(params),
			new DocsIndexWriter(params.indexWriter()));
	}

	public void reset(Stream<T> stateAfterReset) throws IOException {
		docsIndexWriter.reset(toDocuments(stateAfterReset));
	}
}
