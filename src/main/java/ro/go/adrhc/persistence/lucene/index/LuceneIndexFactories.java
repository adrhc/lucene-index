package ro.go.adrhc.persistence.lucene.index;

import lombok.experimental.UtilityClass;
import org.apache.lucene.analysis.Analyzer;

import static ro.go.adrhc.persistence.lucene.index.core.write.DocumentIndexWriterTemplate.ramWriterTemplate;

@UtilityClass
public class LuceneIndexFactories {
	public static LuceneIndex createRAMIndex(Enum<?> idField, Analyzer analyzer) {
		return new LuceneIndex(idField.name(), ramWriterTemplate(analyzer));
	}
}
