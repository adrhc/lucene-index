package ro.go.adrhc.persistence.lucene.index.person;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.*;
import ro.go.adrhc.persistence.lucene.index.core.tokenizer.TokenizationUtils;
import ro.go.adrhc.persistence.lucene.typedindex.domain.RawToDocumentConverter;

import java.util.Optional;

@RequiredArgsConstructor
public class PersonToDocumentConverter implements RawToDocumentConverter<Person> {
	private final TokenizationUtils tokenizationUtils;

	@Override
	@NonNull
	public Optional<Document> convert(Person person) {
		if (person == null) {
			return Optional.empty();
		}
		Document document = new Document();
		// A field that is indexed but not tokenized: the entire String value is indexed as a single
		// token. For example this might be used for a 'country' field or an 'id' field. If you also
		// need to sort on this field, separately add a SortedDocValuesField to your document.
		document.add(new StringField(PersonFields.id.name(), person.id(), Field.Store.YES));
		// A field that is indexed and tokenized, without term vectors. For example this
		// would be used on a 'body' field, that contains the bulk of a document's text.
		document.add(new TextField(PersonFields.name.name(), person.name(), Field.Store.YES));
		document.add(new StringField(PersonFields.oneTokenName.name(),
				tokenizationUtils.normalize(PersonFields.name, person.name()), Field.Store.YES));
		// Field that indexes a per-document String or BytesRef into an inverted index
		// for fast filtering, stores values in a columnar fashion using DocValuesType.SORTED_SET
		// doc values for sorting and faceting, and optionally stores values as stored fields for
		// top-hits retrieval. This field does not support scoring: queries produce constant scores.
		// If you need more fine-grained control you can use StringField, SortedDocValuesField
		// or SortedSetDocValuesField, and StoredField.
		document.add(new KeywordField(PersonFields.cnp.name(), person.cnp(), Field.Store.YES));
		return Optional.of(document);
	}
}
