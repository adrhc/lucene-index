package ro.go.adrhc.persistence.lucene.operation;

import lombok.RequiredArgsConstructor;
import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexCounter;
import ro.go.adrhc.persistence.lucene.core.bare.read.DocIndexCounterImpl;
import ro.go.adrhc.persistence.lucene.core.typed.Indexable;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.read.IndexReadService;
import ro.go.adrhc.persistence.lucene.core.typed.read.IndexReadServiceImpl;
import ro.go.adrhc.persistence.lucene.core.typed.read.TypedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.service.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.service.search.IndexSearchServiceImpl;

@RequiredArgsConstructor
public class ReadIndexOperationsFactory<T extends Indexable<I, T>, I> {
	private final LuceneFieldSpec<T> idField;
	private final TypedIndexReaderTemplate<I, T> indexReaderTemplate;
	private final DocIndexCounter indexCounter;
	private final IndexReadService<I, T> retrieveService;
	private final IndexSearchService<T> searchService;

	public static <T extends Indexable<I, T>, I>
	ReadIndexOperationsFactory<T, I> of(IndexOperationsParams<T> params) {
		DocIndexCounter indexCounter = createDocIndexCounter(params);
		IndexReadService<I, T> retrieveService = createRetrieveService(params);
		IndexSearchService<T> searchService = createSearchService(params);
		TypedIndexReaderTemplate<I, T> typedIndexReaderTemplate =
			TypedIndexReaderTemplate.create(params.typedIndexReaderParams());
		return new ReadIndexOperationsFactory<>(params.idField(),
			typedIndexReaderTemplate, indexCounter, retrieveService, searchService);
	}

	public ReadIndexOperations<T, I> create() {
		return new ReadIndexOperationsImpl<>(idField, indexReaderTemplate,
			indexCounter, retrieveService, searchService);
	}

	private static <I, T> IndexReadService<I, T>
	createRetrieveService(IndexOperationsParams<T> params) {
		return IndexReadServiceImpl.create(params.typedIndexReaderParams());
	}

	private static <T> IndexSearchService<T> createSearchService(IndexOperationsParams<T> params) {
		return IndexSearchServiceImpl.create(params.indexSearchServiceParams());
	}

	private static <T> DocIndexCounter createDocIndexCounter(IndexOperationsParams<T> params) {
		return DocIndexCounterImpl.create(params.docIndexReaderParams());
	}
}
