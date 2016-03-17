package assignment2b.searcher;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import se.constant.Constants;

/**
 *
 * @author nguyentritin
 */
public class A2bDblpSimpleSearcher {
	public static void main(String[] args) throws IOException, ParseException {
		IndexReader reader = DirectoryReader.open(FSDirectory.open(
				Paths.get(Constants.INDEX_ASSIGNMENT2B_DIR)));
		IndexSearcher searcher = new IndexSearcher(reader);
		ScoreDoc document = search(searcher, "2005", "OPODIS");
		
		if (document == null) {
			System.out.println("Year and venue are not found");
			return;
		}
		
		System.out.println("Found with score " + document.score);
		
		printDocumentInfo(searcher, document.doc);
		printAllTerms(searcher, document.doc, "title");
	}
	
	public static ScoreDoc search(IndexSearcher searcher, String year, 
			String venue) throws IOException, ParseException {
		TermQuery yearQuery = new TermQuery(new Term("pubyear", year));
		TermQuery venueQuery = new TermQuery(new Term("pubvenue", venue));
		
		BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
		queryBuilder.add(yearQuery, BooleanClause.Occur.MUST);
		queryBuilder.add(venueQuery, BooleanClause.Occur.MUST);

		Query query = queryBuilder.build();
		
		TopDocs results = searcher.search(query, 10);
		
		if (results.totalHits == 0) {
			return null;
		}

		return results.scoreDocs[0];
	}
	
	public static void printDocumentInfo(IndexSearcher searcher, int luceneDocId) throws IOException {
		Document doc = searcher.doc(luceneDocId);
		
		List<IndexableField> allFields = doc.getFields();
		System.out.println("Start to print document: ");
		for (IndexableField field : allFields) {
			System.out.println(field.name() + ": " + doc.get(field.name()));
		}
	}
	
	public static void printAllTerms(IndexSearcher searcher, int luceneDocId, String field) 
			throws IOException {
		Terms terms = searcher.getIndexReader().getTermVector(1, field);
		
		TermsEnum termsEnum = terms.iterator();
		BytesRef text;
		System.out.println("Start to print terms: ");
		while((text = termsEnum.next()) != null) {
			System.out.print(text.utf8ToString() + ", ");
		}
	}
}
