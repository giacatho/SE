/* 
    Document   : Result
    Created on : 04-Mar-2016, 07:33:59
    Author     : Quoc
*/

package se.searcher.gui;

import java.util.List;

public class Result {
    private long searchTime;
    private int hits;
    private List<SearchOutput> outputs;

    public long getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(long searchTime) {
        this.searchTime = searchTime;
    }    
    
    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public List<SearchOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<SearchOutput> outputs) {
        this.outputs = outputs;
    }

}
