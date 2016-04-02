/* 
    Document   : SearchResult
    Created on : 04-Mar-2016, 07:33:59
    Author     : Quoc
*/

package assignment1.searcher.model;

import common.LanguageUtils;
import common.LuceneUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.apache.lucene.search.TopDocs;

public class SearchResult {
    private final long searchTime;    
    private final int noHits;
    private final List<SearchResultItem> resultItems;
    private long categoryTime;
    private List<ResultCategory> categories;    

    public SearchResult(long searchTime, int noHits, List<SearchResultItem> resultItems) {
        this.searchTime = searchTime;
        this.noHits = noHits;
        this.resultItems = resultItems;    
        
        long startTime = System.currentTimeMillis();
        this.categories = generateCategories(resultItems);
        long endTime = System.currentTimeMillis();
        categoryTime = endTime - startTime;
    }
	
    public long getSearchTime() {
        return searchTime;
    }
    
    public int getNoHits() {
        return this.noHits;
    }

    public List<SearchResultItem> getResultItems () {
            return this.resultItems;
    }

    public List<ResultCategory> getCategories() {
        return categories;
    }    
    
    public long getCategoryTime() {
        return categoryTime;
    }
    
    List<ResultCategory> generateCategories(List<SearchResultItem> items) {
        
        Map<String, String> TermMap = new HashMap();
        Map<String, ResultCategory> CategoryMap = new HashMap();
                
        //for (String content : sentences) {
        for (SearchResultItem item : items) {
            try {
                List<String> terms = LanguageUtils.getNounPhrasesForWeb(item.getDocTitle());
            
                for (String term : terms) {
                    //term map sterm key
                    if (!TermMap.containsKey(term)) {
                        TermMap.put(term, LuceneUtils.stem(term));
                    }
                    String key = TermMap.get(term);
                    
                    //category map to key
                    if (!CategoryMap.containsKey(key)) {                        
                        CategoryMap.put(key, new ResultCategory(term));
                    }
                    ResultCategory category = CategoryMap.get(key);
                    category.getDocIds().add(item.getId());
                }
            } catch (NoSuchElementException ex) {
                System.out.println(ex.toString());
            }
        }
        
        //update categories' size
        List<ResultCategory> resCategories = new ArrayList<ResultCategory>(CategoryMap.values());
        for (ResultCategory c : resCategories) {
            c.setSize(c.getDocIds().size());            
        }
        
        //sort categories
        Collections.sort(resCategories, new Comparator<ResultCategory>() {
            @Override
            public int compare(ResultCategory lhs, ResultCategory rhs) {
                if (lhs.getSize() == rhs.getSize()) {
                    return lhs.getOrder()> rhs.getOrder() ? -1 : (lhs.getOrder() == rhs.getOrder()) ? 0 : 1;
                }
                return lhs.getSize() > rhs.getSize() ? -1 :  1;
            }
        });
        
        //check documents existed in categories 
        Set<Integer> checkedDocuments = new HashSet<Integer>();
        for (int i=0; i<10; i++) {
            ResultCategory category = resCategories.get(i);
            for (int j=0; j < category.getDocIds().size(); j++) {
                Integer docId = category.getDocIds().get(j);
                if (!checkedDocuments.contains(docId)) {
                    checkedDocuments.add(docId);
                }
            }
        }
        //keep the list only first 10
        while (resCategories.size() > 10) {
            resCategories.remove(10);
        }
        
        //create 'Others' contain remaining docs
        ResultCategory otherCategory = new ResultCategory();
        otherCategory.setName("Others");
        for (SearchResultItem item : items) {
            if (!checkedDocuments.contains(item.getId())) {
                otherCategory.getDocIds().add(item.getId());
            }
        }                
        otherCategory.setSize(otherCategory.getDocIds().size());    
        if (otherCategory.getSize() > 0) {
            resCategories.add(otherCategory);
        }
        
        return resCategories;
    }    

}
