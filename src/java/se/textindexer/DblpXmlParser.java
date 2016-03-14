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

package se.textindexer;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import se.constant.Constants;

/**
 * Parse the dblp.xml file and output a text format file, which will 
 * be used by DBLP index builder
 * @author a
 */
public class DblpXmlParser {
    public static void main(String[] args) throws IOException {
        //testTextIndex();
        testDrive();
        System.out.print("here we go!!!");
    }
    
    private static void testDrive(){
        //String uri = "C:\\hz\\education\\NTU\\Projects\\IR\\dataset\\dblp.xml\\2.xml";
        //String uri = "C:\\hz\\education\\NTU\\Projects\\IR\\dataset\\dblp.xml\\dblp.xml";
         try {
            SAXParserFactory parserFactor = SAXParserFactory.newInstance();
            SAXParser parser = parserFactor.newSAXParser();
            parser.getXMLReader().setFeature("http://xml.org/sax/features/validation", true);
            HzXMLHandler handler = new HzXMLHandler();
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
