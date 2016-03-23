package assignment2b.indexer;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import common.Constants;

/**
 * Parse the dblp.xml file and output a text format file, which will 
 * be used by DBLP index builder
 * @author a
 */
public class A2bXmlParser {
    public static void main(String[] args) throws IOException {
        parseDblpXML();
        System.out.print("here we go!!!");
    }
    
    private static void parseDblpXML(){
		try {
			SAXParserFactory parserFactor = SAXParserFactory.newInstance();
			SAXParser parser = parserFactor.newSAXParser();
			parser.getXMLReader().setFeature("http://xml.org/sax/features/validation", true);
			A2bXmlHandler handler = new A2bXmlHandler();
			parser.parse(new File(Constants.DATA_FILE_XML), handler);

		} catch (IOException e) {
			System.out.println("Error reading URI: " + e.getMessage());
		} catch (SAXException e) {
			System.out.println("Error in parsing: " + e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println("Error in XML parser configuration: "
				+ e.getMessage());
		}
    }
    

}
