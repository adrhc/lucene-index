package ro.go.adrhc.persistence.lucene.index.spi;

import org.apache.lucene.document.Document;

import java.io.IOException;
import java.util.Collection;

public interface DocumentsDatasource {
	Collection<String> loadAllIds() throws IOException;

	Collection<Document> loadByIds(Collection<String> ids) throws IOException;

	default Collection<Document> loadAll() throws IOException {
		return loadByIds(loadAllIds());
	}
}
