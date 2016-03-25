package assignment2b.searcher;

import assignment2b.indexer.model.DocSimilarity;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import common.Constants;
import common.Utils;

/**
 *
 * @author nguyentritin
 */
public class A2bSimilarConferencesSearcher {
	private final IndexReader reader;
	private final IndexSearcher searcher;
			
	public A2bSimilarConferencesSearcher() throws IOException {
		this.reader = DirectoryReader.open(FSDirectory.open(
				Paths.get(Constants.INDEX_ASSIGNMENT2B_DIR)));
		this.searcher = new IndexSearcher(reader);
	}
	
	public void searchSimilarity(String year, String venue) throws IOException, ParseException {
		ScoreDoc document = this.searchDocument(year, venue);
		
		if (document == null) {
			System.out.println("YearVenue are not found");
			return;
		}
		
		// To verify that get the correct document
		Document doc = this.searcher.doc(document.doc);
		System.out.println("Year: " + doc.get("pubyear") + ". Venue: " + doc.get("pubvenue") + ".");
		
		int numDocs = reader.maxDoc();
		List<DocSimilarity> docSims = new ArrayList();
		
		// Compute the cosine similarity with all other documents
		for (int i = 0; i < numDocs; i++) {
			if (i == document.doc)
				continue;

			docSims.add(new DocSimilarity(i, Utils.getCosineSimilarity(reader, i, document.doc)));
		}

		Collections.sort(docSims, Collections.reverseOrder());
		
		System.out.println("Similarity publication venue and year for " + venue + " " + year);
		System.out.println("--------------------------------------------------");
		printSearchResult(docSims, 10);
		System.out.println("\n");
	}
	
	private void printSearchResult(List<DocSimilarity> docSims, int maxResult) throws IOException {
		for (int i = 0; i < maxResult && i < docSims.size(); i++) {
			DocSimilarity docSim = docSims.get(i);
			
			Document lSimDoc = this.reader.document(docSim.getLuceneDocId());
			System.out.println(lSimDoc.get("pubvenue") + " " + lSimDoc.get("pubyear") + 
					" - Similarity " + docSim.getCosSim());
		}
    }
	
	private ScoreDoc searchDocument(String year, 
			String venue) throws IOException, ParseException {
		TermQuery yearQuery = new TermQuery(new Term("pubyear", year));
		TermQuery venueQuery = new TermQuery(new Term("pubvenue", venue));
		
		BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
		queryBuilder.add(yearQuery, BooleanClause.Occur.MUST);
		queryBuilder.add(venueQuery, BooleanClause.Occur.MUST);

		Query query = queryBuilder.build();
		
		TopDocs results = this.searcher.search(query, 1);
		
		if (results.totalHits == 0) {
			return null;
		}

		return results.scoreDocs[0];
	}
	
	public void close() throws IOException {
		this.reader.close();
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		A2bSimilarConferencesSearcher searcher = new A2bSimilarConferencesSearcher();
		
		searcher.searchSimilarity("2015", "KDD");
		searcher.searchSimilarity("2014", "SIGIR");
		searcher.searchSimilarity("2013", "CIKM");
		searcher.searchSimilarity("2012", "WSDM");
		searcher.searchSimilarity("2011", "INFOCOM");
		searcher.searchSimilarity("2010", "CVPR");
		
		searcher.close();
	}
}
