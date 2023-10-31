package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import ro.go.adrhc.persistence.lucene.index.core.analysis.AnalyzerFactory;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.index.restore.DocumentsIndexRestoreService;
import ro.go.adrhc.persistence.lucene.index.search.BestMatchingStrategy;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.typedindex.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.QuerySearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.domain.seach.QuerySearchResultFactory;
import ro.go.adrhc.persistence.lucene.typedindex.search.QuerySearchResultFilter;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchByIdService;

import java.io.IOException;
import java.nio.file.Path;

@Getter
@RequiredArgsConstructor
public class TypedIndexFactories<ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>> {
	private final int numHits;
	private final Analyzer analyzer;
	private final Class<T> tClass;
	private final Class<E> tFieldEnumClass;
	private final E idField;

	/**
	 * constructor parameters union
	 */
	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>>
	TypedIndexFactories<ID, T, E> create(int numHits, Class<T> tClass,
			Class<E> typedFieldEnumClass, TokenizerProperties tokenizerProperties) throws IOException {
		AnalyzerFactory analyzerFactory = new AnalyzerFactory(tokenizerProperties);
		return new TypedIndexFactories<>(numHits, analyzerFactory.create(),
				tClass, typedFieldEnumClass, TypedField.getIdField(typedFieldEnumClass));
	}

	public IndexSearchService<QuerySearchResult<T>>
	createTypedIndexSearchService(
			BestMatchingStrategy<QuerySearchResult<T>> bestMatchingStrategy,
			QuerySearchResultFilter<T> searchResultFilter, Path indexPath) {
		return new IndexSearchService<>(
				new DocumentIndexReaderTemplate(numHits, indexPath),
				QuerySearchResultFactory.create(tClass),
				bestMatchingStrategy,
				searchResultFilter
		);
	}

	public TypedSearchByIdService<ID, T> createSearchByIdService(Path indexPath) {
		return TypedSearchByIdService.create(tClass, idField, indexPath);
	}

	public TypedIndexCreateService<T> createTypedIndexCreateService(Path indexPath) {
		return TypedIndexCreateService.create(analyzer, tFieldEnumClass, indexPath);
	}

	public DocumentsIndexRestoreService createDocumentsIndexRestoreService(Path indexPath) {
		return DocumentsIndexRestoreService.create(idField, analyzer, indexPath);
	}

	public TypedIndexUpdateService<ID, T> createTypedIndexUpdateService(Path indexPath) {
		return TypedIndexUpdateService.create(analyzer, tFieldEnumClass, indexPath);
	}
}
