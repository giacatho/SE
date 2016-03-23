/**
 * Author: Liu Hongzhi
 * Email: liuh0051@e.ntu.edu.sg 
 * Date 13 Mar, 2016
 */

package assignment1.indexer.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;

/**
 *
 * @author a
 */
public class MyAnalyzer_LetterTokenizer extends Analyzer {
    @Override
    protected Analyzer.TokenStreamComponents createComponents(String fieldName){
        Tokenizer source = new LetterTokenizer();
        TokenStreamComponents components = new TokenStreamComponents(source);
        return components;
    }
}