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

// Temp: bugs: this one incorrectly add more authors: e.g 	
//type:article
//key:journals/jpdc/LiSTSK13
//title:Parallel multitask cross validation for Support Vector Machine using GPU.
//pubvenue:J. Parallel Distrib. Comput.
//pubyear:2013
//author:Qi Li
//author:Ra
//author:ied Salman
//author:Erik Test
//author:Robert Strack
//author:Vojislav Kecman
	
//<title>NAT2TEST<sub>SCR</sub>: Test case generation from natural language requirements based on SCR specifications.</title>

	
    private Boolean insideDblpItem = false; //inside inproceedings or article
    private BufferedWriter bw;
    private String currentTag;
    private DBLP_Item item;
    
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        currentTag = qName;
        if (qName.equalsIgnoreCase("inproceedings ") || qName.equalsIgnoreCase("article")) {
            insideDblpItem = true;
            item = new DBLP_Item(qName);
            item.setKey(atts.getValue("key"));
            //System.out.println("key: " + atts.getValue("key"));
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("inproceedings ") || qName.equalsIgnoreCase("article")){
            if(insideDblpItem == true){
                try {
                    //System.out.println(item);
                    bw.write(item.toString()); //saved as a log file
                    //an option is to plugin the index thing here                    
                    insideDblpItem = false;
                } catch (IOException ex) {
                    Logger.getLogger(HzXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }         
    }
    
    public void characters(char[] ch, int start, int length)
                throws SAXException {
        String value = new String(ch, start, length);
        if(insideDblpItem == true){
            if(currentTag.equalsIgnoreCase("author") ){
                if(value != null){
                    item.addAuthor(value);
                }
            }else if(currentTag.equalsIgnoreCase("title")){
                item.setTitle(value);
            }else if(currentTag.equalsIgnoreCase("year")){
                item.setPubyear(value);
            }else if(currentTag.equalsIgnoreCase("booktitle") || currentTag.equalsIgnoreCase("journal") ){
                item.setPubvenue(value);
            }
        }
    }
    
    public void startDocument(){
        try {
            bw = new BufferedWriter(new FileWriter(Constants.DATA_FILE_TXT));
        } catch (IOException ex) {
            Logger.getLogger(HzXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void endDocument() {
        try {
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(HzXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
