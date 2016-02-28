package ci6226.indexer;

import ci6226.constant.Constants;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author giacatho
 */
public class DBLPIndexer {
    
    class Article {
        private String key;
        private String title;
        private List<String> authors;
        
        Article() {
            authors = new ArrayList();
        }
        
        public void setKey(String key) {
            this.key = key;
        }
        
        public void setTitle(String title) {
            this.title = title;
        }
        
        public void addAuthor(String author) {
            this.authors.add(author);
        }
        
        public String getKey() {
            return this.key;
        }
        
        public String getTitle() {
            return this.title;
        }
        
        public List<String> getAuthors() {
            return this.authors;
        }
        
        @Override
        public String toString() {
            return "Key: " + this.key + ". Title: " + this.title + " Authors: " + this.authors.toString();
        }
    }
    
    class MyXMLHandler extends DefaultHandler {
        private IndexWriter writer;
        
        private String value;
        
        private Article article;
        
        public void initIndexWriter() {
            try {
                Directory dir = FSDirectory.open(new File(Constants.INDEX_DIR));
                writer = new IndexWriter(dir, //3
                        new StandardAnalyzer( //3
                                Version.LUCENE_30),//3
                        true, //3
                        IndexWriter.MaxFieldLength.UNLIMITED); //3
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void startElement(String namespaceURI, String localName,
            String qName, Attributes atts) throws SAXException {
            
            value = "";
            if (qName.equalsIgnoreCase("article")) {
                article = new Article();
                article.setKey(atts.getValue("key"));
            }
            else if (qName.equalsIgnoreCase("author")) {
            }
            else if (qName.equalsIgnoreCase("title")) {
            }
            
        }
        
        @Override
        public void endElement(String namespaceURI, String localName,
            String qName) throws SAXException {
            if (qName.equalsIgnoreCase("article")) {
                try {
                    if (writer == null) {
                        this.initIndexWriter();
                    }
                    
                    writer.addDocument(getLuceneDocument(article));
                } catch (IOException ex) {
                    Logger.getLogger(DBLPIndexer.class.getName()).log(Level.SEVERE, null, ex);
                    
                    System.exit(1);
                }
            }
            
            else if (qName.equalsIgnoreCase("author")) {
                article.addAuthor(value);
            }
            
            else if (qName.equalsIgnoreCase("title")) {
                article.setTitle(value);
            }
            
        }
        
        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            value += new String(ch, start, length);
        }

        @Override
        public void endDocument() {
            try {
                this.writer.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
        }
        private void Message(String mode, SAXParseException exception) {
            System.out.println(mode + " Line: " + exception.getLineNumber()
                    + " URI: " + exception.getSystemId() + "\n" + " Message: "
                    + exception.getMessage());
        }

        public void warning(SAXParseException exception) throws SAXException {

            Message("**Parsing Warning**\n", exception);
            throw new SAXException("Warning encountered");
        }

        public void error(SAXParseException exception) throws SAXException {

            Message("**Parsing Error**\n", exception);
            throw new SAXException("Error encountered");
        }

        public void fatalError(SAXParseException exception) throws SAXException {

            Message("**Parsing Fatal Error**\n", exception);
            throw new SAXException("Fatal Error encountered");
        }
        
        protected Document getLuceneDocument(Article article) {
            try {
                Document doc = new Document();
                doc.add(new Field("key", article.getKey(), 
                        Field.Store.YES, Field.Index.NOT_ANALYZED));      
                doc.add(new Field("title", article.getTitle(),
                        Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field("authors", article.getAuthors().toString(),
                        Field.Store.YES, Field.Index.ANALYZED));
                return doc;
            } catch (Exception e) {
                throw new RuntimeException(e.toString());
            }
        }
    }
    
    DBLPIndexer(String uri) {
        try {
            SAXParserFactory parserFactor = SAXParserFactory.newInstance();
            SAXParser parser = parserFactor.newSAXParser();
            parser.getXMLReader().setFeature("http://xml.org/sax/features/validation", true);
            MyXMLHandler handler = new MyXMLHandler();
            parser.parse(new File(uri), handler);
        } catch (IOException e) {
            System.out.println("Error reading URI: " + e.getMessage());
        } catch (SAXException e) {
            System.out.println("Error in parsing: " + e.getMessage());
        } catch (ParserConfigurationException e) {
            System.out.println("Error in XML parser configuration: "
                    + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        new DBLPIndexer(Constants.DATA_FILE);
    }
}
