package assignment2a.indexer;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import common.Constants;

/**
 * This class will parse the dblp.xml file using A2aXmlHandler, which then
 calls A2aIndexBuilder to build the index.
 */
public class A2aXmlParser {
    public static void main(String[] args) throws IOException {
        parseDblpXML();
        System.out.print("here we go!!!");
    }
    
    private static void parseDblpXML(){
		try {
			SAXParserFactory parserFactor = SAXParserFactory.newInstance();
			SAXParser parser = parserFactor.newSAXParser();
			parser.getXMLReader().setFeature("http://xml.org/sax/features/validation", true);
			A2aXmlHandler handler = new A2aXmlHandler();
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
