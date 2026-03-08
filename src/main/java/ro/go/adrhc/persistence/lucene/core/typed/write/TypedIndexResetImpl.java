package ro.go.adrhc.persistence.lucene.core.typed.write;

import lombok.extern.slf4j.Slf4j;
import ro.go.adrhc.persistence.lucene.core.bare.write.DocIndexWriter;

import java.io.IOException;
import java.util.stream.Stream;

@Slf4j
public class TypedIndexResetImpl<T> extends AbstractIndexWriter<T> implements TypedIndexReset<T> {
	public TypedIndexResetImpl(
		TypedToDocumentConverter<T> toDocumentConverter, DocIndexWriter indexWriter) {
		super(toDocumentConverter, indexWriter);
	}

	public static <T> TypedIndexResetImpl<T> create(TypedIndexWriterParams<T> params) {
		return new TypedIndexResetImpl<>(
			TypedToDocumentConverter.create(params),
			new DocIndexWriter(params.indexWriter()));
	}

	@Override
	public void reset(Stream<T> stateAfterReset) throws IOException {
		log.info("\nresetting the index ...");
		docIndexWriter.reset(toDocuments(stateAfterReset));
		log.info("\ncommitting changes to the index ...");
		docIndexWriter.commit();
		log.info("\nIndex reset complete!");
	}
}
