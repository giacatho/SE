package assignment2a.indexer;


import assignment2a.indexer.analyzer.MultiwordAnalyzer;
import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import common.Constants;
import common.Utils;
import java.util.List;

public class A2aIndexBuilder {
    private final IndexWriter writer;

	public A2aIndexBuilder () throws IOException {
		this.writer = Utils.initIndexWriter(Constants.INDEX_ASSIGNMENT2A_DIR, new MultiwordAnalyzer());
	}

    public void addToIndex(String year, String venue, String title, List<String> authors) throws IOException {
		Document doc = new Document();
        
		// For this assignment, term vector needs to be stored but the title itself does not
		FieldType myFieldType = new FieldType(TextField.TYPE_STORED);
		myFieldType.setStoreTermVectors(true);
		
        doc.add(new Field("title", title, myFieldType));
		
		doc.add(new StringField("pubyear", year, Field.Store.YES));
		doc.add(new StringField("pubvenue", venue, Field.Store.YES));
		
		for (String author: authors) {
			doc.add(new StringField("author", author, Field.Store.YES));
		}
		
        this.writer.addDocument(doc);
    }
	
    public void close() throws IOException {
        this.writer.close();
    }

}
