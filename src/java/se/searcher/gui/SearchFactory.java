/* 
    Document   : SearchFactory
    Created on : 03-Mar-2016, 17:53:28
    Author     : Quoc
*/

package se.searcher.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.lucene.queryParser.ParseException;
import se.searcher.LuceneSearcher;


public class SearchFactory {
    private List<SearchInput> inputs;
    private Result result;
    private List<String> keyWords;

    public List<SearchInput> getQueries() {
        return inputs;
    }

    public Result getResult() {
        return result;
    }

    public SearchFactory(HttpServletRequest request, String parementerName) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create(); 
        
        SearchInput[] lstTemp = gson.fromJson(request.getParameter(parementerName), SearchInput[].class); 
        inputs = Arrays.asList(lstTemp);      
        
        keyWords = new ArrayList();
        for (SearchInput input : inputs) {
            String[] split = input.getKey().split(" ");
            for (String key : split) {
                keyWords.add(key);
            }
        }
    }
    
    public  boolean run(){
        try {
            LuceneSearcher machine = new LuceneSearcher();            
            
            SearchOutput.setKeywords(keyWords);
            
            result = new Result();            
            result.setOutputs(machine.search(inputs));
            result.setSearchTime(machine.getSearchTime());
            result.setHits(machine.getTotalHits());
            
            return true;
        } catch (IOException ex) {
            Logger.getLogger(SearchFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(SearchFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
}
