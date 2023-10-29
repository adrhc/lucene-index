package ro.go.adrhc.persistence.lucene.typedindex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexCreateService;
import ro.go.adrhc.persistence.lucene.fsindex.FSIndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.core.analysis.AnalyzerFactory;
import ro.go.adrhc.persistence.lucene.index.core.docds.datasource.DocumentsDataSource;
import ro.go.adrhc.persistence.lucene.index.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.index.core.docds.rawidserde.RawIdToStringConverter;
import ro.go.adrhc.persistence.lucene.index.core.read.DocumentIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.index.restore.DSIndexRestoreService;
import ro.go.adrhc.persistence.lucene.index.search.BestMatchingStrategy;
import ro.go.adrhc.persistence.lucene.index.search.IndexSearchService;
import ro.go.adrhc.persistence.lucene.index.search.SearchedToQueryConverter;
import ro.go.adrhc.persistence.lucene.typedindex.core.DocumentToTypedConverter;
import ro.go.adrhc.persistence.lucene.typedindex.core.TypedIndexReaderTemplate;
import ro.go.adrhc.persistence.lucene.typedindex.core.TypedToDocumentConverter;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedFieldEnum;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchResult;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchResultFactory;
import ro.go.adrhc.persistence.lucene.typedindex.search.TypedSearchResultFilter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import static ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedFieldEnum.getIdField;

@RequiredArgsConstructor
public class TypedIndexFactories<ID, T extends Identifiable<ID>, E extends Enum<E> & TypedFieldEnum<T>> {
	private final int maxResultsPerSearchedItem;
	@Getter
	private final Analyzer analyzer;
	private final Class<T> tClass;
	private final Class<E> typedFieldEnumClass;

	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedFieldEnum<T>>
	TypedIndexFactories<ID, T, E> of(int maxResultsPerSearchedItem, Class<T> foundClass,
			Class<E> typedFieldEnumClass, TokenizerProperties tokenizerProperties) throws IOException {
		AnalyzerFactory analyzerFactory = new AnalyzerFactory(tokenizerProperties);
		return new TypedIndexFactories<>(maxResultsPerSearchedItem,
				analyzerFactory.create(), foundClass, typedFieldEnumClass);
	}

	public <S> IndexSearchService<S, TypedSearchResult<S, T>> createTypedFSIndexSearchService(
			SearchedToQueryConverter<S> toQueryConverter, Path indexPath) {
		return createTypedFSIndexSearchService(
				toQueryConverter, Stream::findFirst, it -> true, indexPath);
	}

	public <S> IndexSearchService<S, TypedSearchResult<S, T>> createTypedFSIndexSearchService(
			SearchedToQueryConverter<S> toQueryConverter,
			BestMatchingStrategy<TypedSearchResult<S, T>> bestMatchingStrategy,
			TypedSearchResultFilter<S, T> searchResultFilter, Path indexPath) {
		return new IndexSearchService<>(
				createDocumentIndexReaderTemplate(indexPath), toQueryConverter,
				createTypedSearchResultFactory(),
				bestMatchingStrategy,
				searchResultFilter
		);
	}

	public FSIndexCreateService createFSIndexCreateService(
			DocumentsDataSource documentsDatasource, Path indexPath) {
		return FSIndexCreateService.create(documentsDatasource, analyzer, indexPath);
	}

	public DSIndexRestoreService createDSIndexRestoreService(
			DocumentsDataSource documentsDatasource, Path indexPath) {
		return new DSIndexRestoreService(getIdField(typedFieldEnumClass).name(),
				documentsDatasource, createDocumentIndexReaderTemplate(indexPath),
				createFSIndexUpdateService(indexPath));
	}

	public TypedIndexUpdateService<ID, T> createTypedIndexUpdateService(Path indexPath) {
		RawIdToStringConverter<ID> rawIdToStringConverter = RawIdToStringConverter.of(Object::toString);
		return new TypedIndexUpdateService<>(rawIdToStringConverter,
				createTypedToDocumentConverter(), createFSIndexUpdateService(indexPath));
	}

	public FSIndexUpdateService createFSIndexUpdateService(Path indexPath) {
		return FSIndexUpdateService.create(getIdField(typedFieldEnumClass), analyzer, indexPath);
	}

	public TypedIndexReaderTemplate<T> createTypedIndexReaderTemplate(Path indexPath) {
		return new TypedIndexReaderTemplate<>(DocumentToTypedConverter.of(tClass),
				createDocumentIndexReaderTemplate(indexPath));
	}

	private DocumentIndexReaderTemplate createDocumentIndexReaderTemplate(Path indexPath) {
		return new DocumentIndexReaderTemplate(maxResultsPerSearchedItem, indexPath);
	}

	private <S> TypedSearchResultFactory<S, T> createTypedSearchResultFactory() {
		return new TypedSearchResultFactory<>(DocumentToTypedConverter.of(tClass)::convert);
	}

	private TypedToDocumentConverter<T> createTypedToDocumentConverter() {
		return TypedToDocumentConverter.create(analyzer, typedFieldEnumClass);
	}
}
