package ro.go.adrhc.persistence.lucene.typedindex.core.rawtodoc;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.TypedFieldEnum;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.spec.TypedFieldSpecsCollection;
import ro.go.adrhc.persistence.lucene.typedindex.domain.field.spec.TypedLuceneFieldFactory;

import java.util.Optional;
import java.util.function.Function;

import static ro.go.adrhc.persistence.lucene.index.domain.LuceneFieldFactory.storedField;
import static ro.go.adrhc.util.fn.FunctionUtils.sneakyToOptionalResult;

@RequiredArgsConstructor
public class TypedDataToDocumentConverter<T> implements RawToDocumentConverter<T> {
	private static final ObjectMapper MAPPER = Jackson2ObjectMapperBuilder.json().build();
	private static final String RAW_DATA_FIELD = "raw";
	private final TypedLuceneFieldFactory typedFieldFactory;
	private final Function<T, Optional<String>> tStringifier;
	private final TypedFieldSpecsCollection<T> typedFields;

	public static <T, E extends Enum<E> & TypedFieldEnum<T>>
	TypedDataToDocumentConverter<T> create(Analyzer analyzer, Class<E> typedFieldEnumClass) {
		TypedFieldSpecsCollection<T> typedFieldSpecsCollection = TypedFieldSpecsCollection.create(typedFieldEnumClass);
		Function<T, Optional<String>> tStringifier = sneakyToOptionalResult(MAPPER::writeValueAsString);
		TypedLuceneFieldFactory typedLuceneFieldFactory = TypedLuceneFieldFactory.create(analyzer);
		return new TypedDataToDocumentConverter<>(typedLuceneFieldFactory, tStringifier, typedFieldSpecsCollection);
	}

	public static String getRawData(Document doc) {
		return doc.get(RAW_DATA_FIELD);
	}

	@Override
	@NonNull
	public Optional<Document> convert(T tValue) {
		if (tValue == null) {
			return Optional.empty();
		}

		Document doc = new Document();
		typedFields
				.map(tf -> typedFieldFactory.create(tValue, tf))
				.forEach(doc::add);

		Optional<String> rawDataAsJson = tStringifier.apply(tValue);
		if (rawDataAsJson.isEmpty()) {
			return Optional.empty();
		}

		doc.add(storedField(RAW_DATA_FIELD, rawDataAsJson.get()));

		return Optional.of(doc);
	}
}
