package assignment1.searcher;

import common.Constants;
import common.Utils;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author giacatho
 */
public class A1TermsCounter {
	IndexReader reader;
	
	public A1TermsCounter() throws IOException {
		reader = DirectoryReader.open(FSDirectory.open(
					Paths.get(Constants.INDEX_ASSIGNMENT1_DIR)));
	}
	
	public static void main(String args[]) throws IOException {
		A1TermsCounter counter = new A1TermsCounter();
		
		System.out.println("Term sizes: " + Utils.getAllTerms(counter.reader, "title").size());
		
		counter.reader.close();
	}
}
