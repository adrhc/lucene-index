package ro.go.adrhc.persistence.lucene.index.operations.search;

public interface IndexSearchService<T> extends SearchManyService<T>,
		SearchReduceService<T>, BestMatchSearchService<T> {
}
