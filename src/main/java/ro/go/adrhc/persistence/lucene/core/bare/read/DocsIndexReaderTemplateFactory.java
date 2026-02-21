package ro.go.adrhc.persistence.lucene.core.bare.read;

import lombok.experimental.UtilityClass;

import java.nio.file.Path;

@UtilityClass
public class DocsIndexReaderTemplateFactory {
	public static DocsIndexReaderTemplate of(Path indexPath) {
		return of(IndexReaderPoolFactory.of(indexPath));
	}

	public static DocsIndexReaderTemplate of(IndexReaderPool indexReaderPool) {
		return new DocsIndexReaderTemplate(() -> DocsIndexReader.create(indexReaderPool));
	}
}
