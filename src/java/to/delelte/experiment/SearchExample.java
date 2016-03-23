package to.delelte.experiment;

import common.Constants;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author giacatho
 */
public class SearchExample {
	private final IndexReader reader;
	private final IndexSearcher searcher;
			
	public SearchExample() throws IOException {
		this.reader = DirectoryReader.open(FSDirectory.open(
				Paths.get(Constants.INDEX_STANDARD_ANALYZER_DIR)));
//		this.reader = DirectoryReader.open(FSDirectory.open(
//				Paths.get(Constants.INDEX_ASSIGNMENT2B_DIR)));
		this.searcher = new IndexSearcher(reader);
	}

	private int searchNoDocumentFromYearByTotalHitCountCollector(String year) throws IOException {
		TermQuery yearQuery = new TermQuery(new Term("pubyear", year));

		TotalHitCountCollector collector = new TotalHitCountCollector();
		this.searcher.search(yearQuery, collector);
		
		return collector.getTotalHits();
	}
	
	private int searchNoDocumentFromYearByTopDocs(String year) throws IOException {
		TermQuery yearQuery = new TermQuery(new Term("pubyear", year));
		
		TopDocs topDocs = this.searcher.search(yearQuery, reader.maxDoc());
		
		return topDocs.totalHits;
	}
	
	public void close() throws IOException {
		this.reader.close();
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		SearchExample searcher = new SearchExample();
		
		System.out.println("Total results for year 2005 - searchNoDocumentFromYearByTotalHitCountCollector " + 
				searcher.searchNoDocumentFromYearByTotalHitCountCollector("2005"));
		
		System.out.println("Total results for year 2005 - searchNoDocumentFromYearByTopDocs " + 
				searcher.searchNoDocumentFromYearByTopDocs("2005"));
		
		searcher.close();
	}
	
	
}
