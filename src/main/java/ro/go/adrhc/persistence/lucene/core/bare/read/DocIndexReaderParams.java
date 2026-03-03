package ro.go.adrhc.persistence.lucene.core.bare.read;

import org.apache.lucene.index.IndexReader;

import java.io.IOException;

public interface DocIndexReaderParams {
	void closeIndexReader(IndexReader indexReader) throws IOException;

	IndexReader createIndexReader() throws IOException;
}
