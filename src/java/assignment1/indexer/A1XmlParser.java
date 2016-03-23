/**
 * Author: Liu Hongzhi
 * Email: liuh0051@e.ntu.edu.sg 
 * Date 6 Mar, 2016
 * 
 * Purpose: Parse the DBLP XML file and output a simple format text file
 * 
 * Run in Command Line
 * CD C:\hz\education\NTU\Projects\IR\work\TextIndexer\build\classes
 * java -mx900M -DentityExpansionLimit=10000000 textindexer.DblpXmlParser
 */

package assignment1.indexer;

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
public class A1XmlParser {
    public static void main(String[] args) throws IOException {
        parseDblpXML();
        System.out.print("here we go!!!");
    }
    
    private static void parseDblpXML(){
		try {
			SAXParserFactory parserFactor = SAXParserFactory.newInstance();
			SAXParser parser = parserFactor.newSAXParser();
			parser.getXMLReader().setFeature("http://xml.org/sax/features/validation", true);
			A1XmlHandler handler = new A1XmlHandler();
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
