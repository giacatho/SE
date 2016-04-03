package assignment2a.searcher;

import common.Constants;
import common.Utils;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map.Entry;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author giacatho
 */
public class A2aPopularSearcher {
    private final IndexReader reader;
    private final IndexSearcher searcher;

    public A2aPopularSearcher () throws IOException {
            this.reader = DirectoryReader.open(FSDirectory.open(
                            Paths.get(Constants.INDEX_ASSIGNMENT2A_DIR)));
            this.searcher = new IndexSearcher(reader);
    }

    private void searchPopularTopic(Query query) throws IOException {
            TopDocs results = this.searcher.search(query, this.reader.maxDoc());

            if (results.totalHits == 0) {
                    System.out.println("Can not find any document. Please recheck the query.");
                    return;
            }

//		System.out.println("Total documents found : " + results.scoreDocs.length); 

            System.out.println("\n-------------------------------");
            System.out.println("Most pupular single word topics:");
            List<Entry<String, Integer>> topTerms = Utils.getTopTermFrequencies(this.reader, 
                            results.scoreDocs, 1);
            Utils.printTopTerms(topTerms);

            System.out.println("\n-------------------------------");
            System.out.println("Most pupular bi-word topics");
            topTerms = Utils.getTopTermFrequencies(this.reader, 
                            results.scoreDocs, 2);
            Utils.printTopTerms(topTerms);

            System.out.println("\n-------------------------------");
            System.out.println("Most pupular tri-word topics");
            topTerms = Utils.getTopTermFrequencies(this.reader, 
                            results.scoreDocs, 3);
            Utils.printTopTerms(topTerms);

            System.out.println("\n\n");
    }

    public void searchPopularTopicInYear(String year) throws IOException {
            System.out.println("Seaching for year " + year);
            TermQuery yearQuery = new TermQuery(new Term("pubyear", year));

            this.searchPopularTopic(yearQuery);
    }

    public void searchPopularTopicInYearVenue(String year, String veneu) throws IOException {
            System.out.println("Seaching for venue " + veneu + "in year " + year);
            TermQuery yearQuery = new TermQuery(new Term("pubyear", year));
            TermQuery venueQuery = new TermQuery(new Term("pubvenue", veneu));

            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            queryBuilder.add(yearQuery, BooleanClause.Occur.MUST);
            queryBuilder.add(venueQuery, BooleanClause.Occur.MUST);

            Query query = queryBuilder.build();

            this.searchPopularTopic(query);
    }

    public void searchPopularTopicInYearAuthor(String year, String author) throws IOException {
            System.out.println("Seaching for author " + author + "in year " + year);

            TermQuery yearQuery = new TermQuery(new Term("pubyear", year));
            TermQuery authorQuery = new TermQuery(new Term("author", author));

            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            queryBuilder.add(yearQuery, BooleanClause.Occur.MUST);
            queryBuilder.add(authorQuery, BooleanClause.Occur.MUST);

            Query query = queryBuilder.build();

            this.searchPopularTopic(query);
    }

    public void close() throws IOException {
            this.reader.close();
    }

    public static void main(String[] args) throws IOException {
            A2aPopularSearcher searcher = new A2aPopularSearcher();

            for (int year = 2013; year <= 2013; year++) {
                    searcher.searchPopularTopicInYear("" + year);
            }

//		// Search year venue
//		searcher.searchPopularTopicInYearVenue("2005", "SIGIR");
//		searcher.searchPopularTopicInYearVenue("2015", "SIGIR");
//		
//		
//		// Search year author
//		searcher.searchPopularTopicInYearAuthor("2005", "Martin Fowler");
//		searcher.searchPopularTopicInYearAuthor("1971", "Donald E. Knuth");
//		searcher.searchPopularTopicInYearAuthor("1993", "Guido van Rossum");

            searcher.close();
    }	
}
