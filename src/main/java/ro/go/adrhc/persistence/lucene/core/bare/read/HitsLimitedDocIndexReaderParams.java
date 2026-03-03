package ro.go.adrhc.persistence.lucene.core.bare.read;

public interface HitsLimitedDocIndexReaderParams extends DocIndexReaderParams {
	int numHits();
}
