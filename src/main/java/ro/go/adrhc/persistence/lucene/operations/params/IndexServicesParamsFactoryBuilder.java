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

@Slf4j
public class IndexServicesParamsFactoryBuilder<
		T extends Identifiable<?>,
		E extends Enum<E> & LuceneFieldSpec<T>> {
	public static final int NUM_HITS = 10;
	private int searchHits = NUM_HITS;
	private SearchResultFilter<T> searchResultFilter = it -> true;
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

	public IndexServicesParamsFactoryBuilder<T, E> analyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
		failed = false;
		return this;
	}

	public IndexServicesParamsFactoryBuilder<T, E>
	tokenizerProperties(TokenizerProperties tokenizerProperties) {
		setNotNullAnalyzerOrFail(defaultAnalyzer(tokenizerProperties).orElse(null));
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
		if (failed) {
			return Optional.empty();
		}
		if (!prepareAnalyzer()) {
			return Optional.empty();
		}
		if (readOnly) {
			return Optional.of(new IndexServicesParamsFactoryImpl<>(
					tClass, idField, createIndexReaderPool(), typedFields, analyzer,
					null, searchHits, searchResultFilter, indexPath));
		} else {
			return createIndexWriter().map(indexWriter -> new IndexServicesParamsFactoryImpl<>(
					tClass, idField, createIndexReaderPool(), typedFields, analyzer,
					indexWriter, searchHits, searchResultFilter, indexPath));
		}
	}

	private IndexReaderPool createIndexReaderPool() {
		return new IndexReaderPool(() -> {
			FSDirectory directory = FSDirectory.open(indexPath);
			if (DirectoryReader.indexExists(directory)) {
				return DirectoryReader.open(directory);
			} else {
				log.warn("\n{} is an empty index!", indexPath);
				return null;
			}
		});
	}

	private Optional<IndexWriter> createIndexWriter() {
		try {
			return Optional.of(IndexWriterFactory.fsWriter(analyzer, indexPath));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return Optional.empty();
	}

	private boolean prepareAnalyzer() {
		if (analyzer == null) {
			setNotNullAnalyzerOrFail(AnalyzerFactory.defaultAnalyzer().orElse(null));
		}
		return !failed;
	}

	private void setNotNullAnalyzerOrFail(Analyzer analyzer) {
		this.analyzer = analyzer;
		failed = analyzer == null;
	}
}
