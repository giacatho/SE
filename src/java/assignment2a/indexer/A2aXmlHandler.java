package assignment2a.indexer;

import assignment2a.indexer.model.A2aDBLPItem;
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

public class A2aXmlHandler extends DefaultHandler {
	
	Map<String, List<String>> yearToTitleList = new HashMap();
			
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

			if (qName.equalsIgnoreCase("inproceedings"))
				noInproceedings++;
			else if (qName.equalsIgnoreCase("article"))
				noArticles++;
			else
				throw new RuntimeException("Impossible state");
			
			String year = item.getPubyear();
			if (year == null || year.isEmpty())
				return;
			
			if (yearToTitleList.containsKey(year)) {
				yearToTitleList.get(year).add(item.getTitle());
			} else {
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
		System.out.println("Start to parse the dblp.xml");
    }
	
	@Override
    public void endDocument() {
        try {
			System.out.println("Parsing has been completed.");
			System.out.println(String.format("Total %d inproceedings and %d articles.", 
					noInproceedings, noArticles));
			System.out.println("Total " + yearToTitleList.size() + " year");
			System.out.println("They are: " + yearToTitleList.keySet());
			
			System.out.println("Start to indexing");
			A2aIndexBuilder indexBuilder = new A2aIndexBuilder(this.yearToTitleList);
			indexBuilder.buildIndex();
			System.out.println("Finish indexing");
        } catch (IOException ex) {
            Logger.getLogger(A2aXmlHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
	
}
