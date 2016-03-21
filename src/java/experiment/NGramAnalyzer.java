/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experiment;

import java.io.IOException;
import java.io.StringReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

/**
 *
 * @author nguyentritin
 */
public class NGramAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer source = new StandardTokenizer();
		TokenStream filter = new StopFilter(new LowerCaseFilter(
				new ShingleFilter(source,2,2)), StopAnalyzer.ENGLISH_STOP_WORDS_SET);
		
		TokenStreamComponents components = new TokenStreamComponents(source, filter);
		return components;
	}
	
	
	public static void main(String[] args) throws IOException {
		String str = "An easy way to write an analyzer for tokens bi-gram (or even token n-gram) with Lucene";
		Analyzer analyzer = new NGramAnalyzer();
		
		TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(str));
		// The Analyzer class will construct the Tokenizer, TokenFilter(s), and CharFilter(s),
		// and pass the result Reader to the Tokenizer
		OffsetAttribute offsetAttr = tokenStream.addAttribute(OffsetAttribute.class);
		
		try {
			tokenStream.reset();
			while (tokenStream.incrementToken()) {
				// User AttributeSource.reflectAsString(boolean)
				// for token stream debuggin
				System.out.println("token: " + tokenStream.reflectAsString(false));

				System.out.println("token start offset: " + offsetAttr.startOffset());
				System.out.println("  token end offset: " + offsetAttr.endOffset());
			}
			tokenStream.end();
		} finally {
			tokenStream.close();
		}
		
	}
}
