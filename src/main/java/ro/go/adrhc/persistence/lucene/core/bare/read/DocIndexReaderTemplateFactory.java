package ro.go.adrhc.persistence.lucene.core.bare.read;

import lombok.experimental.UtilityClass;

import java.nio.file.Path;

@UtilityClass
public class DocIndexReaderTemplateFactory {
	public static DocIndexReaderTemplate of(Path indexPath) {
		return of(IndexReaderPoolFactory.of(indexPath));
	}

	public static DocIndexReaderTemplate of(IndexReaderPool indexReaderPool) {
		return new DocIndexReaderTemplate(() -> DocIndexReader.of(indexReaderPool));
	}

	public static DocIndexReaderTemplate of(DocIndexReaderParams params) {
		return new DocIndexReaderTemplate(() -> new DocIndexReader(
			params::closeIndexReader, params.createIndexReader()));
	}
}
