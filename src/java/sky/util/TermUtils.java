/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sky.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;

/**
 *
 * @author cnaquoc
 */
public final class TermUtils {
    static Analyzer analyzer = new EnglishAnalyzer(EnglishAnalyzer.getDefaultStopSet());
    public static Analyzer getAnalyzer() {
        return analyzer;
    }
}
