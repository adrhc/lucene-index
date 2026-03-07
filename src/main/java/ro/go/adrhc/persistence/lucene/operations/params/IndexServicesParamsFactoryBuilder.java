package ro.go.adrhc.persistence.lucene.operations.params;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.bare.analysis.AnalyzerFactory;
import ro.go.adrhc.persistence.lucene.core.bare.analysis.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.core.bare.field.FieldType;
import ro.go.adrhc.persistence.lucene.core.bare.read.IndexReaderPoolFactory;
import ro.go.adrhc.persistence.lucene.core.bare.write.IndexWriterFactory;
import ro.go.adrhc.persistence.lucene.core.typed.Identifiable;
import ro.go.adrhc.persistence.lucene.core.typed.field.LuceneFieldSpec;
import ro.go.adrhc.persistence.lucene.core.typed.field.RawFieldValueSerdes;
import ro.go.adrhc.persistence.lucene.operations.search.SearchResultFilter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

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
	private TokenizerProperties tokenizerProperties;
	private Analyzer analyzer;
	private RawFieldValueSerdes<T> rawFieldValueSerdes;
	private Function<Analyzer, Optional<IndexWriter>> indexWriterFactory;

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
		this.tokenizerProperties = tokenizerProperties;
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

	public IndexServicesParamsFactoryBuilder<T, E>
	indexWriterFactory(Function<Analyzer, Optional<IndexWriter>> indexWriterFactory) {
		this.indexWriterFactory = indexWriterFactory;
		return this;
	}

	public IndexServicesParamsFactoryBuilder<T, E>
	rawFieldValueSerdes(RawFieldValueSerdes<T> rawFieldValueSerdes) {
		this.rawFieldValueSerdes = rawFieldValueSerdes;
		return this;
	}

	public Optional<IndexServicesParamsFactory<T>> build() {
		return build(false);
	}

	public Optional<IndexServicesParamsFactory<T>> build(boolean readOnly) {
		rawFieldValueSerdes = rawFieldValueSerdes == null
			? RawFieldValueSerdes.create(tClass) : rawFieldValueSerdes;
		Analyzer finalAnalyzer = perFieldAnalyzer(analyzer());
		if (readOnly) {
			return Optional.of(new IndexServicesParamsFactoryImpl<>(
				tClass, idField, typedFields, finalAnalyzer, IndexReaderPoolFactory.of(indexPath),
				null, rawFieldValueSerdes, searchResultFilter, indexPath, searchHits));
		} else {
			return createIndexWriter(finalAnalyzer)
				.map(indexWriter -> new IndexServicesParamsFactoryImpl<>(
					tClass, idField, typedFields, finalAnalyzer, IndexReaderPoolFactory.of(indexWriter),
					indexWriter, rawFieldValueSerdes, searchResultFilter, indexPath, searchHits));
		}
	}

	private Analyzer perFieldAnalyzer(Analyzer baseAnalyzer) {
		if (typedFields == null ||
			typedFields.stream().noneMatch(t -> t.fieldType() == FieldType.WORD)) {
			return baseAnalyzer;
		}
		Analyzer wordAnalyzer = wordAnalyzer();
		Map<String, Analyzer> overrides = new HashMap<>();
		for (LuceneFieldSpec<T> fieldSpec : typedFields) {
			if (fieldSpec.fieldType() == FieldType.WORD) {
				overrides.put(fieldSpec.name(), wordAnalyzer);
			}
		}
		return new PerFieldAnalyzerWrapper(baseAnalyzer, overrides);
	}

	private Optional<IndexWriter> createIndexWriter(Analyzer analyzer) {
		try {
			if (indexWriterFactory != null) {
				return indexWriterFactory.apply(analyzer);
			} else {
				return Optional.of(IndexWriterFactory.fsWriter(analyzer, indexPath));
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return Optional.empty();
	}

	private Analyzer wordAnalyzer() {
		return AnalyzerFactory.defaultWordAnalyzer(tokenizerProperties())
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
