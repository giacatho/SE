package assignment2a.indexer;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import se.constant.Constants;

/**
 * Input: text file containing DBLP items Output: a Lucene index and index
 * statistics
 *
 * @author a
 */
public class A2aDblpIndexBuilder {

	Map<String, List<String>> yearToTitleList;
    private IndexWriter writer;

	public A2aDblpIndexBuilder (Map<String, List<String>> yearToTitleList) {
		this.yearToTitleList = yearToTitleList;
	}
	
	public void buildIndex() throws IOException {
		 // Title is all English
		System.out.println("Stop word set " + new EnglishAnalyzer().getStopwordSet());
		
		// English Analyzer support stop words with stemming
        initIndexWriter(Constants.INDEX_ASSIGNMENT2A_DIR, new EnglishAnalyzer());
		
		for (Map.Entry<String, List<String>> entrySet : yearToTitleList.entrySet()) {
			String year = entrySet.getKey();
			List<String> titleList = entrySet.getValue();

			this.addToIndex(year, titleList.toString()); 
		}
		
		this.close();
    }

    private void initIndexWriter(String storePath, Analyzer analyzer) throws IOException {
        Path pathIndexStore = Paths.get(storePath);
        Directory indexStoreDir = FSDirectory.open(pathIndexStore);
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        this.writer = new IndexWriter(indexStoreDir, iwc);
    }

    private void addToIndex(String year, String titles) throws IOException {
		System.out.println("Add to index for year " + year);
		Document doc = new Document();
        
		// In this assignment, term vector needs to be stored but the title itself does not
		FieldType myFieldType = new FieldType(TextField.TYPE_NOT_STORED);
		myFieldType.setStoreTermVectors(true);
		
        doc.add(new Field("title", titles, myFieldType));
		
		doc.add(new StringField("pubyear", year, Field.Store.YES));
		
        this.writer.addDocument(doc);
    }
	
    private void close() throws IOException {
        this.writer.close();
    }

}
