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

import assignment1.indexer.model.A1DBLPItem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;
import common.Constants;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

/**
 * Input: text file containing DBLP items Output: a Lucene index and index
 * statistics
 *
 * @author a
 */
public class A1IndexBuilder {

    private BufferedReader bufReader;
    private InputStream stream;
    //private Path pathIndexStore;
    //private Directory indexStoreDir;
    //private  Analyzer analyzer;
    private IndexWriter writer;

    public A1IndexBuilder() {

    }

    /*
    public A1IndexBuilder(String dblpTextFileName) throws IOException {
        setInputFile(dblpTextFileName);
    }
     */
    private void setInputFile(String dblpTextFileName) throws IOException {
        Path file = Paths.get(dblpTextFileName);
        stream = Files.newInputStream(file);
        bufReader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }

    public IndexWriter getWriter() {
        return writer;
    }

    //TODO: parameterize this function , so that it can be used to generate different kind of index
    public void init_indexWriter(String storePath, Analyzer analyzer) throws IOException {
        //"C:\\hz\\education\\NTU\\Projects\\IR\\index_stores\\store1"
        Path pathIndexStore = Paths.get(storePath);
        Directory indexStoreDir = FSDirectory.open(pathIndexStore);
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        writer = new IndexWriter(indexStoreDir, iwc);
    }

    private String getValue(String line) {
        int pos = line.indexOf(":");
        return line.substring(pos + 1).trim();
    }

    /**
     * @input: this.bufReader
     * @return A1DBLPItem
     */
    public A1DBLPItem getNextItem() throws IOException {
        if (bufReader == null) {
            return null;
        }
        A1DBLPItem item = null; //new A1DBLPItem();
        String line;
        while ((line = bufReader.readLine()) != null) {
            line = line.trim();
            //parse line and create DBLP_item
            if (line.startsWith("type:")) {
                item = new A1DBLPItem(getValue(line));
            }

            if (item != null) {
                if (line.startsWith("key:")) {
                    item.setKey(getValue(line));
                } else if (line.startsWith("title:")) {
                    item.setTitle(getValue(line));
                } else if (line.startsWith("pubvenue:")) {
                    item.setPubvenue(getValue(line));
                } else if (line.startsWith("pubyear:")) {
                    item.setPubyear(getValue(line));
                } else if (line.startsWith("author:")) {
                    item.addAuthor(getValue(line));
                }
            }
			
            //blank line indicate end of an DBLP item
            if (line.length() == 0) {
                break;
            }
        }
        return item;
    }

    public void addItemToIndex(A1DBLPItem item) throws IOException {
		Document doc = new Document();
        
		doc.add(new TextField("title", item.getTitle(), Field.Store.YES));
		doc.add(new TextField("all", item.getTitle(), Field.Store.NO));
		
		for(String author : item.getAuthors()){
			doc.add(new TextField("author", author, Field.Store.YES));
			doc.add(new TextField("all", author, Field.Store.NO));
        } 
		
        doc.add(new StringField("key", item.getKey(), Field.Store.YES));
		doc.add(new TextField("all", item.getKey(), Field.Store.NO));
		
		doc.add(new StringField("pubyear", item.getPubyear(), Field.Store.YES));
		doc.add(new TextField("all", item.getPubyear(), Field.Store.NO));
		
        doc.add(new TextField("pubvenue", item.getPubvenue(), Field.Store.YES));
		doc.add(new TextField("all", item.getPubvenue(), Field.Store.NO));
        
		writer.addDocument(doc);
    }

    /**
     * @deprecated
     */
    public void printIndexStat() {
        System.out.println("maxDoc: " + writer.maxDoc());
        System.out.println("Analyzer: " + writer.getAnalyzer());
    }

    public void close() throws IOException {
        this.stream.close();
        this.bufReader.close();
        this.writer.close();
    }

    public int getSizeOfTerms(IndexReader reader, String fieldName) throws IOException {
        List<LeafReaderContext> list = reader.leaves();

        int rawTermSize = 0;
        int sum = 0;
        HashSet<String> hashset = new HashSet<String>();

        for (LeafReaderContext leaf : list) {
            LeafReader lr = leaf.reader();
            TermsEnum itor = lr.terms(fieldName).iterator();
            BytesRef ref;
            while ((ref = itor.next()) != null) {
                Term t = new Term("title", ref);
                hashset.add(t.text());
                //System.out.println(t.text());
            }
            rawTermSize += lr.terms(fieldName).size();
        }
        //System.out.println("Raw term size: " + rawTermSize);
        //System.out.println("Unique term size: " + hashset.size());
        return hashset.size();

    }

    public static void main(String[] args) throws IOException {
        System.out.println("DblpIndexBuilder: here we go!");
        eval_different_indexing();
        //testDrive();
    }

    public static void testDrive() throws IOException {
        test_getSizeOfTerms();
    }

    private static void test_getSizeOfTerms() throws IOException {
        String uri = "C:\\hz\\education\\NTU\\Projects\\IR\\index_stores\\store2";

        Directory indexStoreDir = FSDirectory.open(Paths.get(uri));
        IndexReader reader = DirectoryReader.open(indexStoreDir);

        A1IndexBuilder ibuilder = new A1IndexBuilder();
        int termSize = ibuilder.getSizeOfTerms(reader, "title");
        System.out.println("Term Size (title): " + termSize);
    }

    //TODO: parameterize this function , so that it can be used to generate different kind of index
    public static void eval_different_indexing() throws IOException {
		String indexStoreFolderName;
        Analyzer analyzer;

        ///////////Simple Analyzer////////////////
//        analyzer = new SimpleAnalyzer();
//		  indexStoreFolderName = Constants.INDEX_FOLDER + "store_SimpleAnalyzer";
//        buildAndEvalIndex(Constants.DATA_FILE_TXT, indexStoreFolderName, analyzer);
//
//        //////////////WhitespaceAnalyzer////////////////        
//        analyzer = new WhitespaceAnalyzer();
//		  indexStoreFolderName = Constants.INDEX_FOLDER + "store_WhitespaceAnalyzer";
//        buildAndEvalIndex(Constants.DATA_FILE_TXT, indexStoreFolderName, analyzer);
//
        //////////////StandardAnalyzer////////////////        
        analyzer = new StandardAnalyzer();
		indexStoreFolderName = Constants.INDEX_STANDARD_ANALYZER_DIR;
        buildAndEvalIndex(Constants.DATA_FILE_TXT, indexStoreFolderName, analyzer);
//        
//        //////////////StopAnalyzer////////////////        
//        analyzer = new StopAnalyzer();
//		  indexStoreFolderName = Constants.INDEX_FOLDER + "store_StopAnalyzer";
//        buildAndEvalIndex(dblpTextFileName, indexStoreFolderName, analyzer);
//
//        ////////////CustomAnalyzer1///////////////////////
//        analyzer = new MyAnalyzer_PorterStem();
//        indexStoreFolderName = Constants.INDEX_FOLDER + "store_CustomAnalyzer1";
//        buildAndEvalIndex(dblpTextFileName, indexStoreFolderName, analyzer);
//
//        ////////////CustomAnalyzer2///////////////////////
//        analyzer = new MyAnalyzer_LowerCase();
//        indexStoreFolderName = Constants.INDEX_FOLDER + "store_CustomAnalyzer2";
//        buildAndEvalIndex(dblpTextFileName, indexStoreFolderName, analyzer);
//
//        ////////////CustomAnalyzer3///////////////////////
//        analyzer = new MyAnalyzer_LetterTokenizer();
//        indexStoreFolderName = Constants.INDEX_FOLDER + "store_CustomAnalyzer3";
//        buildAndEvalIndex(dblpTextFileName, indexStoreFolderName, analyzer);
//        ////////////////////////////
    }

    private static void buildAndEvalIndex(String dblpTextFileName, String indexStoreFolderName, Analyzer analyzer) throws IOException {
        Date begin = new Date();
        A1IndexBuilder ibuilder = new A1IndexBuilder();
        ibuilder.setInputFile(dblpTextFileName);
        ibuilder.init_indexWriter(indexStoreFolderName, analyzer);

        A1DBLPItem item;
        int count = 0;
        while ((item = ibuilder.getNextItem()) != null) {
            ibuilder.addItemToIndex(item);
            //progress indicators
            if (count % 10000 == 0) {
                System.out.print(".");
            }
            count++;
        }
        System.out.println();
        System.out.println("indexed doc #: " + count);
        ibuilder.printIndexStat();
        IndexReader reader = DirectoryReader.open(ibuilder.getWriter(), true);
        int termSize = ibuilder.getSizeOfTerms(reader, "title");
        ibuilder.close();
        //////////////EVALUATION RESULTS//////////////////
        Date end = new Date();
        int elapsedSeconds = (int) (end.getTime() - begin.getTime()) / 1000; //in seconds
        System.out.println("Time Used: " + elapsedSeconds + " seconds");
        System.out.println("Term Size (title): " + termSize);
    }

}
