/* 
 Document   : A1Searcher
 Created on : 03-Mar-2016, 19:52:18
 Author     : Quoc
 */
package assignment1.searcher;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
import common.Constants;
import assignment1.searcher.model.Field;
import assignment1.searcher.model.SearchResult;
import assignment1.searcher.model.SearchInput;
import assignment1.searcher.model.SearchResultItem;
import common.Utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.PhraseQuery;

public class A1Searcher {

	static String[] AllFields = new String[]{"title", "author", "pubyear", "pubvenue"};

	IndexSearcher searcher = null;

	public A1Searcher() throws IOException {
		IndexReader reader = DirectoryReader.open(FSDirectory.open(
				Paths.get(Constants.INDEX_ASSIGNMENT1_DIR)));
		this.searcher = new IndexSearcher(reader);
	}

	Analyzer getAnalyzer() {
		return new StandardAnalyzer();
	}

	BooleanClause.Occur getOccur(SearchInput input) {
		switch (input.getOperator()) {
			case AND:
				return BooleanClause.Occur.MUST;
			case NOT:
				return BooleanClause.Occur.MUST_NOT;
			case OR:
				return BooleanClause.Occur.SHOULD;
		}

		return null;
	}
	
//	http://stackoverflow.com/questions/24784707/removing-all-the-characters-between-two-specific-tags-java-regex
	private Query parse(String query, String field) throws ParseException {
		BooleanQuery.Builder booleanBuilder = new BooleanQuery.Builder();
		PhraseQuery.Builder phraseBuilder; 
		
		List<String> phrases = this.getPhrases(query);
		for (String phrase: phrases) {
			phraseBuilder = new PhraseQuery.Builder();
			String[] words = phrase.split("\\s+");
			for (String word:words) {
				phraseBuilder.add(new Term(field, word));
			}
			
			booleanBuilder.add(phraseBuilder.build(), BooleanClause.Occur.MUST);
		}
		
		query = query.replaceAll("\"([^\"]*)\"", "").trim();
		if (!query.isEmpty()) {
			QueryParser queryParser = new QueryParser(field, getAnalyzer());
			booleanBuilder.add(queryParser.parse(query), BooleanClause.Occur.MUST);
		}
		
		return booleanBuilder.build();
	}

	private Query parse(SearchInput input) throws ParseException {
		String indexField;
		Field field = input.getField();
		if (field == null) {
			indexField = "all";
		} else {
			indexField = field.toString().toLowerCase();
		}
			
		String query = input.getKey().trim();
		
		return this.parse(query, indexField);
	}
	
	public Query parse(List<SearchInput> inputs) throws ParseException {
		if (inputs.size() == 1) {
			return parse(inputs.get(0));
		}

		BooleanQuery.Builder query = new BooleanQuery.Builder();
		
		for (SearchInput input : inputs) {
			query.add(this.parse(input), getOccur(input));
		}

		return query.build();
	}

	public SearchResult search(List<SearchInput> inputs)
			throws IOException, ParseException {
		return search(inputs, 10);
	}

	public SearchResult search(List<SearchInput> inputs, int numberOfHits) 
			throws IOException, ParseException {
		
		Query query = parse(inputs);
		
		long startTime = System.currentTimeMillis();
		TopDocs hits = this.searcher.search(query, numberOfHits);
		long endTime = System.currentTimeMillis();

		List<String> keywords = Utils.getKeywords(inputs);
		List<SearchResultItem> resultItems = new ArrayList();
		
                int id = 0;
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = this.searcher.doc(scoreDoc.doc);
			
			resultItems.add(new SearchResultItem(
                                        id,
					keywords,
					scoreDoc.score, 
					doc.get("key"), 
					doc.get("title"),
					Arrays.asList(doc.getValues("author")),
					doc.get("pubyear"),
					doc.get("pubvenue")
			));                        
                        id++;
		}
		
		return new SearchResult(endTime - startTime, hits.totalHits, resultItems);
	}
	
	//http://stackoverflow.com/questions/1473155/how-to-get-data-between-quotes-in-java
	private List<String> getPhrases(String query) {
		List<String> phrases = new ArrayList(); 
		Pattern p = Pattern.compile("\"([^\"]*)\"");
		Matcher m = p.matcher(query);
		while (m.find()) {
			phrases.add(m.group(1).trim().toLowerCase());
		}
		
		return phrases;
	}
	
	public static void main(String[] args) throws Exception {
		A1Searcher searcher = new A1Searcher();
		
		//searcher.searchAll("ib \"How are you\" I get \"two phrases\" that");
		
	}
}
