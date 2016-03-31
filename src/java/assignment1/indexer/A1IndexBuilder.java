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
package assignment1.indexer;

import java.io.IOException;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import common.Constants;
import common.Utils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

/**
 * Input: text file containing DBLP items Output: a Lucene index and index
 * statistics
 *
 * @author a
 */
public class A1IndexBuilder {
    private final IndexWriter writer;

    public A1IndexBuilder() throws IOException {
		Analyzer analyzer;
		analyzer = new StandardAnalyzer();
//		analyzer = new SimpleAnalyzer();
//		analyzer = new WhitespaceAnalyzer();
//		analyzer = new StopAnalyzer();
//		analyzer = new PorterStemAnalyzer();
//		analyzer = new LowerCaseAnalyzer();
//		analyzer = new LetterAnalyzer();
		
		this.writer = Utils.initIndexWriter(Constants.INDEX_ASSIGNMENT1_DIR, analyzer);
    }
	
	public void addToIndex(String key, String year, String venue, String title, List<String> authors) throws IOException {
		Document doc = new Document();
        
		doc.add(new TextField("key", key, Field.Store.YES));
		doc.add(new TextField("all", key, Field.Store.NO));
		
		doc.add(new StringField("pubyear", year, Field.Store.YES));
		doc.add(new TextField("all", year, Field.Store.NO));
		
        doc.add(new TextField("pubvenue", venue, Field.Store.YES));
		doc.add(new TextField("all", venue, Field.Store.NO));
		
		doc.add(new TextField("title", title, Field.Store.YES));
		doc.add(new TextField("all", title, Field.Store.NO));
		
		for(String author : authors){
			doc.add(new TextField("author", author, Field.Store.YES));
			doc.add(new TextField("all", author, Field.Store.NO));
        } 
		
		writer.addDocument(doc);
	}
    
	public void printIndexStat() {
        System.out.println("maxDoc: " + writer.maxDoc());
        System.out.println("Analyzer: " + writer.getAnalyzer());
    }

    public void close() throws IOException {
        this.writer.close();
    }
}
