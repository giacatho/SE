/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sky.indexer;

import common.Constants;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import sky.util.Utils;

/**
 *
 * @author cnaquoc
 */
public final class IndexBuilder {
    public static void main(String[] args) throws IOException {
        Indexer indexer = new FacetIndexer(Constants.INDEX_FOLDER, Constants.TAXO_FOLDER, Utils.getDefaultAnalyzer());
        
        System.out.println("Here we go!");
        try {
                SAXParserFactory parserFactor = SAXParserFactory.newInstance();
                SAXParser parser = parserFactor.newSAXParser();
                parser.getXMLReader().setFeature("http://xml.org/sax/features/validation", true);
                FacetXMLHandler handler = new FacetXMLHandler(indexer);
                parser.parse(new File(Constants.DATA_FILE_XML), handler);      
                
        } catch (IOException e) {
                System.out.println("Error reading URI: " + e.getMessage());
        } catch (SAXException e) {
                System.out.println("Error in parsing: " + e.getMessage());
        } catch (ParserConfigurationException e) {
                System.out.println("Error in XML parser configuration: "
                        + e.getMessage());
        }
        System.out.println("Done!");
    }
}
