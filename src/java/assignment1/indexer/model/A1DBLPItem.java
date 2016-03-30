/**
 * Author: Liu Hongzhi
 * Email: liuh0051@e.ntu.edu.sg
 * Date 6 Mar, 2016
 */
package assignment1.indexer.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author a
 */
public class A1DBLPItem {

    private String key;
    private String title;
    private List<String> authors;
    private String pubyear;
    private String pubvenue;

    public A1DBLPItem() {
        authors = new ArrayList<String>();
    }

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
}
