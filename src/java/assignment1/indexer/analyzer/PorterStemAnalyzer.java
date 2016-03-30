/**
 * Author: Liu Hongzhi
 * Email: liuh0051@e.ntu.edu.sg
 * Date 13 Mar, 2016
 */
package assignment1.indexer.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseTokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;

/**
 *
 * @author a
 */
public class PorterStemAnalyzer extends Analyzer {
    @Override
    protected TokenStreamComponents createComponents(String fieldName){
        Tokenizer source = new LowerCaseTokenizer();
        TokenStreamComponents components = new TokenStreamComponents(source, new PorterStemFilter(source));
        return components;
    }
}
