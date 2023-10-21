package ro.go.adrhc.persistence.lucene.services;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.domain.DocumentsProvider;
import ro.go.adrhc.persistence.lucene.index.FSLuceneIndex;

import java.io.IOException;

@RequiredArgsConstructor
public class IndexCreateService {
	private final DocumentsProvider documentsProvider;
	private final FSLuceneIndex fsTypedIndex;

	public void createOrReplace() throws IOException {
		fsTypedIndex.createOrReplace(documentsProvider.loadAll());
	}
}
