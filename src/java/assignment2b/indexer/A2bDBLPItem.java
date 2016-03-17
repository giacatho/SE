package assignment2b.indexer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

/**
 *
 * @author a
 */
public class A2bDBLPItem {
	private int yearAndVenueId;
    private String title;
    private String pubyear;
    private String pubvenue;
    private String type;

    public A2bDBLPItem(String type) {
        this.setType(type);
    }

	public int getYearAndVenueId() {
		return yearAndVenueId;
	}

	public void setYearAndVenueId(int yearAndVenueId) {
		this.yearAndVenueId = yearAndVenueId;
	}
	
    //setter and getter
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
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
        return String.format("type:%s\ntitle:%s\npubyear:%s\npubvenue:%s\nyearAndVenueId:%s\n\n", 
				this.type, this.title, this.pubyear, this.pubvenue, this.yearAndVenueId);
    }
    //TODO: study the difference between different Fields
    public Document getLuceneDocument(){
        Document doc = new Document();
        
		// Tin: Title and author needs term vector to support phrase query
		FieldType myFieldType = new FieldType(TextField.TYPE_STORED);
		myFieldType.setStoreTermVectors(true);
		
        doc.add(new Field("title", this.getTitle(), myFieldType));
		
        //TODO: use data field, if necessary
		doc.add(new StringField("pubyear",this.getPubyear(),Field.Store.YES));
        doc.add(new TextField("pubvenue",this.getPubvenue(),Field.Store.YES));
        
        return doc;
    }
}
