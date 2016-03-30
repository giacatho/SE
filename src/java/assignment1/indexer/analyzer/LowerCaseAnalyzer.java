/**
 * Author: Liu Hongzhi
 * Email: liuh0051@e.ntu.edu.sg 
 * Date 13 Mar, 2016
 */

package assignment1.indexer.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseTokenizer;

/**
 *
 * @author a
 */
public class LowerCaseAnalyzer extends Analyzer {
    @Override
    protected Analyzer.TokenStreamComponents createComponents(String fieldName){
        Tokenizer source = new LowerCaseTokenizer();
        TokenStreamComponents components = new TokenStreamComponents(source);
        return components;
    }
}