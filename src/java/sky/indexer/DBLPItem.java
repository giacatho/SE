/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sky.indexer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cnaquoc
 */
public class DBLPItem {
    
    private String key;
    private String title;
    private List<String> authors;
    private String pubyear;
    private String pubvenue;
    private String type;

    public DBLPItem() {
        authors = new ArrayList<String>();
    }

    public DBLPItem(String type) {
        this.setType(type);
        authors = new ArrayList<String>();
    }
    //setter and getter

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    //setter and getter
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    //setter and getter
    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void addAuthor(String author) {
        if(author != null){
            this.authors.add(author);
        }
    }

    public List<String> getAuthors() {
        return authors;
    }

    //setter and getter
    public void setPubyear(String pubyear) {
        this.pubyear = pubyear;
    }

    public String getPubyear() {
        return pubyear;
    }

    //setter and getter
    public void setPubvenue(String pubvenue) {
        this.pubvenue = pubvenue;
    }

    public String getPubvenue() {
        return pubvenue;
    }
    //setter and getter

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        String s = String.format("type:%s\nkey:%s\ntitle:%s\npubvenue:%s\npubyear:%s\n", this.type, this.key, this.title, this.pubvenue, this.pubyear);
        for (String temp: this.authors){
            s += "author:" + temp + "\n";
        }
        s += "\n";
        return s; //To change body of generated methods, choose Tools | Templates.
    }    
}
