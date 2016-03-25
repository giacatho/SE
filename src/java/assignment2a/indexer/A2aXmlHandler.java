package assignment2a.indexer;

import assignment2a.indexer.model.A2aDBLPItem;
import java.io.IOException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class A2aXmlHandler extends DefaultHandler {
	A2aIndexBuilder a2IndexBuilder;
	
	private int noInproceedings = 0;
	private int noArticles = 0;
    private Boolean insideDblpItem = false; //inside inproceedings or article
    private A2aDBLPItem item;
	private String value;
    
	@Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		value = "";
        if (qName.equalsIgnoreCase("inproceedings") || qName.equalsIgnoreCase("article")) {
            insideDblpItem = true;
            item = new A2aDBLPItem();
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
				this.a2IndexBuilder.addToIndex(item.getPubyear(), item.getPubvenue(), item.getTitle(), item.getAuthors());
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
    public void startDocument() {
		System.out.println("Start to parse and index dblp.xml");
		try {
			this.a2IndexBuilder = new A2aIndexBuilder();
		} catch (IOException ex) {
			throw new RuntimeException(ex.toString());
		}
    }
	
	@Override
    public void endDocument() {
		System.out.println("Parsing and indexing has been completed.");
		System.out.println(String.format("Total %d inproceedings and %d articles.", 
				noInproceedings, noArticles));

		try {
			this.a2IndexBuilder.close();
		} catch (IOException ex) {
			throw new RuntimeException(ex.toString());
		}
    }
	
	
}
