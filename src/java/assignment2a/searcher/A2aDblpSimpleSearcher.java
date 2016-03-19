package assignment2a.searcher;

import java.io.IOException;
import java.nio.file.Paths;
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
	public static void main(String[] args) throws IOException, ParseException {
		IndexReader reader = DirectoryReader.open(FSDirectory.open(
				Paths.get(Constants.INDEX_ASSIGNMENT2B_DIR)));
		IndexSearcher searcher = new IndexSearcher(reader);
		ScoreDoc document = search(searcher, "2005");
		
		if (document == null) {
			System.out.println("Document for Year not found");
			return;
		}
		
		System.out.println("Found with score " + document.score);
		Utils.printDocumentInfo(searcher, document.doc);
		Utils.printAllTerms(searcher, document.doc, "title");
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
}
