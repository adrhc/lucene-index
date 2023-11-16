package ro.go.adrhc.persistence.lucene.typedcore.write;

import ro.go.adrhc.persistence.lucene.core.write.DocsIndexWriter;
import ro.go.adrhc.persistence.lucene.typedcore.serde.TypedToDocumentConverter;

import java.io.IOException;
import java.util.stream.Stream;

public class TypedIndexReset<T> extends AbstractTypedIndex<T> {
	public TypedIndexReset(TypedToDocumentConverter<T> toDocumentConverter, DocsIndexWriter indexWriter) {
		super(toDocumentConverter, indexWriter);
	}

	public static <T> TypedIndexReset<T> create(AbstractTypedIndexParams<T> params) {
		return new TypedIndexReset<>(TypedToDocumentConverter.create(params),
				new DocsIndexWriter(params.getIndexWriter()));
	}

	public void reset(Stream<T> stateAfterReset) throws IOException {
		docsIndexWriter.reset(toDocuments(stateAfterReset));
	}
}
