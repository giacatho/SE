/**
 * Author: Liu Hongzhi
 * Email: liuh0051@e.ntu.edu.sg
 * Date 6 Mar, 2016
 */
package se.textindexer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import se.constant.Constants;

/**
 *
 * @author a
 */
public class HzXMLHandler extends DefaultHandler {

    private Boolean insideDblpItem = false; //inside inproceedings or article
    private BufferedWriter bw;
    private DBLP_Item item;
	private String value;
    
	@Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		value = "";
        if (qName.equalsIgnoreCase("inproceedings ") || qName.equalsIgnoreCase("article")) {
            insideDblpItem = true;
            item = new DBLP_Item(qName);
            item.setKey(atts.getValue("key"));
            //System.out.println("key: " + atts.getValue("key"));
        }
    }

	@Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (!insideDblpItem) return;
		
        if (qName.equalsIgnoreCase("inproceedings ") || qName.equalsIgnoreCase("article")){
			try {
				insideDblpItem = false;
				bw.write(item.toString()); //saved as a log file
				//an option is to plugin the index thing here                    
			} catch (IOException ex) {
				Logger.getLogger(HzXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
			}
        }
            
		else if (qName.equalsIgnoreCase("author")) {
			item.addAuthor(value);
		}

		else if (qName.equalsIgnoreCase("title")) {
			item.setTitle(value);
		}
		
		else if (qName.equalsIgnoreCase("year")) {
			item.setPubyear(value);
		}
		
		else if (qName.equalsIgnoreCase("booktitle") || qName.equalsIgnoreCase("journal") ){
			item.setPubvenue(value);
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
            bw = new BufferedWriter(new FileWriter(Constants.DATA_FILE_TXT));
        } catch (IOException ex) {
            Logger.getLogger(HzXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	@Override
    public void endDocument() {
        try {
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(HzXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
