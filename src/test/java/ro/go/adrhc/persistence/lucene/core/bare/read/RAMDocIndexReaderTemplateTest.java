package ro.go.adrhc.persistence.lucene.core.bare.read;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RAMDocIndexReaderTemplateTest extends AbstractRAMDocIndexTest {
	@Test
	void useRAMReader() throws IOException {
		useRAMIndex((r, w) -> {
			DocIndexReaderTemplate tmpl = DocIndexReaderTemplateFactory.of(r);
			Integer count = tmpl.useReader(DocIndexReader::count);
			assertEquals(0, count);
		});
	}
}
