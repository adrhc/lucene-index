package ro.go.adrhc.persistence.lucene.operations.params;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import ro.go.adrhc.persistence.lucene.core.bare.analysis.AnalyzerFactory;
import ro.go.adrhc.persistence.lucene.core.bare.analysis.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.bare.write.IndexWriterFactory;
import ro.go.adrhc.persistence.lucene.core.typed.Identifiable;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.operations.search.SearchResultFilter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Optional;

import static ro.go.adrhc.persistence.lucene.core.bare.analysis.AnalyzerFactory.defaultAnalyzer;
import static ro.go.adrhc.util.Slf4jUtils.logError;

@Slf4j
public class IndexServicesParamsFactoryBuilder<
		T extends Identifiable<?>,
		E extends Enum<E> & LuceneFieldSpec<T>> {
	public static final int NUM_HITS = 10;
	private int searchHits = NUM_HITS;
	private SearchResultFilter<T> searchResultFilter = _ -> true;
	private Class<T> tClass;
	private Collection<? extends LuceneFieldSpec<T>> typedFields;
	private LuceneFieldSpec<T> idField;
	private Path indexPath;
	private Analyzer analyzer;
	private boolean failed;

	public static <T extends Identifiable<?>, E extends Enum<E> & LuceneFieldSpec<T>>
	IndexServicesParamsFactoryBuilder<T, E>
	of(Class<T> tClass, Class<E> tFieldEnumClass, Path indexPath) {
		IndexServicesParamsFactoryBuilder<T, E> builder =
				new IndexServicesParamsFactoryBuilder<>();
		builder.tClass = tClass;
		builder.indexPath = indexPath;
		return builder.tFieldEnumClass(tFieldEnumClass);
	}

	public IndexServicesParamsFactoryBuilder<T, E>
	tFieldEnumClass(Class<E> tFieldEnumClass) {
		typedFields = EnumSet.allOf(tFieldEnumClass);
		idField = LuceneFieldSpec.getIdField(tFieldEnumClass);
		return this;
	}

	public IndexServicesParamsFactoryBuilder<T, E>
	tokenizerProperties(TokenizerProperties tokenizerProperties) {
		setAnalyzer(defaultAnalyzer(tokenizerProperties).orElse(null));
		return this;
	}

	public IndexServicesParamsFactoryBuilder<T, E> searchHits(int searchHits) {
		this.searchHits = searchHits;
		return this;
	}

	public IndexServicesParamsFactoryBuilder<T, E> searchResultFilter(
			SearchResultFilter<T> searchResultFilter) {
		this.searchResultFilter = searchResultFilter;
		return this;
	}

	public Optional<IndexServicesParamsFactory<T>> build() {
		return build(false);
	}

	public Optional<IndexServicesParamsFactory<T>> build(boolean readOnly) {
		if (analyzer == null) {
			setAnalyzer(AnalyzerFactory.defaultAnalyzer().orElse(null));
		}
		if (failed) {
			return Optional.empty();
		}
		analyzer = analyzer == null ? AnalyzerFactory.defaultAnalyzer().orElse(null) : analyzer;
		IndexWriter indexWriter;
		try {
			indexWriter = readOnly ? null : IndexWriterFactory.fsWriter(analyzer, indexPath);
		} catch (IOException e) {
			logError(log, e);
			return Optional.empty();
		}
		IndexReaderPool indexReaderPool = new IndexReaderPool(
				() -> DirectoryReader.open(FSDirectory.open(indexPath)));
		return Optional.of(new IndexServicesParamsFactoryImpl<>(tClass, idField, indexReaderPool,
				typedFields, analyzer, indexWriter, searchHits, searchResultFilter, indexPath));
	}

	private void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
		failed = analyzer == null;
	}
}
