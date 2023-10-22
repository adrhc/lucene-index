package ro.go.adrhc.persistence.lucene.index.spi;

import org.apache.lucene.document.Document;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface DocumentsProvider {
	List<String> loadAllIds() throws IOException;

	List<Document> loadByIds(Collection<String> paths) throws IOException;

	default List<Document> loadAll() throws IOException {
		return loadByIds(loadAllIds());
	}
}
