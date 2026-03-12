package ro.go.adrhc.persistence.lucene.operation;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.bare.analysis.AnalyzerFactory;
import ro.go.adrhc.persistence.lucene.core.bare.analysis.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.core.bare.field.FieldType;
import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPoolFactory;
import ro.go.adrhc.persistence.lucene.core.bare.write.DocIndexWriterFactory;
import ro.go.adrhc.persistence.lucene.core.typed.Identifiable;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.field.RawFieldValueSerdes;
import ro.go.adrhc.persistence.lucene.service.search.SearchResultFilter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

import static ro.go.adrhc.util.fn.FunctionUtils.failToEmpty;

@Slf4j
public class IndexOperationsParamsBuilder<
	T extends Identifiable<?>, E extends Enum<E> & LuceneFieldSpec<T>> {
	private SearchResultFilter<T> searchResultFilter = it -> true;
	private Class<T> tClass;
	private Collection<? extends LuceneFieldSpec<T>> typedFields;
	private LuceneFieldSpec<T> idField;
	private Path indexPath;
	private TokenizerProperties tokenizerProperties;
	private Analyzer analyzer;
	private Analyzer wordAnalyzer;
	private RawFieldValueSerdes<T> rawFieldValueSerdes;
	private Function<Analyzer, Optional<IndexWriter>> indexWriterFactory;

	public static <T extends Identifiable<?>, E extends Enum<E> & LuceneFieldSpec<T>>
	IndexOperationsParamsBuilder<T, E> of(Class<T> tClass, Class<E> tFieldEnumClass, Path indexPath) {
		return of(tClass, tFieldEnumClass).indexPath(indexPath);
	}

	/**
	 * If indexPath is not later set, a RAMDirectory will be used for the index.
	 */
	public static <T extends Identifiable<?>, E extends Enum<E> & LuceneFieldSpec<T>>
	IndexOperationsParamsBuilder<T, E> of(Class<T> tClass, Class<E> tFieldEnumClass) {
		IndexOperationsParamsBuilder<T, E> builder = new IndexOperationsParamsBuilder<>();
		builder.tClass = tClass;
		return builder.tFieldEnumClass(tFieldEnumClass);
	}

	public IndexOperationsParamsBuilder<T, E> indexPath(Path indexPath) {
		this.indexPath = indexPath;
		return this;
	}

	public IndexOperationsParamsBuilder<T, E>
	tFieldEnumClass(Class<E> tFieldEnumClass) {
		typedFields = EnumSet.allOf(tFieldEnumClass);
		idField = LuceneFieldSpec.getIdField(tFieldEnumClass);
		return this;
	}

	public IndexOperationsParamsBuilder<T, E> analyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
		return this;
	}

	public IndexOperationsParamsBuilder<T, E> wordAnalyzer(Analyzer analyzer) {
		this.wordAnalyzer = analyzer;
		return this;
	}

	/**
	 * @param tokenizerProperties is used to construct the "default" and "word" analyzers (if not set explicitly)
	 */
	public IndexOperationsParamsBuilder<T, E>
	tokenizerProperties(TokenizerProperties tokenizerProperties) {
		this.tokenizerProperties = tokenizerProperties;
		return this;
	}

	public IndexOperationsParamsBuilder<T, E> searchResultFilter(
		SearchResultFilter<T> searchResultFilter) {
		this.searchResultFilter = searchResultFilter;
		return this;
	}

	public IndexOperationsParamsBuilder<T, E>
	indexWriterFactory(Function<Analyzer, Optional<IndexWriter>> indexWriterFactory) {
		this.indexWriterFactory = indexWriterFactory;
		return this;
	}

	public IndexOperationsParamsBuilder<T, E>
	rawFieldValueSerdes(RawFieldValueSerdes<T> rawFieldValueSerdes) {
		this.rawFieldValueSerdes = rawFieldValueSerdes;
		return this;
	}

	public Optional<IndexOperationsParams<T>> build() {
		return build(false);
	}

	public Optional<IndexOperationsParams<T>> build(boolean readOnly) {
		rawFieldValueSerdes = rawFieldValueSerdes == null
			? RawFieldValueSerdes.create(tClass) : rawFieldValueSerdes;
		Analyzer finalAnalyzer = perFieldAnalyzer(analyzer());
		if (readOnly) {
			return Optional.of(new IndexOperationsParamsImpl<>(
				tClass, idField, typedFields, finalAnalyzer, IndexReaderPoolFactory.of(indexPath),
				null, rawFieldValueSerdes, searchResultFilter, indexPath));
		} else {
			return createIndexWriter(finalAnalyzer)
				.map(indexWriter -> new IndexOperationsParamsImpl<>(
					tClass, idField, typedFields, finalAnalyzer, IndexReaderPoolFactory
					.of(indexWriter), indexWriter, rawFieldValueSerdes, searchResultFilter, indexPath));
		}
	}

	private Analyzer perFieldAnalyzer(Analyzer defaultAnalyzer) {
		if (typedFields == null ||
			typedFields.stream().noneMatch(t -> t.fieldType() == FieldType.WORD)) {
			return defaultAnalyzer;
		}
		Analyzer finalWordAnalyzer = wordAnalyzer();
		Map<String, Analyzer> overrides = new HashMap<>();
		for (LuceneFieldSpec<T> fieldSpec : typedFields) {
			if (fieldSpec.fieldType() == FieldType.WORD) {
				overrides.put(fieldSpec.name(), finalWordAnalyzer);
			}
		}
		return new PerFieldAnalyzerWrapper(defaultAnalyzer, overrides);
	}

	private Optional<IndexWriter> createIndexWriter(Analyzer analyzer) {
		try {
			if (indexWriterFactory != null) {
				return indexWriterFactory.apply(analyzer);
			} else if (indexPath != null) {
				return Optional.of(DocIndexWriterFactory.fsWriter(analyzer, indexPath));
			} else {
				return failToEmpty(DocIndexWriterFactory::ramWriter, analyzer);
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return Optional.empty();
	}

	private Analyzer wordAnalyzer() {
		return wordAnalyzer != null ? wordAnalyzer :
			AnalyzerFactory.defaultWordAnalyzer(tokenizerProperties())
				.orElseThrow(() -> new IllegalStateException("Default WORD Analyzer can't be created!"));
	}

	private Analyzer analyzer() {
		return analyzer != null ? analyzer :
			AnalyzerFactory.defaultAnalyzer(tokenizerProperties())
				.orElseThrow(() -> new IllegalStateException("Default Analyzer can't be created!"));
	}

	private TokenizerProperties tokenizerProperties() {
		return tokenizerProperties != null ? tokenizerProperties : new TokenizerProperties();
	}
}
