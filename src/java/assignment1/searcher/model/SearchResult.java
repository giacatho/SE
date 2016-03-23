/* 
    Document   : SearchResult
    Created on : 04-Mar-2016, 07:33:59
    Author     : Quoc
*/

package assignment1.searcher.model;

import java.util.List;

public class SearchResult {
    private final long searchTime;
    private final int noHits;
    private final List<SearchResultItem> resultItems;

	public SearchResult(long searchTime, int noHits, List<SearchResultItem> resultItems) {
		this.searchTime = searchTime;
		this.noHits = noHits;
		this.resultItems = resultItems;
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
}
