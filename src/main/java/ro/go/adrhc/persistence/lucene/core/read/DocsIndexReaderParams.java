package ro.go.adrhc.persistence.lucene.core.read;

public interface DocsIndexReaderParams {
	IndexReaderPool getIndexReaderPool();

	int getNumHits();
}
