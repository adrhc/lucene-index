package ro.go.adrhc.persistence.lucene.operations.search;

public interface IndexSearchService<T> extends SearchManyService<T>,
	SearchReduceService<T>, BestMatchSearchService<T> {
}
