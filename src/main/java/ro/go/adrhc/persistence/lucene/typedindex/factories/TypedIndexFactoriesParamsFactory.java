package ro.go.adrhc.persistence.lucene.typedindex.factories;

import lombok.experimental.UtilityClass;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.token.props.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.core.write.IndexWriterFactory;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.read.ScoreAndTyped;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.search.SearchResultFilter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.EnumSet;

import static ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexFactoriesParamsDefaults.NUM_HITS;
import static ro.go.adrhc.persistence.lucene.typedindex.factories.TypedIndexFactoriesParamsDefaults.defaultAnalyzer;

@UtilityClass
public class TypedIndexFactoriesParamsFactory {
	/**
	 * defaults: analyzer(TokenizerProperties), numHits, searchResultFilter
	 */
	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>>
	TypedIndexFactoriesParams<ID, T> create(Class<T> tClass, Class<E> tFieldEnumClass,
			TokenizerProperties tokenizerProperties, Path indexPath) throws IOException {
		return create(tClass, tFieldEnumClass, tokenizerProperties, it -> true, indexPath);
	}

	/**
	 * defaults: analyzer(TokenizerProperties), numHits
	 */
	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>>
	TypedIndexFactoriesParams<ID, T> create(Class<T> tClass, Class<E> tFieldEnumClass,
			TokenizerProperties tokenizerProperties,
			SearchResultFilter<ScoreAndTyped<T>> searchResultFilter,
			Path indexPath) throws IOException {
		return create(tClass, tFieldEnumClass, tokenizerProperties,
				searchResultFilter, NUM_HITS, indexPath);
	}

	/**
	 * defaults: analyzer(TokenizerProperties)
	 */
	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>>
	TypedIndexFactoriesParams<ID, T> create(Class<T> tClass, Class<E> tFieldEnumClass,
			TokenizerProperties tokenizerProperties,
			SearchResultFilter<ScoreAndTyped<T>> searchResultFilter,
			int numHits, Path indexPath) throws IOException {
		return create(tClass, tFieldEnumClass,
				defaultAnalyzer(tokenizerProperties),
				searchResultFilter, numHits, indexPath);
	}

	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>>
	TypedIndexFactoriesParams<ID, T> create(Class<T> tClass, Class<E> tFieldEnumClass,
			Analyzer analyzer, SearchResultFilter<ScoreAndTyped<T>> searchResultFilter,
			int numHits, Path indexPath) throws IOException {
		IndexWriter indexWriter = IndexWriterFactory.fsWriter(analyzer, indexPath);
		IndexReaderPool indexReaderPool = new IndexReaderPool(indexWriter);
		TypedField<T> idField = TypedField.getIdField(tFieldEnumClass);
		return new TypedIndexFactoriesParams<>(tClass, EnumSet.allOf(tFieldEnumClass),
				idField, indexWriter.getAnalyzer(), indexWriter, indexReaderPool,
				numHits, searchResultFilter, indexPath);
	}
}
