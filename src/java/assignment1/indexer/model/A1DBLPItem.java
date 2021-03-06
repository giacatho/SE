/**
 * Author: Liu Hongzhi
 * Email: liuh0051@e.ntu.edu.sg
 * Date 6 Mar, 2016
 */
package assignment1.indexer.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

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
    private String type;

    public A1DBLPItem() {
        authors = new ArrayList<String>();
    }

    public A1DBLPItem(String type) {
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
    //TODO: study the difference between different Fields
    public Document getLuceneDocument(){
        Document doc = new Document();
        
		// Tin: Title and author needs term vector to support phrase query
		FieldType myFieldType = new FieldType(TextField.TYPE_STORED);
		myFieldType.setStoreTermVectors(true);
		
        doc.add(new Field("title", this.getTitle(), myFieldType));
		for(String author : this.authors){
			// Tin: author needs term vector to support phrase query
			doc.add(new Field("author", author, myFieldType));
        } 
		
        //TODO: use data field, if necessary
        doc.add(new StringField("key",this.getKey(),Field.Store.YES));
		doc.add(new StringField("pubyear",this.getPubyear(),Field.Store.YES));
        doc.add(new TextField("pubvenue",this.getPubvenue(),Field.Store.YES));
        
        return doc;
    }
}
