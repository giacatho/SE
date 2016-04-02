/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.IOException;
import java.io.StringReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.stempel.StempelStemmer;

/**
 *
 * @author cnaquoc
 */
public final class LuceneUtils {
    static Analyzer analyzer; // = new StandardAnalyzer();
    
    public static String stem(String string) {    
        analyzer = new StandardAnalyzer();
        String result = "";
        try {
            TokenStream stream  = analyzer.tokenStream(null, string);
            stream.reset();
            while (stream.incrementToken()) {
                result += stream.getAttribute(CharTermAttribute.class).toString() + " ";
            }
            stream.close();
        } catch (IOException e) {
            // not thrown b/c we're using a string reader...
            throw new RuntimeException(e);
        }
                
        return result.trim();
    }
    
//    public static stem (String term) {
//        StempelStemmer stemmer = new StempelStemmer();
//        return stemmer.stem(term);
//    }
}
