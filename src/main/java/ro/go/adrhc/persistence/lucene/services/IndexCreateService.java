package ro.go.adrhc.persistence.lucene.services;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.FSTypedIndex;
import ro.go.adrhc.persistence.lucene.domain.MetadataProvider;

import java.io.IOException;

@RequiredArgsConstructor
public class IndexCreateService<MID, M> {
	private final MetadataProvider<MID, M> metadataProvider;
	private final FSTypedIndex<M> fsTypedIndex;

	public void createOrReplace() throws IOException {
		fsTypedIndex.createOrReplace(metadataProvider.loadAll());
	}
}
