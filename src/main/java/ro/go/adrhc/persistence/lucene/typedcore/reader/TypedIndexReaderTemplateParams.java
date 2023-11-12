package ro.go.adrhc.persistence.lucene.typedcore.reader;

import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;

public interface TypedIndexReaderTemplateParams<T> {
	Class<T> getType();

	IndexReaderPool getIndexReaderPool();

	int getNumHits();
}
