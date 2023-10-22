package ro.go.adrhc.persistence.lucene.index;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.fsindex.FSLuceneIndex;
import ro.go.adrhc.persistence.lucene.index.spi.DocumentsDatasource;

import java.io.IOException;

@RequiredArgsConstructor
public class IndexCreateService {
	private final DocumentsDatasource documentsDatasource;
	private final FSLuceneIndex fsTypedIndex;

	public void createOrReplace() throws IOException {
		fsTypedIndex.createOrReplace(documentsDatasource.loadAll());
	}
}
