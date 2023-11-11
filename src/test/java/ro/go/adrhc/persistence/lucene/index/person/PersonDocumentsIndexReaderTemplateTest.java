package ro.go.adrhc.persistence.lucene.index.person;

import org.junit.jupiter.api.Test;
import ro.go.adrhc.persistence.lucene.core.field.FieldAccessors;
import ro.go.adrhc.persistence.lucene.core.read.DocumentsIndexReaderTemplate;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ro.go.adrhc.persistence.lucene.index.person.PersonFieldType.ID_QUERIES;

public class PersonDocumentsIndexReaderTemplateTest extends AbstractPersonsIndexTest {
	@Test
	void readTest() throws IOException {
		DocumentsIndexReaderTemplate readerTemplate = createDocumentsIndexReaderTemplate();
		Optional<Number> optionalDocument = readerTemplate
				.findById(ID_QUERIES.longEquals(1))
				.map(doc -> FieldAccessors.numericValue(PersonFieldType.id, doc));
		assertThat(optionalDocument).isPresent();
		assertThat(optionalDocument.get().longValue()).isEqualTo(1);
	}
}
