package ro.go.adrhc.persistence.lucene.core.typed.write;

import java.io.IOException;

public interface IndexServiceOperations {
	void commit() throws IOException;
}
