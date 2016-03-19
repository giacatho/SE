package assignment2a.indexer;

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
public class A2aXMLHandler extends DefaultHandler {
	
	Map<String, List<String>> yearToTitleList = new HashMap();
			
    private Boolean insideDblpItem = false; //inside inproceedings or article
    private A2aDBLPItem item;
	private String value;
    
	@Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		value = "";
        if (qName.equalsIgnoreCase("inproceedings") || qName.equalsIgnoreCase("article")) {
            insideDblpItem = true;
            item = new A2aDBLPItem(qName);
            //System.out.println("key: " + atts.getValue("key"));
        }
    }

	@Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (!insideDblpItem) return;
		
        if (qName.equalsIgnoreCase("inproceedings") || qName.equalsIgnoreCase("article")) {
			insideDblpItem = false;

			String year = item.getPubyear();
			if (year == null || year.isEmpty())
				return;
			
			if (yearToTitleList.containsKey(year)) {
				yearToTitleList.get(year).add(item.getTitle());
			} else {
				// System.out.println("Year " + item.getPubyear() + ", venue " + item.getPubvenue());
				List<String> titleList = new ArrayList();
				titleList.add(item.getTitle());
				yearToTitleList.put(year, titleList);
			}
        }
            
		else if (qName.equalsIgnoreCase("title")) {
			item.setTitle(value);
		}
		
		else if (qName.equalsIgnoreCase("year")) {
			item.setPubyear(value.trim());
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
    }
	
	@Override
    public void endDocument() {
        try {
			System.out.println("Total " + yearToTitleList.size() + " YearAndVenue");
			
			System.out.println("Year list: " + yearToTitleList.keySet());
			
			A2aDblpIndexBuilder indexBuilder = new A2aDblpIndexBuilder(this.yearToTitleList);
			indexBuilder.buildIndex();
        } catch (IOException ex) {
            Logger.getLogger(A2aXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
	
}
