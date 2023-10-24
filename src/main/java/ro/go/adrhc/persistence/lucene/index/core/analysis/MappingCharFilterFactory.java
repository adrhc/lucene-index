package ro.go.adrhc.persistence.lucene.index.core.analysis;

import org.apache.lucene.analysis.charfilter.NormalizeCharMap;
import org.apache.lucene.analysis.util.ResourceLoader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MappingCharFilterFactory extends org.apache.lucene.analysis.charfilter.MappingCharFilterFactory {
	private final Map<String, String> args;

	public MappingCharFilterFactory(Map<String, String> args) {
		super(new HashMap<>());
		this.args = Collections.unmodifiableMap(new HashMap<>(args));
	}

	@Override
	public void inform(ResourceLoader loader) {
		if (args.isEmpty()) {
			return;
		}
		NormalizeCharMap.Builder normalizeCharMapBuilder = new NormalizeCharMap.Builder();
		args.forEach(normalizeCharMapBuilder::add);
		normMap = normalizeCharMapBuilder.build();
	}
}
