package ro.go.adrhc.persistence.lucene.typedindex.create;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexAdderTemplate;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemover;
import ro.go.adrhc.persistence.lucene.typedcore.write.TypedIndexRemoverTemplate;
import ro.go.adrhc.util.collection.StreamCounter;

import java.io.IOException;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class TypedIndexCreateService<ID, T> implements IndexCreateService<T> {
	private final TypedIndexAdderTemplate<T> indexAdderTemplate;
	private final TypedIndexRemoverTemplate<ID> indexRemoverTemplate;

	/**
	 * constructor parameters union
	 */
	public static <ID, T> TypedIndexCreateService<ID, T>
	create(TypedIndexCreateServiceParams<T> params) {
		return new TypedIndexCreateService<>(
				TypedIndexAdderTemplate.create(params),
				TypedIndexRemoverTemplate.create(params));
	}

	public void createOrReplace(Stream<T> tStream) throws IOException {
		log.debug("\nremoving all ...");
		indexRemoverTemplate.useRemover(TypedIndexRemover::removeAll);
		log.debug("\nadding all ...");
		StreamCounter counter = new StreamCounter();
		indexAdderTemplate.useAdder(adder -> adder.addMany(counter.countedStream(tStream)));
		log.debug("\nadded {} items", counter.getCount());
	}
}
