/**
 * Author: Liu Hongzhi
 * Email: liuh0051@e.ntu.edu.sg
 * Date 12 Mar, 2016
 *
 * Run in Command Line
 * set path=%path%;C:\Program Files\Java\jdk1.8.0_73\bin
 * set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_73
 * set CLASSPATH=.;%JAVA_HOME%\lib;C:\hz\education\NTU\Projects\IR\lucene_lib\lucene-core-5.4.1.jar;C:\hz\education\NTU\Projects\IR\lucene_lib\lucene-analyzers-common-5.4.1.jar;C:\hz\education\NTU\Projects\IR\lucene_lib\lucene-demo-5.4.1.jar;C:\hz\education\NTU\Projects\IR\lucene_lib\lucene-queryparser-5.4.1.jar
 * CD C:\hz\education\NTU\Projects\IR\work\TextIndexer\build\classes
 * java textindexer.DblpIndexBuilder
 */
package assignment2a.indexer;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
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
    private IndexWriter _writer;

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
        _writer = new IndexWriter(indexStoreDir, iwc);
    }

    private void addToIndex(String year, String titles) throws IOException {
		System.out.println("Add to index for year " + year);
        _writer.addDocument(getLuceneDocument(year, titles));
    }
	
	private Document getLuceneDocument(String year, String titles) {
        Document doc = new Document();
        
		// In this assignment, term vector needs to be stored but the title itself does not
		FieldType myFieldType = new FieldType(TextField.TYPE_NOT_STORED);
		myFieldType.setStoreTermVectors(true);
		
        doc.add(new Field("title", titles, myFieldType));
		
		doc.add(new StringField("pubyear", year, Field.Store.YES));
        
        return doc;
    }

    private void close() throws IOException {
        this._writer.close();
    }

}
