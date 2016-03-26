/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sky.searcher;

import assignment1.searcher.model.*;
import common.Constants;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
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
import sky.util.Utils;

/**
 *
 * @author cnaquoc
 */
public class TermSearcher {
    static String[] AllFields = new String[]{"title", "author"};

    IndexSearcher searcher = null;

    public TermSearcher() throws IOException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(
                        Paths.get(Constants.INDEX_FOLDER)));
        this.searcher = new IndexSearcher(reader);
    }

    Analyzer getAnalyzer() {
        return Utils.getDefaultAnalyzer(); //SimpleAnalyzer();//EnglishAnalyzer(EnglishAnalyzer.getDefaultStopSet()); //StandardAnalyzer();
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

    Query parse(SearchInput input) throws ParseException {
        QueryParser parser;

        Field field = input.getField();
        if (field == null) {
                parser = new MultiFieldQueryParser(AllFields, getAnalyzer());
        } else {
                parser = new QueryParser(field.toString().toLowerCase(), getAnalyzer());
        }

        return parser.parse(input.getKey());
    }

    Query parse(List<SearchInput> inputs) throws ParseException {
        if (inputs.size() == 1) {
                return parse(inputs.get(0));
        }

        BooleanQuery.Builder query = new BooleanQuery.Builder();

        for (SearchInput input : inputs) {
                query.add(parse(input), getOccur(input));
        }

        return query.build();
    }
    
    public Query getQuery(List<SearchInput> inputs) throws ParseException {
        return parse(inputs);
    }
    
    TopDocs getTopDocs(List<SearchInput> inputs, int numberOfHits) throws ParseException, IOException {
        Query query = getQuery(inputs);

        TopDocs hits = this.searcher.search(query, numberOfHits);         
        
        return hits;
    }
    
    public SearchResult search(List<SearchInput> inputs)
                    throws IOException, ParseException {
            return search(inputs, 10);
    }

    public SearchResult search(List<SearchInput> inputs, int numberOfHits) 
                    throws IOException, ParseException {

        long startTime = System.currentTimeMillis();

        TopDocs hits = getTopDocs(inputs, numberOfHits);
        long endTime = System.currentTimeMillis();

        List<String> keywords = common.Utils.getKeywords(inputs);
        List<SearchResultItem> resultItems = new ArrayList();

        for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = this.searcher.doc(scoreDoc.doc);

                resultItems.add(new SearchResultItem(
                                keywords,
                                scoreDoc.score, 
                                doc.get("key"), 
                                doc.get("title"),
                                Arrays.asList(doc.getValues("author")),
                                doc.get("pubyear"),
                                doc.get("pubvenue")
                ));
        }

        return new SearchResult(endTime - startTime, hits.totalHits, resultItems);
    }
}
