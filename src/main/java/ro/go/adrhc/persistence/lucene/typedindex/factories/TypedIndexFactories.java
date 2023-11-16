package ro.go.adrhc.persistence.lucene.typedindex.factories;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.index.DocsCountService;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedIndexAdderService;
import ro.go.adrhc.persistence.lucene.typedindex.create.TypedIndexInitService;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedIndexRetrieveService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedIndexUpdateService;

@RequiredArgsConstructor
public class TypedIndexFactories<ID, T extends Identifiable<ID>> {
	private final TypedIndexFactoriesParams<T> params;

	public TypedIndexSearchService<T> createSearchService() {
		return TypedIndexSearchService.create(params);
	}

	public TypedIndexRetrieveService<ID, T> createIdSearchService() {
		return TypedIndexRetrieveService.create(params);
	}

	public DocsCountService createCountService() {
		return DocsCountService.create(params.getIndexReaderPool());
	}

	public TypedIndexRestoreService<ID, T> createRestoreService() {
		return TypedIndexRestoreService.create(params);
	}

	public TypedIndexInitService<ID, T> createInitService() {
		return TypedIndexInitService.create(params);
	}

	public TypedIndexAdderService<T> createAdderService() {
		return TypedIndexAdderService.create(params);
	}

	public TypedIndexUpdateService<T> createUpdateService() {
		return TypedIndexUpdateService.create(params);
	}

	public TypedIndexRemoveService<ID> createRemoveService() {
		return TypedIndexRemoveService.create(params);
	}
}
