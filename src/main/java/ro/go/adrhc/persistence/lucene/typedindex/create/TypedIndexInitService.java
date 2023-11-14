package ro.go.adrhc.persistence.lucene.typedindex.create;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexAdderTemplate;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemover;
import ro.go.adrhc.util.collection.StreamCounter;

import java.io.IOException;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class TypedIndexInitService<ID, T> implements IndexInitService<T> {
	private final TypedIndexRemover<ID> indexRemover;
	private final TypedIndexAdderTemplate<T> indexAdderTemplate;

	/**
	 * constructor parameters union
	 */
	public static <ID, T> TypedIndexInitService<ID, T>
	create(TypedIndexInitServiceParams<T> params) {
		return new TypedIndexInitService<>(
				TypedIndexRemover.create(params),
				TypedIndexAdderTemplate.create(params));
	}

	public void initialize(Stream<T> tStream) throws IOException {
		log.debug("\nremoving all ...");
		// no IndexWriter flush
		indexRemover.removeAll();
		log.debug("\nadding all ...");
		StreamCounter counter = new StreamCounter();
		// with IndexWriter flush
		indexAdderTemplate.useAdder(adder -> adder.addMany(counter.countedStream(tStream)));
		log.debug("\nadded {} items", counter.getCount());
	}
}
