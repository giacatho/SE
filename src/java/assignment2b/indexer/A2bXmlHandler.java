package assignment2b.indexer;

import assignment2b.indexer.model.A2bDBLPItem;
import assignment2b.indexer.model.YearVenue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author 
 */
public class A2bXmlHandler extends DefaultHandler {
	
	Map<YearVenue, List<String>> yearVenueToTitleList = new HashMap();
			
	private int noInproceedings = 0;
	private int noArticles = 0;
    private Boolean insideDblpItem = false; //inside inproceedings or article
    private A2bDBLPItem item;
	private String value;
    
	@Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		value = "";
        if (qName.equalsIgnoreCase("inproceedings") || qName.equalsIgnoreCase("article")) {
            insideDblpItem = true;
            item = new A2bDBLPItem();
        }
    }

	@Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (!insideDblpItem) return;
		
        if (qName.equalsIgnoreCase("inproceedings") || qName.equalsIgnoreCase("article")) {
			insideDblpItem = false;

			if (qName.equalsIgnoreCase("inproceedings"))
				noInproceedings++;
			else if (qName.equalsIgnoreCase("article"))
				noArticles++;
			else
				throw new RuntimeException("Impossible state");
			
			YearVenue yearVenue = new YearVenue(item.getPubyear(), item.getPubvenue());
			if (yearVenueToTitleList.containsKey(yearVenue)) {
				yearVenueToTitleList.get(yearVenue).add(item.getTitle());
			} else {
				List<String> titleList = new ArrayList();
				titleList.add(item.getTitle());
				yearVenueToTitleList.put(yearVenue, titleList);
			}
        }
            
		if (qName.equalsIgnoreCase("title")) {
			item.setTitle(value.trim());
		}
		
		else if (qName.equalsIgnoreCase("year")) {
			item.setPubyear(value.trim());
		}
		
		else if (qName.equalsIgnoreCase("booktitle")){
			item.setPubvenue(value.trim());
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
		System.out.println("Start to parse the dblp.xml");
    }
	
	@Override
    public void endDocument() {
        try {
			System.out.println("Parsing has been completed.");
			System.out.println(String.format("Total %d inproceedings and %d articles.", 
					noInproceedings, noArticles));
			
			System.out.println("Total " + yearVenueToTitleList.size() + " yearVene");
			
			System.out.println("Start to indexing");
			A2bIndexBuilder indexBuilder = new A2bIndexBuilder(this.yearVenueToTitleList);
			indexBuilder.buildIndex();
			System.out.println("Finish indexing");
        } catch (IOException ex) {
            Logger.getLogger(A2bXmlHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
	
}
