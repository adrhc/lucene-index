package ro.go.adrhc.persistence.lucene;

import lombok.experimental.UtilityClass;
import org.apache.lucene.analysis.Analyzer;

import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.write.DocumentIndexWriterTemplate.fsWriterTemplate;
import static ro.go.adrhc.persistence.lucene.write.DocumentIndexWriterTemplate.ramWriterTemplate;

@UtilityClass
public class LuceneIndexFactories {
	public static LuceneIndex createRAMIndex(Enum<?> idField, Analyzer analyzer) {
		return new LuceneIndex(idField.name(), ramWriterTemplate(analyzer));
	}

	public static FSLuceneIndex createFSIndex(Enum<?> idField, Analyzer analyzer, Path indexPath) {
		return new FSLuceneIndex(idField.name(),
				fsWriterTemplate(analyzer, indexPath), indexPath);
	}
}
