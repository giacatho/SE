package assignment2b.searcher;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import se.constant.Constants;
import se.searcher.model.Field;
import se.searcher.model.SearchInput;

/**
 *
 * @author nguyentritin
 */
public class A2bDblpSimpleSearcher {
	public static void main(String[] args) throws IOException, ParseException {
		IndexReader reader = DirectoryReader.open(FSDirectory.open(
				Paths.get(Constants.INDEX_ASSIGNMENT2B_DIR)));
		IndexSearcher searcher = new IndexSearcher(reader);
		ScoreDoc document = search(searcher, "2012", "GLOBECOM");
		
		if (document == null) {
			System.out.println("Year and venue are not found");
			return;
		}
		
		System.out.println("Found with score " + document.score);
		printDocumentInfo(searcher, document.doc);
		printAllTerms(searcher, document.doc, "title");
		
		// Document doc = searcher.doc(document.doc);
		
		// String title = doc.get("title");
		
//		if (title == null) {
//			System.out.println("Can not get title");
//			return;
//		}
		
		//Query query = getQuery(title), 3);
		
		Query query = getTitleQuery(searcher, document.doc);
		
		TopDocs results = searcher.search(query, 3);
		System.out.println("Near conferences:");
		for (ScoreDoc scoreDoc : results.scoreDocs) {
			if (scoreDoc.doc != document.doc) {
				System.out.println("Score: " + scoreDoc.score);
				printDocumentInfo(searcher, scoreDoc.doc);
				//printAllTerms(searcher, scoreDoc.doc, "title");
				System.out.println("\n\n\n\n");
			}
		}
		
		//printDocumentInfo(searcher, document.doc);
		//printAllTerms(searcher, document.doc, "title");
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
	
	
	
	public static Query getQuery(String title) throws ParseException {

		QueryParser parser = new QueryParser("title", new StandardAnalyzer());

		title = title.substring(1, title.length()-2);
		System.out.println(title);
		
		BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
		queryBuilder.add(parser.parse(title), BooleanClause.Occur.MUST);
		
		return queryBuilder.build();
	}
	
	public static void printDocumentInfo(IndexSearcher searcher, int luceneDocId) throws IOException {
		Document doc = searcher.doc(luceneDocId);
		
		List<IndexableField> allFields = doc.getFields();
		//System.out.println("Start to print document: ");
		for (IndexableField field : allFields) {
			System.out.println(field.name() + ": " + doc.get(field.name()));
		}
	}
	
	public static void printAllTerms(IndexSearcher searcher, int luceneDocId, String field) 
			throws IOException {
		Terms terms = searcher.getIndexReader().getTermVector(luceneDocId, field);
		
		TermsEnum termsEnum = terms.iterator();
		BytesRef term;
		System.out.println("Start to print terms: ");
		while((term = termsEnum.next()) != null) {
			// enumerate through documents, in this case only one
			DocsEnum docsEnum = termsEnum.docs(null, null); 
			int docIdEnum;
			while ((docIdEnum = docsEnum.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
				// get the term frequency in the document 
				System.out.println(term.utf8ToString()+ " " + docIdEnum + " " + docsEnum.freq()); 
			}
		}
		System.out.println();
	}
	
	public static Query getTitleQuery(IndexSearcher searcher, int luceneDocId) throws IOException   {
		
		Terms terms = searcher.getIndexReader().getTermVector(luceneDocId, "title");
		
		TermsEnum termsEnum = terms.iterator();
		BytesRef text;
		//System.out.println("Start to print terms: ");
		
		BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
		
		while((text = termsEnum.next()) != null) {
			//System.out.print(text.utf8ToString() + ", ");
			TermQuery tq = new TermQuery(new Term("title", text.utf8ToString().trim()));
			queryBuilder.add(tq, BooleanClause.Occur.SHOULD);
		}
		
		return queryBuilder.build();
	}
}
