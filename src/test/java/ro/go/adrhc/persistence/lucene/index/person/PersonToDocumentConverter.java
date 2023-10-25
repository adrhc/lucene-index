package ro.go.adrhc.persistence.lucene.index.person;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import ro.go.adrhc.persistence.lucene.index.domain.FieldFactory;
import ro.go.adrhc.persistence.lucene.typedindex.domain.RawToDocumentConverter;

import java.util.Optional;

import static ro.go.adrhc.persistence.lucene.index.domain.FieldFactory.storedKeywordField;
import static ro.go.adrhc.persistence.lucene.index.domain.FieldFactory.storedTextField;

@RequiredArgsConstructor
public class PersonToDocumentConverter implements RawToDocumentConverter<Person> {
	private final FieldFactory fieldFactory;

	@Override
	@NonNull
	public Optional<Document> convert(Person person) {
		if (person == null) {
			return Optional.empty();
		}
		Document document = new Document();
		document.add(storedKeywordField(PersonFields.id, person.id()));
		document.add(storedKeywordField(PersonFields.cnp, person.cnp()));
		document.add(storedTextField(PersonFields.name, person.name()));
		document.add(fieldFactory.storedStringField(PersonFields.oneTokenName, person.name()));
		return Optional.of(document);
	}
}
