package ro.go.adrhc.persistence.lucene.core.read;

public interface DocumentsIndexReaderParams {
	IndexReaderPool getIndexReaderPool();

	int getNumHits();
}
