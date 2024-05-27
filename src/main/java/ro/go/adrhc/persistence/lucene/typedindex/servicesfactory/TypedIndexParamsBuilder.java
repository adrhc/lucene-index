package ro.go.adrhc.persistence.lucene.typedindex.servicesfactory;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.token.props.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.core.write.IndexWriterFactory;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedcore.serde.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.search.SearchResultFilter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.EnumSet;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNullElseGet;
import static ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.AnalyzerFactory.NUM_HITS;
import static ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.AnalyzerFactory.defaultAnalyzer;

public class TypedIndexParamsBuilder<ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>> {
	private int numHits = NUM_HITS;
	private SearchResultFilter<T> searchResultFilter = _ -> true;
	private Class<T> tClass;
	private Collection<? extends TypedField<T>> typedFields;
	private TypedField<T> idField;
	private Path indexPath;
	private Analyzer analyzer;
	private Predicate<ID> shouldKeepIndexedButMissingFromDS = _ -> false;

	public static <ID, T extends Identifiable<ID>, E extends Enum<E> & TypedField<T>>
	TypedIndexParamsBuilder<ID, T, E>
	of(Class<T> tClass, Class<E> tFieldEnumClass, Path indexPath) {
		TypedIndexParamsBuilder<ID, T, E> builder =
				new TypedIndexParamsBuilder<>();
		builder.tClass = tClass;
		builder.indexPath = indexPath;
		return builder.tFieldEnumClass(tFieldEnumClass);
	}

	public TypedIndexParamsBuilder<ID, T, E>
	tFieldEnumClass(Class<E> tFieldEnumClass) {
		typedFields = EnumSet.allOf(tFieldEnumClass);
		idField = TypedField.getIdField(tFieldEnumClass);
		return this;
	}

	public TypedIndexParamsBuilder<ID, T, E>
	tokenizerProperties(TokenizerProperties tokenizerProperties) {
		analyzer = defaultAnalyzer(tokenizerProperties);
		return this;
	}

	public TypedIndexParamsBuilder<ID, T, E> numHits(int numHits) {
		this.numHits = numHits;
		return this;
	}

	public TypedIndexParamsBuilder<ID, T, E> searchResultFilter(
			SearchResultFilter<T> searchResultFilter) {
		this.searchResultFilter = searchResultFilter;
		return this;
	}

	public TypedIndexParamsBuilder<ID, T, E> shouldKeepIndexedButMissingFromDS(
			Predicate<ID> shouldKeepIndexedButMissingFromDS) {
		this.shouldKeepIndexedButMissingFromDS = shouldKeepIndexedButMissingFromDS;
		return this;
	}

	public TypedIndexParams<ID, T> build() throws IOException {
		return build(false);
	}

	public TypedIndexParams<ID, T> build(boolean readOnly) throws IOException {
		analyzer = requireNonNullElseGet(analyzer, AnalyzerFactory::defaultAnalyzer);
		IndexWriter indexWriter = readOnly ? null : IndexWriterFactory.fsWriter(analyzer, indexPath);
		IndexReaderPool indexReaderPool = new IndexReaderPool(
				() -> DirectoryReader.open(FSDirectory.open(indexPath)));
		return new TypedIndexParamsImpl<>(tClass, typedFields, idField, analyzer,
				indexWriter, indexReaderPool, numHits, searchResultFilter, indexPath,
				shouldKeepIndexedButMissingFromDS);
	}
}
