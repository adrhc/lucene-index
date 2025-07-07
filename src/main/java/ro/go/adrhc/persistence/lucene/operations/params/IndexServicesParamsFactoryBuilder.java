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
		return this;
	}

	public IndexServicesParamsFactoryBuilder<T, E>
	tokenizerProperties(TokenizerProperties tokenizerProperties) {
		this.analyzer = defaultAnalyzer(tokenizerProperties).orElse(null);
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
		useDefaultAnalyzerIfEmpty();
		if (analyzer == null) {
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

	private void useDefaultAnalyzerIfEmpty() {
		if (analyzer == null) {
			this.analyzer = AnalyzerFactory.defaultAnalyzer().orElse(null);
		}
	}
}
