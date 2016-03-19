package assignment2b.searcher;

import assignment2b.indexer.model.DocSimilarity;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
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
public class A2bDblpSimpleSearcher {
	private final IndexReader reader;
	private final IndexSearcher searcher;
			
	public A2bDblpSimpleSearcher() throws IOException {
		this.reader = DirectoryReader.open(FSDirectory.open(
				Paths.get(Constants.INDEX_ASSIGNMENT2B_DIR_TMP)));
		this.searcher = new IndexSearcher(reader);
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		A2bDblpSimpleSearcher searcher = new A2bDblpSimpleSearcher();
		
		searcher.searchSimilarity("2010", "FOCS");
		searcher.searchSimilarity("2011", "INFOCOM");
		searcher.searchSimilarity("2012", "WSDM");
		searcher.searchSimilarity("2013", "CIKM");
		searcher.searchSimilarity("2014", "SIGIR");
		searcher.searchSimilarity("2015", "KDD");
		
		searcher.close();
	}
	
	public void searchSimilarity(String year, String venue) throws IOException, ParseException {
		ScoreDoc document = this.searchDocument(year, venue);
		
		if (document == null) {
			System.out.println("Year and venue are not found");
			return;
		}
		
		Utils.printDocumentYearAndVenue(this.searcher, document.doc);
		
		int numDocs = reader.maxDoc();
		List<DocSimilarity> docSims = new ArrayList();
		for (int i = 0; i < numDocs; i++) {
			if (i == document.doc)
				continue;

			double cosSim = Utils.getCosineSimilarity(reader, i, document.doc);

			docSims.add(new DocSimilarity(i, cosSim));
		}

		Collections.sort(docSims, Collections.reverseOrder());
		
		System.out.println("Result for " + venue + " " + year);
		printSearchResult(docSims);
	}
	
	private void printSearchResult(List<DocSimilarity> docSims) throws IOException {
		for (int i = 0; i < 10 && i < docSims.size(); i++) {
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
		
		TopDocs results = this.searcher.search(query, 10);
		
		if (results.totalHits == 0) {
			return null;
		}

		return results.scoreDocs[0];
	}
	
	public void close() throws IOException {
		this.reader.close();
	}
}
