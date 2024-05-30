package ro.go.adrhc.persistence.lucene.core.read;

public interface HitsLimitedDocsIndexReaderParams {
	IndexReaderPool getIndexReaderPool();

	int getNumHits();
}
