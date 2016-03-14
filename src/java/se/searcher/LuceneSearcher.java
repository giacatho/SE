/* 
    Document   : LuceneSearcher
    Created on : 03-Mar-2016, 19:52:18
    Author     : Quoc
*/

package se.searcher;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import se.constant.Constants;
import se.searcher.gui.Field;
import se.searcher.gui.SearchInput;
import se.searcher.gui.SearchOutput;


public class LuceneSearcher {
	static String[] AllFields = new String[] {"title", "author"};
	
	IndexSearcher IS = null;
	
	long searchTime;
	public long getSearchTime() {
		return searchTime;
	}
	
	int totalHits;
	public int getTotalHits() {
		return totalHits;
	}
	
	public LuceneSearcher() throws IOException {
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(Constants.INDEX_SIMPLE_ANALYZER_DIR)));
		IS = new IndexSearcher(reader);
	}
	
	Analyzer getAnalyzer() {
		// Tin: the field is never used in previous version
		return new StandardAnalyzer(); 
	}
	
	BooleanClause.Occur getOccur(SearchInput input) {
		switch (input.getOperator()) {
			case AND: return BooleanClause.Occur.MUST;
			case NOT: return BooleanClause.Occur.MUST_NOT;
			case OR: return BooleanClause.Occur.SHOULD;
		}
		
		return null;
	}
	
	Query parse(SearchInput input) throws ParseException {
		QueryParser parser = null;
		
		Field field = input.getField();
		if (field == null) {
			parser = new MultiFieldQueryParser(AllFields, getAnalyzer());
		} else {
			parser = new QueryParser(field.toString().toLowerCase(), getAnalyzer());
		}
		
		return parser.parse(input.getKey());
	}
	
	Query parse(List<SearchInput> inputs) throws ParseException {
		if (inputs.size() == 1) 
			return parse(inputs.get(0));
		
		BooleanQuery query = new BooleanQuery();
		for (SearchInput input: inputs) {
			query.add(parse(input), getOccur(input));
		}
		
		return query;
	}
	
	public List<SearchOutput> search(List<SearchInput> inputs) throws IOException, ParseException {
		return search(inputs, 10);
	}
	
	public List<SearchOutput> search(List<SearchInput> inputs, int numberOfHits) throws IOException, ParseException {
		Query query = parse(inputs);
		long start = System.currentTimeMillis();
		
		TopDocs hits = IS.search(query, numberOfHits);
		totalHits = hits.totalHits;
		long end = System.currentTimeMillis();
		
		searchTime = end - start;
		
		List<SearchOutput> outputs = new ArrayList();
		for (ScoreDoc scoreDoc: hits.scoreDocs) {
			Document doc = IS.doc(scoreDoc.doc);
			
			outputs.add(new SearchOutput(doc));
		}
		
		return outputs;
	}
}