/* 
    Document   : QueryResult
    Created on : 03-Mar-2016, 17:52:49
    Author     : Quoc
*/

package se.searcher.gui;

import java.util.List;
import org.apache.lucene.document.Document;

public class SearchOutput {
    private String html;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
    
    public SearchOutput(Document doc) {
        html = doc.get("title");        
        
        for (String key :keyWords) {
            html = html.replaceAll("(?i)(" + key + ")", "<b>$1</b>");
        }
    }
    
    static List<String> keyWords;
    public static void setKeywords(List<String> keys) {
        keyWords = keys;
    }
    
//    public static SearchOutput parse(Document doc) {
//        SearchOutput output = new SearchOutput();
//        
//        output.setHtml(doc.get("title"));
//                
//        return output;
//    }
}
