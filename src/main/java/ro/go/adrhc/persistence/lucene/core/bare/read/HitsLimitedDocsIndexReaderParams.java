package ro.go.adrhc.persistence.lucene.core.bare.read;

public interface HitsLimitedDocsIndexReaderParams {
	IndexReaderPool getIndexReaderPool();

	int getNumHits();
}
