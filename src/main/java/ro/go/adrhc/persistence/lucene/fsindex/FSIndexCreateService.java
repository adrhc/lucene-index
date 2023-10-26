package ro.go.adrhc.persistence.lucene.fsindex;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.index.core.docds.datasource.DocumentsDataSource;

import java.io.IOException;

@RequiredArgsConstructor
public class FSIndexCreateService {
	private final DocumentsDataSource documentsDatasource;
	private final FSIndexUpdateService fsLuceneIndex;

	public void createOrReplace() throws IOException {
		fsLuceneIndex.createOrReplace(documentsDatasource.loadAll());
	}
}
