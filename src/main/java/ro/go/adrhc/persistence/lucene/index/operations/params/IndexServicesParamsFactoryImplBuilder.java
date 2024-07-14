package ro.go.adrhc.persistence.lucene.index.operations.params;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import ro.go.adrhc.persistence.lucene.core.analysis.AnalyzerFactory;
import ro.go.adrhc.persistence.lucene.core.analysis.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.write.IndexWriterFactory;
import ro.go.adrhc.persistence.lucene.index.operations.search.SearchResultFilter;
import ro.go.adrhc.persistence.lucene.typedcore.Identifiable;
import ro.go.adrhc.persistence.lucene.typedcore.field.LuceneFieldSpec;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.EnumSet;

import static java.util.Objects.requireNonNullElseGet;
import static ro.go.adrhc.persistence.lucene.core.analysis.AnalyzerFactory.NUM_HITS;
import static ro.go.adrhc.persistence.lucene.core.analysis.AnalyzerFactory.defaultAnalyzer;

public class IndexServicesParamsFactoryImplBuilder<
		T extends Identifiable<?>,
		E extends Enum<E> & LuceneFieldSpec<T>> {
	private int searchHits = NUM_HITS;
	private SearchResultFilter<T> searchResultFilter = _ -> true;
	private Class<T> tClass;
	private Collection<? extends LuceneFieldSpec<T>> typedFields;
	private LuceneFieldSpec<T> idField;
	private Path indexPath;
	private Analyzer analyzer;

	public static <T extends Identifiable<?>, E extends Enum<E> & LuceneFieldSpec<T>>
	IndexServicesParamsFactoryImplBuilder<T, E>
	of(Class<T> tClass, Class<E> tFieldEnumClass, Path indexPath) {
		IndexServicesParamsFactoryImplBuilder<T, E> builder =
				new IndexServicesParamsFactoryImplBuilder<>();
		builder.tClass = tClass;
		builder.indexPath = indexPath;
		return builder.tFieldEnumClass(tFieldEnumClass);
	}

	public IndexServicesParamsFactoryImplBuilder<T, E>
	tFieldEnumClass(Class<E> tFieldEnumClass) {
		typedFields = EnumSet.allOf(tFieldEnumClass);
		idField = LuceneFieldSpec.getIdField(tFieldEnumClass);
		return this;
	}

	public IndexServicesParamsFactoryImplBuilder<T, E>
	tokenizerProperties(TokenizerProperties tokenizerProperties) {
		analyzer = defaultAnalyzer(tokenizerProperties);
		return this;
	}

	public IndexServicesParamsFactoryImplBuilder<T, E> searchHits(int searchHits) {
		this.searchHits = searchHits;
		return this;
	}

	public IndexServicesParamsFactoryImplBuilder<T, E> searchResultFilter(
			SearchResultFilter<T> searchResultFilter) {
		this.searchResultFilter = searchResultFilter;
		return this;
	}

	public IndexServicesParamsFactoryImpl<T> build() throws IOException {
		return build(false);
	}

	public IndexServicesParamsFactoryImpl<T> build(boolean readOnly) throws IOException {
		analyzer = requireNonNullElseGet(analyzer, AnalyzerFactory::defaultAnalyzer);
		IndexWriter indexWriter = readOnly ? null : IndexWriterFactory.fsWriter(analyzer,
				indexPath);
		IndexReaderPool indexReaderPool = new IndexReaderPool(
				() -> DirectoryReader.open(FSDirectory.open(indexPath)));
		return new IndexServicesParamsFactoryImpl<>(tClass, idField, indexReaderPool,
				typedFields,
				analyzer, indexWriter, searchHits, searchResultFilter, indexPath);
	}
}
