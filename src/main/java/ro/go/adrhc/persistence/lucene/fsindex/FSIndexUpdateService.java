package ro.go.adrhc.persistence.lucene.fsindex;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import ro.go.adrhc.persistence.lucene.index.IndexUpdateService;
import ro.go.adrhc.persistence.lucene.index.core.write.DocumentIndexWriterTemplate;

import java.nio.file.Path;

import static ro.go.adrhc.persistence.lucene.index.core.write.DocumentIndexWriterTemplate.fsWriterTemplate;

@Slf4j
@Getter
public class FSIndexUpdateService extends IndexUpdateService {
	private final Path indexPath;

	public FSIndexUpdateService(String idFieldName,
			DocumentIndexWriterTemplate indexWriterTemplate, Path indexPath) {
		super(idFieldName, indexWriterTemplate);
		this.indexPath = indexPath;
	}

	/**
	 * removeByIds won't work when idFieldName is null
	 */
	public static FSIndexUpdateService create(Analyzer analyzer, Path indexPath) {
		return new FSIndexUpdateService(null, fsWriterTemplate(analyzer, indexPath), indexPath);
	}

	public static FSIndexUpdateService create(Enum<?> idField, Analyzer analyzer, Path indexPath) {
		return new FSIndexUpdateService(idField.name(),
				fsWriterTemplate(analyzer, indexPath), indexPath);
	}
}
