package ro.go.adrhc.persistence.lucene.core.analysis;

import org.apache.lucene.analysis.CharFilterFactory;
import org.apache.lucene.analysis.pattern.PatternReplaceCharFilter;

import java.io.Reader;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PatternReplaceCharFilterFactory extends CharFilterFactory {
	public static final String PATTERN = "pattern";
	public static final String REPLACEMENT = "replacement";
	public static final String FLAGS = "flags";
	private final Pattern pattern;
	private final String replacement;

	public PatternReplaceCharFilterFactory(Map<String, String> args) {
		super(args);
		pattern = createPattern(args);
		replacement = get(args, REPLACEMENT, "");
		if (!args.isEmpty()) {
			throw new IllegalArgumentException("Unknown parameters: " + args);
		}
	}

	@Override
	public Reader create(Reader input) {
		return new PatternReplaceCharFilter(pattern, replacement, input);
	}

	@Override
	public Reader normalize(Reader input) {
		return create(input);
	}

	protected Pattern createPattern(Map<String, String> args) {
		try {
			return Pattern.compile(require(args, PATTERN), getInt(args, FLAGS, 0));
		} catch (PatternSyntaxException e) {
			throw new IllegalArgumentException
					("Configuration Error: '" + PATTERN + "' can not be parsed in " +
							this.getClass().getSimpleName(), e);
		}
	}
}
