package assignment2b.indexer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import se.constant.Constants;

/**
 *
 * @author 
 */
public class A2bXMLHandler extends DefaultHandler {
	class YearAndVenue {
		String year;
		String venue;
		
		public YearAndVenue(String year, String venue) {
			this.year = year;
			this.venue = venue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			
			YearAndVenue other = (YearAndVenue) obj;
			return year.equalsIgnoreCase(other.year) && venue.equalsIgnoreCase(other.venue);
		}

		@Override
		public int hashCode() {
			int hash = 7;
			hash = 89 * hash + (this.year != null ? this.year.hashCode() : 0);
			hash = 89 * hash + (this.venue != null ? this.venue.hashCode() : 0);
			return hash;
		}
	}
	
	Map<YearAndVenue, Integer> yearAndVenueToId = new HashMap();
	int sequenNo = 1;
			
    private Boolean insideDblpItem = false; //inside inproceedings or article
    private BufferedWriter bw;
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
		
        if (qName.equalsIgnoreCase("inproceedings")){
			try {
				insideDblpItem = false;
				
				YearAndVenue yearAndvenue = new YearAndVenue(item.getPubyear(), item.getPubvenue());
				if (yearAndVenueToId.containsKey(yearAndvenue)) {
					item.setYearAndVenueId(yearAndVenueToId.get(yearAndvenue));
				} else {
					// System.out.println("Year " + item.getPubyear() + ", venue " + item.getPubvenue());
					item.setYearAndVenueId(sequenNo);
					yearAndVenueToId.put(yearAndvenue, sequenNo);
					sequenNo++;
				}
				
				bw.write(item.toString()); //saved as a log file
				//an option is to plugin the index thing here                    
			} catch (IOException ex) {
				Logger.getLogger(A2bXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
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
        try {
            bw = new BufferedWriter(new FileWriter(Constants.DATA_FILE_TXT_A2B));
        } catch (IOException ex) {
            Logger.getLogger(A2bXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	@Override
    public void endDocument() {
        try {
			System.out.println("Total " + (sequenNo - 1) + " YearAndVenue");
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(A2bXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
