/**
 * Author: Liu Hongzhi
 * Email: liuh0051@e.ntu.edu.sg
 * Date 13 Mar, 2016
 */
package se.textindexer;

import java.io.Reader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseTokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;

/**
 *
 * @author a
 */
public class MyAnalyzer_PorterStem extends Analyzer {
    @Override
    protected TokenStreamComponents createComponents(String fieldName){
        Tokenizer source = new LowerCaseTokenizer();
        TokenStreamComponents components = new TokenStreamComponents(source, new PorterStemFilter(source));
        //TokenStreamComponents components = new TokenStreamComponents(source);
        return components;
    }
}
