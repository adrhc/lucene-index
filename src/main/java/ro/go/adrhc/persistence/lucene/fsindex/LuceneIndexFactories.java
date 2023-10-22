package ro.go.adrhc.persistence.lucene.fsindex;

import lombok.experimental.UtilityClass;
import org.apache.lucene.analysis.Analyzer;

import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.index.core.write.DocumentIndexWriterTemplate.fsWriterTemplate;

@UtilityClass
public class LuceneIndexFactories {
	public static FSLuceneIndex createFSIndex(Enum<?> idField, Analyzer analyzer, Path indexPath) {
		return new FSLuceneIndex(idField.name(),
				fsWriterTemplate(analyzer, indexPath), indexPath);
	}
}
