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
package assignment2b.indexer;


import assignment2b.indexer.model.YearAndVenue;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
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
public class A2bDblpIndexBuilder {

	Map<YearAndVenue, List<String>> yearAndVenueToTitleList;
    private IndexWriter _writer;

	public A2bDblpIndexBuilder (Map<YearAndVenue, List<String>> yearAndVenueToTitleList) {
		this.yearAndVenueToTitleList = yearAndVenueToTitleList;
	}
	
	public void buildIndex() throws IOException {
		initIndexWriter(Constants.INDEX_ASSIGNMENT2B_DIR_TMP, new SimpleAnalyzer());
		for (Map.Entry<YearAndVenue, List<String>> entrySet : yearAndVenueToTitleList.entrySet()) {
			YearAndVenue yearAndVenue = entrySet.getKey();
			List<String> titleList = entrySet.getValue();
			
			// TODO need more beautiful title, not use to string
			this.addToIndex(yearAndVenue.getYear(), yearAndVenue.getVenue(), titleList.toString()); 
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

    private void addToIndex(String year, String venue, String titles) throws IOException {
        _writer.addDocument(getLuceneDocument(year, venue, titles));
    }
	
	private Document getLuceneDocument(String year, String venue, String titles){
        Document doc = new Document();
        
		// Tin: Title and author needs term vector to support phrase query
		FieldType myFieldType = new FieldType(TextField.TYPE_STORED);
		myFieldType.setStoreTermVectors(true);
		
        doc.add(new Field("title", titles, myFieldType));
		
		doc.add(new StringField("pubyear", year, Field.Store.YES));
		// TODO try to change to StringField for venue but fail to search for the document with year/venue, why?
        //doc.add(new TextField("pubvenue", venue, Field.Store.YES));
		doc.add(new StringField("pubvenue", venue, Field.Store.YES));
        
        return doc;
    }

    private void close() throws IOException {
        this._writer.close();
    }

}
