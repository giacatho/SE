/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

set path=%path%;C:\Program Files\Java\jdk1.8.0_73\bin
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_73
set CLASSPATH=.;%JAVA_HOME%\lib;C:\hz\education\NTU\Projects\IR\lucene_lib\lucene-core-5.4.1.jar;C:\hz\education\NTU\Projects\IR\lucene_lib\lucene-analyzers-common-5.4.1.jar;C:\hz\education\NTU\Projects\IR\lucene_lib\lucene-demo-5.4.1.jar;C:\hz\education\NTU\Projects\IR\lucene_lib\lucene-queryparser-5.4.1.jar

 */
package se.textindexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * This tool parse and index text files in a folder
 * @author LHZ
 */
public class TextIndexer {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        testTextIndex();
    }

    private static void testTextIndex() throws IOException {
        String INDEX_STORE_DIR = "C:\\hz\\education\\NTU\\Projects\\IR\\NetBean\\TextIndexer\\index_dir";
        String INDEXED_DIR = "C:\\hz\\education\\NTU\\Projects\\IR\\corpus\\RFCs0501_1000";
        indexDir(Paths.get(INDEXED_DIR), Paths.get(INDEX_STORE_DIR));
      
        System.out.print("Test Text Index: Successful!");
    }
    
    //find all .txt files in the folder and index them one by one       
    private static void indexDir(Path pathIndexed,Path pathIndexStore) throws IOException{
        Directory indexStoreDir = FSDirectory.open(pathIndexStore);
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(OpenMode.CREATE);
        //iterate the files in the directory
        //while
        IndexWriter writer = new IndexWriter(indexStoreDir, iwc);
        
        if (Files.isDirectory(pathIndexed)) {
            File file = new File(pathIndexed.toString());
            for(File f : file.listFiles()){
                //System.out.println(f.getAbsolutePath());
                Path path = Paths.get(f.getAbsolutePath());
                addDocumentToIndex(writer, path);
            }
                
            //Path file = Paths.get("C:\\hz\\education\\NTU\\Projects\\IR\\corpus\\RFCs0501_1000\\rfc821.txt");
            //addDocumentToIndex(writer, file);
        }
        
        System.out.println("# of doc indexed: " + writer.numDocs());
        writer.close();
  
    }
    
    //add a single doc to Index
    private static void addDocumentToIndex(IndexWriter writer,Path file) throws IOException{
        Document doc = new Document();
        InputStream stream = Files.newInputStream(file);
        Field pathField = new StringField("path", file.toString(), Field.Store.YES);
        Field contentField = new TextField("content", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)));
        doc.add(pathField);
        doc.add(contentField);
        if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
            writer.addDocument(doc);
        } 
        stream.close();
    }
    

    
}
