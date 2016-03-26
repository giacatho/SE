/*
Modify HongZhi handler
 */
package sky.indexer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author cnaquoc
 */
public class FacetXMLHandler extends DefaultHandler{
    
    private Boolean insideDblpItem = false; //inside inproceedings or article
    private DBLPItem item;
    private String value;
    private Indexer indexer;
    int count = 0;
    
    public FacetXMLHandler(Indexer indexer) {
        this.indexer = indexer;
    }
            
	@Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        value = "";
        if (qName.equalsIgnoreCase("inproceedings") || qName.equalsIgnoreCase("article")) {
            insideDblpItem = true;
            item = new DBLPItem(qName);
            item.setKey(atts.getValue("key"));
            //System.out.println("key: " + atts.getValue("key"));
        }
    }

	@Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (!insideDblpItem) return;
        count++;
		
        if (qName.equalsIgnoreCase("inproceedings") || qName.equalsIgnoreCase("article")){
            try {
                insideDblpItem = false;
                //create document
                if (item != null) indexer.index(item);                
            } catch (IOException ex) {
                Logger.getLogger(FacetXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("error");
            }
        }
            
        else if (qName.equalsIgnoreCase("author")) {
                item.addAuthor(value);
        }

        else if (qName.equalsIgnoreCase("title")) {
                item.setTitle(value);
        }

        else if (qName.equalsIgnoreCase("year")) {
                item.setPubyear(value.trim());
        }

        else if (qName.equalsIgnoreCase("booktitle")){// || qName.equalsIgnoreCase("journal") ){
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
            this.indexer.Finish();
            System.out.println("Total: " + count);
        } catch (IOException ex) {
            Logger.getLogger(FacetXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
