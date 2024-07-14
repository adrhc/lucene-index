package ro.go.adrhc.persistence.lucene.core.bare.analysis;

import org.apache.lucene.analysis.CharFilterFactory;
import org.apache.lucene.analysis.charfilter.MappingCharFilter;
import org.apache.lucene.analysis.charfilter.NormalizeCharMap;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class MappingCharFilterFactory extends CharFilterFactory {
	protected NormalizeCharMap normMap;

	public MappingCharFilterFactory(Map<String, String> args) {
		super(new HashMap<>());
		this.normMap = toNormalizeCharMap(args);
	}

	@Override
	public Reader create(Reader input) {
		return normMap == null ? input : new MappingCharFilter(normMap, input);
	}

	@Override
	public Reader normalize(Reader input) {
		return create(input);
	}

	protected NormalizeCharMap toNormalizeCharMap(Map<String, String> args) {
		if (args.isEmpty()) {
			return null;
		}
		NormalizeCharMap.Builder normalizeCharMapBuilder = new NormalizeCharMap.Builder();
		args.forEach(normalizeCharMapBuilder::add);
		return normalizeCharMapBuilder.build();
	}
}
