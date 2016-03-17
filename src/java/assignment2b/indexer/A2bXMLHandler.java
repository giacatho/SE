package assignment2b.indexer;

import assignment2b.indexer.model.YearAndVenue;
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
public class A2bXMLHandler extends DefaultHandler {
	
	Map<YearAndVenue, List<String>> yearAndVenueToTitleList = new HashMap();
			
    private Boolean insideDblpItem = false; //inside inproceedings or article
    private A2bDBLPItem item;
	private String value;
    
	@Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		value = "";
        if (qName.equalsIgnoreCase("inproceedings")) {
            insideDblpItem = true;
            item = new A2bDBLPItem(qName);
            //System.out.println("key: " + atts.getValue("key"));
        }
    }

	@Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (!insideDblpItem) return;
		
        if (qName.equalsIgnoreCase("inproceedings")) {
			insideDblpItem = false;

			YearAndVenue yearAndvenue = new YearAndVenue(item.getPubyear(), item.getPubvenue());
			if (yearAndVenueToTitleList.containsKey(yearAndvenue)) {
				yearAndVenueToTitleList.get(yearAndvenue).add(item.getTitle());
			} else {
				// System.out.println("Year " + item.getPubyear() + ", venue " + item.getPubvenue());
				List<String> titleList = new ArrayList();
				titleList.add(item.getTitle());
				yearAndVenueToTitleList.put(yearAndvenue, titleList);
			}
        }
            
		if (qName.equalsIgnoreCase("title")) {
			item.setTitle(value);
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
    }
	
	@Override
    public void endDocument() {
        try {
			System.out.println("Total " + yearAndVenueToTitleList.size() + " YearAndVenue");
			
			A2bDblpIndexBuilder indexBuilder = new A2bDblpIndexBuilder(this.yearAndVenueToTitleList);
			indexBuilder.buildIndex();
        } catch (IOException ex) {
            Logger.getLogger(A2bXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
	
}
