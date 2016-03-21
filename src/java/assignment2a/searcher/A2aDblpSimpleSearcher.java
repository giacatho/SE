package assignment2a.searcher;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import se.constant.Constants;
import se.searcher.util.Utils;

/**
 *
 * @author nguyentritin
 */
public class A2aDblpSimpleSearcher {
	private final IndexReader reader;
	private final IndexSearcher searcher;
			
	public A2aDblpSimpleSearcher() throws IOException {
		this.reader = DirectoryReader.open(FSDirectory.open(
				Paths.get(Constants.INDEX_ASSIGNMENT2A_DIR)));
		this.searcher = new IndexSearcher(reader);
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		A2aDblpSimpleSearcher searcher = new A2aDblpSimpleSearcher();

		for (int i=1995; i<=2016; i++) {
			searcher.searchCommonTopic(i + "");
		}
		
		searcher.close();
	}
	
	public void searchCommonTopic(String year) throws IOException, ParseException {
		ScoreDoc document = this.searchDocument(year);
		
		if (document == null) {
			System.out.println("Year and venue are not found");
			return;
		}
		
		Document doc = this.reader.document(document.doc);
		System.out.println("Year " + doc.get("pubyear"));
		
		Map<String, Integer> termFrequencyMap = Utils.getTermFrequencies(this.reader, document.doc, true);
		List<Entry<String, Integer>> sortedTermFequencies = Utils.getEntriesSortedByValues(termFrequencyMap);
		this.printSearchResult(sortedTermFequencies);
	}
	
	private ScoreDoc searchDocument(String year) throws IOException, ParseException {
		TermQuery yearQuery = new TermQuery(new Term("pubyear", year));
		
		TopDocs results = this.searcher.search(yearQuery, 1);
		
		if (results.totalHits == 0) {
			return null;
		}

		return results.scoreDocs[0];
	}
	
	public void close() throws IOException {
		this.reader.close();
	}
	
	public static ScoreDoc search(IndexSearcher searcher, String year) 
			throws IOException, ParseException {
		TermQuery yearQuery = new TermQuery(new Term("pubyear", year));
		
		BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
		queryBuilder.add(yearQuery, BooleanClause.Occur.MUST);

		Query query = queryBuilder.build();
		
		TopDocs results = searcher.search(query, 10);
		
		if (results.totalHits == 0) {
			return null;
		}

		return results.scoreDocs[0];
	}
	
	private void printSearchResult(List<Entry<String, Integer>> sortedTermFequencies) throws IOException {
		for (int i = 0; i < 10 && i < sortedTermFequencies.size(); i++) {
			Entry termFrequency = sortedTermFequencies.get(i);
			
			System.out.println("Term: " + termFrequency.getKey() + ". Frequency: " + 
					termFrequency.getValue());
		}
    }
}
