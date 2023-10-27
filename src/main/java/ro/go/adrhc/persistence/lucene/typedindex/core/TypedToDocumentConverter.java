package ro.go.adrhc.persistence.lucene.typedindex.core;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.core.docds.datasource.RawToDocumentConverter;
import ro.go.adrhc.persistence.lucene.index.core.docds.rawds.Identifiable;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedFieldEnum;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.spec.TypedFieldFactory;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.spec.TypedFieldSpecsCollection;

import java.util.Optional;
import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.index.domain.field.FieldFactory.storedField;
import static ro.go.adrhc.persistence.lucene.typedindex.core.ObjectMapperFactory.JSON_MAPPER;
import static ro.go.adrhc.util.fn.FunctionUtils.sneakyToOptionalResult;

@RequiredArgsConstructor
public class TypedToDocumentConverter<T extends Identifiable<?>> implements RawToDocumentConverter<T> {
	private static final String RAW_DATA_FIELD = "raw";
	private final TypedFieldFactory typedFieldFactory;
	private final Function<T, Optional<String>> tStringifier;
	private final TypedFieldSpecsCollection<T> typedFields;

	public static <T extends Identifiable<?>, E extends Enum<E> & TypedFieldEnum<T>>
	TypedToDocumentConverter<T> create(Analyzer analyzer, Class<E> typedFieldEnumClass) {
		TypedFieldSpecsCollection<T> typedFieldSpecsCollection = TypedFieldSpecsCollection.create(typedFieldEnumClass);
		Function<T, Optional<String>> tStringifier = sneakyToOptionalResult(JSON_MAPPER::writeValueAsString);
		TypedFieldFactory typedFieldFactory = TypedFieldFactory.create(analyzer);
		return new TypedToDocumentConverter<>(typedFieldFactory, tStringifier, typedFieldSpecsCollection);
	}

	public static String getRawData(Document doc) {
		return doc.get(RAW_DATA_FIELD);
	}

	@Override
	@NonNull
	public Optional<Document> convert(T tValue) {
		if (tValue == null || !tValue.hasId()) {
			return Optional.empty();
		}

		Document doc = new Document();
		typedFields
				.map(tf -> typedFieldFactory.create(tValue, tf))
				.flatMap(Optional::stream)
				.forEach(doc::add);

		Optional<String> rawDataAsJson = tStringifier.apply(tValue);
		if (rawDataAsJson.isEmpty()) {
			return Optional.empty();
		}

		doc.add(storedField(RAW_DATA_FIELD, rawDataAsJson.get()));

		return Optional.of(doc);
	}
}
