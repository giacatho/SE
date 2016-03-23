package assignment2b.indexer;


import assignment2b.indexer.model.YearVenue;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import common.Constants;

public class A2bIndexBuilder {

	private final Map<YearVenue, List<String>> yearVenueToTitleList;
    private IndexWriter writer;

	public A2bIndexBuilder (Map<YearVenue, List<String>> yearVenueToTitleList) {
		this.yearVenueToTitleList = yearVenueToTitleList;
	}
	
	public void buildIndex() throws IOException {
		initIndexWriter(Constants.INDEX_ASSIGNMENT2B_DIR, new StandardAnalyzer());
		
		int count = 0;
		for (Map.Entry<YearVenue, List<String>> entrySet : yearVenueToTitleList.entrySet()) {
			YearVenue yearVenue = entrySet.getKey();
			List<String> titleList = entrySet.getValue();
			
			// TODO need more beautiful title, not use to string
			this.addToIndex(yearVenue.getYear(), yearVenue.getVenue(), titleList.toString()); 
			
			if (count % 1000 == 0) {
                System.out.print(".");
            }
            count++;
		}
		
		this.close();
    }

    private void initIndexWriter(String storePath, Analyzer analyzer) throws IOException {
        Path pathIndexStore = Paths.get(storePath);
        Directory indexStoreDir = FSDirectory.open(pathIndexStore);
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        writer = new IndexWriter(indexStoreDir, iwc);
    }

    private void addToIndex(String year, String venue, String titles) throws IOException {
		Document doc = new Document();
        
		// In this assignment, term vector needs to be stored but the title itself does not
		FieldType myFieldType = new FieldType(TextField.TYPE_NOT_STORED);
		myFieldType.setStoreTermVectors(true);
		
        doc.add(new Field("title", titles, myFieldType));
		
		doc.add(new StringField("pubyear", year, Field.Store.YES));
		doc.add(new StringField("pubvenue", venue, Field.Store.YES));
        
        this.writer.addDocument(doc);
    }
	
    private void close() throws IOException {
        this.writer.close();
    }

}
