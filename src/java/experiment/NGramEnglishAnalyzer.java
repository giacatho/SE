/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experiment;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 *
 * @author nguyentritin
 */
public class NGramEnglishAnalyzer extends Analyzer {

	@Override
	// Reference https://fossies.org/linux/www/lucene-5.5.0-src.tgz/lucene-5.5.0/analysis/common/src/java/org/apache/lucene/analysis/en/EnglishAnalyzer.java
	protected TokenStreamComponents createComponents(String fieldName) {
		final Tokenizer source = new StandardTokenizer();
		
		TokenStream result = new StandardFilter(source);
		result = new EnglishPossessiveFilter(result);
		result = new LowerCaseFilter(result);
		result = new StopFilter(result, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
		result = new PorterStemFilter(result);
		result = new ShingleFilter(result);
		
		return new TokenStreamComponents(source, result);
	}
	
}
