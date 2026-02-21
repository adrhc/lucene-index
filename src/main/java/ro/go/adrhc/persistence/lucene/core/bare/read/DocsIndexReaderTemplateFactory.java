package ro.go.adrhc.persistence.lucene.core.bare.read;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DocsIndexReaderTemplateFactory {
	public static DocsIndexReaderTemplate create(HitsLimitedDocsIndexReaderParams params) {
		return new DocsIndexReaderTemplate(() -> HitsLimitedDocsIndexReaderFactory.create(params));
	}

	public static DocsIndexReaderTemplate createUnlimited(IndexReaderPool indexReaderPool) {
		return new DocsIndexReaderTemplate(
			() -> HitsLimitedDocsIndexReaderFactory.createUnlimited(indexReaderPool));
	}
}
