package ro.go.adrhc.persistence.lucene.fsindex;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.index.spi.DocumentsDatasource;

import java.io.IOException;

@RequiredArgsConstructor
public class FSIndexCreateService {
	private final DocumentsDatasource documentsDatasource;
	private final FSIndexUpdateService fsLuceneIndex;

	public void createOrReplace() throws IOException {
		fsLuceneIndex.createOrReplace(documentsDatasource.loadAll());
	}
}
