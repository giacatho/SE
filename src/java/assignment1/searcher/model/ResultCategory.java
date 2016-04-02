/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment1.searcher.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cnaquoc
 */
public class ResultCategory {
    private String name;    
    private List<Integer> docIds;
    private int size;
    private int order;

    public ResultCategory() {
        docIds = new ArrayList<Integer>();
    }
    
    public ResultCategory(String name) {
        this();
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getDocIds() {
        return docIds;
    }

    public void setDocIds(List<Integer> docIds) {
        this.docIds = docIds;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }    

    public int getOrder() {
        return order;
    }

    public void setOrder(int position) {
        this.order = position;
    }
}
