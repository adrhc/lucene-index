package ro.go.adrhc.persistence.lucene.typedindex.servicesfactory;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import ro.go.adrhc.persistence.lucene.core.read.IndexReaderPool;
import ro.go.adrhc.persistence.lucene.core.token.props.TokenizerProperties;
import ro.go.adrhc.persistence.lucene.core.write.IndexWriterFactory;
import ro.go.adrhc.persistence.lucene.typedcore.Identifiable;
import ro.go.adrhc.persistence.lucene.typedcore.field.TypedField;
import ro.go.adrhc.persistence.lucene.typedindex.search.SearchResultFilter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.EnumSet;

import static java.util.Objects.requireNonNullElseGet;
import static ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.AnalyzerFactory.NUM_HITS;
import static ro.go.adrhc.persistence.lucene.typedindex.servicesfactory.AnalyzerFactory.defaultAnalyzer;

public class TypedIndexParamsBuilder<T extends Identifiable<?>, E extends Enum<E> & TypedField<T>> {
	private int searchHits = NUM_HITS;
	private SearchResultFilter<T> searchResultFilter = _ -> true;
	private Class<T> tClass;
	private Collection<? extends TypedField<T>> typedFields;
	private TypedField<T> idField;
	private Path indexPath;
	private Analyzer analyzer;

	public static <T extends Identifiable<?>, E extends Enum<E> & TypedField<T>>
	TypedIndexParamsBuilder<T, E>
	of(Class<T> tClass, Class<E> tFieldEnumClass, Path indexPath) {
		TypedIndexParamsBuilder<T, E> builder =
				new TypedIndexParamsBuilder<>();
		builder.tClass = tClass;
		builder.indexPath = indexPath;
		return builder.tFieldEnumClass(tFieldEnumClass);
	}

	public TypedIndexParamsBuilder<T, E>
	tFieldEnumClass(Class<E> tFieldEnumClass) {
		typedFields = EnumSet.allOf(tFieldEnumClass);
		idField = TypedField.getIdField(tFieldEnumClass);
		return this;
	}

	public TypedIndexParamsBuilder<T, E>
	tokenizerProperties(TokenizerProperties tokenizerProperties) {
		analyzer = defaultAnalyzer(tokenizerProperties);
		return this;
	}

	public TypedIndexParamsBuilder<T, E> searchHits(int searchHits) {
		this.searchHits = searchHits;
		return this;
	}

	public TypedIndexParamsBuilder<T, E> searchResultFilter(
			SearchResultFilter<T> searchResultFilter) {
		this.searchResultFilter = searchResultFilter;
		return this;
	}

	public TypedIndexParams<T> build() throws IOException {
		return build(false);
	}

	public TypedIndexParams<T> build(boolean readOnly) throws IOException {
		analyzer = requireNonNullElseGet(analyzer, AnalyzerFactory::defaultAnalyzer);
		IndexWriter indexWriter = readOnly ? null : IndexWriterFactory.fsWriter(analyzer, indexPath);
		IndexReaderPool indexReaderPool = new IndexReaderPool(
				() -> DirectoryReader.open(FSDirectory.open(indexPath)));
		return new TypedIndexParamsImpl<>(tClass, idField, indexReaderPool, typedFields,
				analyzer, indexWriter, searchHits, searchResultFilter, indexPath);
	}
}
