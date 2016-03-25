package assignment2a.indexer.model;

import java.util.ArrayList;
import java.util.List;

public class A2aDBLPItem {
    private String pubyear;
	private String pubvenue; 
	private String title;
	private List<String> authors;

    public A2aDBLPItem() {
		authors = new ArrayList();
    }
	
	public void setPubyear(String pubyear) {
        this.pubyear = pubyear;
    }

    public String getPubyear() {
        return pubyear;
    }
	
	public void setPubvenue(String pubvenue) {
		this.pubvenue = pubvenue;
	}
	
	public String getPubvenue() {
		return this.pubvenue;
	}

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
	
	public void addAuthor(String author) {
		this.authors.add(author);
	}
	
	public List<String> getAuthors() {
		return this.authors;
	}
}
