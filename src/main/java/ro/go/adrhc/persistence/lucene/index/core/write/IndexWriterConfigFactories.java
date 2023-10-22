package ro.go.adrhc.persistence.lucene.index.core.write;

import lombok.experimental.UtilityClass;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriterConfig;

@UtilityClass
public class IndexWriterConfigFactories {
	public static IndexWriterConfig createOrAppend(Analyzer analyzer) {
		return new IndexWriterConfig(analyzer)
				.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
	}
}
