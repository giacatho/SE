package assignment2b.indexer.model;

/**
 *
 * @author a
 */
public class A2bDBLPItem {
    private String title;
    private String pubyear;
    private String pubvenue;

    public A2bDBLPItem() {
		
	}

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
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
        return pubvenue;
    }

    @Override
    public String toString() {
        return String.format("title:%s\npubyear:%s\npubvenue:%s\n\n", 
				this.title, this.pubyear, this.pubvenue);
    }
}
