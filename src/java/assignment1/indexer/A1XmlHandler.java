/**
 * Author: Liu Hongzhi
 * Email: liuh0051@e.ntu.edu.sg
 * Date 6 Mar, 2016
 */
package assignment1.indexer;

import assignment1.indexer.model.A1DBLPItem;
import common.Constants;
import common.Utils;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author a
 */
public class A1XmlHandler extends DefaultHandler {
	A1IndexBuilder a1IndexBuider;
	
	private long startTS;
	private long endTS;
	
	private int noInproceedings = 0;
	private int noArticles = 0;

	private Boolean insideDblpItem = false; //inside inproceedings or article
    private A1DBLPItem item;
	private String value;
    
	@Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		value = "";
        if (qName.equalsIgnoreCase("inproceedings") || qName.equalsIgnoreCase("article")) {
            insideDblpItem = true;
            item = new A1DBLPItem();
            item.setKey(atts.getValue("key"));
        }
    }

	@Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (!insideDblpItem) return;
		
		if (qName.equalsIgnoreCase("inproceedings") || qName.equalsIgnoreCase("article")) {
			insideDblpItem = false;
			if (item.getPubyear() == null || item.getPubvenue() == null || item.getTitle() == null) {
				// Just ignore it
				System.out.print("-");
				return;
			}

			if (qName.equalsIgnoreCase("inproceedings"))
				noInproceedings++;
			else if (qName.equalsIgnoreCase("article"))
				noArticles++;
			else
				throw new RuntimeException("Impossible state");

			if ((noInproceedings + noArticles) % 10000 == 0) {
				System.out.print(".");
			}

			try {
				this.a1IndexBuider.addToIndex(item.getKey(), item.getPubyear(), item.getPubvenue(), 
						item.getTitle(), item.getAuthors());
			} catch (IOException ex) {
				throw new RuntimeException(ex.toString());
			}
		}
		
		else if (qName.equalsIgnoreCase("year")) {
			item.setPubyear(value.trim());
		}
		
		else if (qName.equalsIgnoreCase("booktitle") || qName.equalsIgnoreCase("journal") ){
			item.setPubvenue(value.trim());
		}
            
		else if (qName.equalsIgnoreCase("title")) {
			item.setTitle(value.trim());
		}
		
		else if (qName.equalsIgnoreCase("author")) {
			item.addAuthor(value.trim());
		}
    }
    
	@Override
    public void characters(char[] ch, int start, int length)
                throws SAXException {
		if (!insideDblpItem) return;
		
        value += new String(ch, start, length);
    }
    
	@Override
    public void startDocument(){
        System.out.println("Start to parse and index dblp.xml");
		this.startTS = System.currentTimeMillis();
		
		System.out.println("Start: " + this.startTS);
		
		try {
			this.a1IndexBuider = new A1IndexBuilder();
		} catch (IOException ex) {
			throw new RuntimeException(ex.toString());
		}
    }
	@Override
    public void endDocument() {
		this.endTS = System.currentTimeMillis();
		
        System.out.println("Parsing and indexing has been completed.");
		System.out.println(String.format("Total %d inproceedings and %d articles.", 
				noInproceedings, noArticles));
		
		System.out.println("Start time: " + this.endTS);
		System.out.println("End time: " + this.endTS);
		System.out.println("It takes " + (this.endTS - this.startTS)/1000 + " seconds.");
				
		try {
			this.a1IndexBuider.close();
			
			IndexReader reader = DirectoryReader.open(FSDirectory.open(
				Paths.get(Constants.INDEX_ASSIGNMENT1_DIR)));
			System.out.println("Total terms in title: " + Utils.getSizeOfTerms(reader, "title"));
			reader.close();
			
		} catch (IOException ex) {
			throw new RuntimeException(ex.toString());
		}
    }
}
