package ro.go.adrhc.persistence.lucene.core.typed.search;

public interface IndexSearchService<T> extends SearchManyService<T>,
	SearchReduceService<T>, BestMatchSearchService<T> {
}
