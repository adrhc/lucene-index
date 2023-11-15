package ro.go.adrhc.persistence.lucene.typedindex.factories;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.index.DocsCountService;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.add.TypedIndexAdderService;
import ro.go.adrhc.persistence.lucene.typedindex.create.TypedIndexInitService;
import ro.go.adrhc.persistence.lucene.typedindex.remove.TypedIndexRemoveService;
import ro.go.adrhc.persistence.lucene.typedindex.restore.TypedIndexRestoreService;
import ro.go.adrhc.persistence.lucene.typedindex.retrieve.TypedIndexRetrieveService;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedIndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.update.TypedIndexUpdateService;

@RequiredArgsConstructor
public class TypedIndexFactories<ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>> {
	private final TypedIndexFactoriesParams<ID, T> factoriesParams;

	public TypedIndexSearchService<T> createSearchService() {
		return TypedIndexSearchService.create(factoriesParams);
	}

	public TypedIndexRetrieveService<ID, T> createIdSearchService() {
		return TypedIndexRetrieveService.create(factoriesParams);
	}

	public DocsCountService createCountService() {
		return DocsCountService.create(factoriesParams.getIndexReaderPool());
	}

	public TypedIndexRestoreService<ID, T> createRestoreService() {
		return TypedIndexRestoreService.create(factoriesParams);
	}

	public TypedIndexInitService<ID, T> createInitService() {
		return TypedIndexInitService.create(factoriesParams);
	}

	public TypedIndexAdderService<T> createAdderService() {
		return TypedIndexAdderService.create(factoriesParams);
	}

	public TypedIndexUpdateService<T> createUpdateService() {
		return TypedIndexUpdateService.create(factoriesParams);
	}

	public TypedIndexRemoveService<ID> createRemoveService() {
		return TypedIndexRemoveService.create(factoriesParams);
	}
}
