/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sky.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.facet.FacetsConfig;
import static sky.util.Utils.wordSource;

/**
 *
 * @author cnaquoc
 */
public final class FacetUtils {
    
    static FacetsConfig config = null;    
    public static FacetsConfig getConfig() {
        if (config == null) {
            config = new FacetsConfig();
            //config.setHierarchical(FieldNaming, true);
            //config.setIndexFieldName(FieldNaming.Facet.Title, FieldNaming.Term.Title);
//            config.setIndexFieldName(FieldNaming.Facet.Venue, FieldNaming.Term.Venue);
//            config.setIndexFieldName(FieldNaming.Facet.Year, FieldNaming.Term.Year);
            
            //config.setHierarchical(FieldNaming.Facet.Title, true);
            config.setMultiValued(FieldNaming.Facet.Title, true);
//            config.setMultiValued(FieldNaming.Facet.Title1, true);
//            config.setMultiValued(FieldNaming.Facet.Title2, true);
//            config.setMultiValued(FieldNaming.Facet.Title3, true);
//            config.setMultiValued(FieldNaming.Facet.Title4, true);
        }
//        config.setIndexFieldName("Author", "author");
//        config.setIndexFieldName("Publish Date", "pubdate");
//        config.setHierarchical("Publish Date", true);   
        
        return config;
    }            
        
//    static CharArraySet StopWords = EnglishAnalyzer.getDefaultStopSet(); 
//    static List<String> clean(String value) {
//        String[] words = value.split(" ");        
//        
//        List<String> lstWords = new ArrayList<String>();        
//        for (String word: words) {
//            word = word.toLowerCase().replaceAll("[^a-z0-9_]", "");
//            if (word.equals("a")) continue;
//            
//            if (!StopWords.contains(word)) {
//                if (word.length() > 0) lstWords.add(word);
//            }
//        }
//        
//        return lstWords;
//    }
    static final String[] blockPhraseSource = new String[] { "use", "case", "use case", "ad", "hoc", "ad hoc" };
    static final Set<String> BlockPhrases = new HashSet<String>(Arrays.asList(blockPhraseSource));
            
    public static FacetField getHierarchicalFacet(String nameOfField, String value) {
        List<String> lstWords = Utils.clean(value);
                
        if (lstWords.size() > 0) {
            String[] inputWordArray = new String[lstWords.size()];
            return new FacetField(nameOfField, lstWords.toArray(inputWordArray));
        }
        
        return null;
    }
    
    public static List<FacetField> getHierarchicalFacets(String nameOfField, int deepLevel, String value) {
        return getHierarchicalFacets(nameOfField, 1, deepLevel, value);
    }
    
    public static List<FacetField> getHierarchicalFacets(String nameOfField, int minLength, int deepLevel, String value) {
        List<String> lstWords = Utils.cleanEx(value);
        System.out.println("Indexing: " + lstWords.toString());
        
        List<FacetField> fields = new ArrayList<FacetField>();
        if (lstWords != null) {
            for (int i=0; i< lstWords.size(); i++) {
                if (i+minLength < lstWords.size()) {
                    String startWord = StringUtils.join(lstWords.subList(i,i+minLength), " ");

                    List<String> inputWords = new ArrayList<String>();            
                    for (int level=0; level<=deepLevel; level++){
                        String word = startWord;
                        if (i + minLength + level + 1 < lstWords.size()) 
                            startWord += " " + lstWords.get(i + minLength + level + 1);

                        inputWords.add(word);
                        //inputWords.addAll( StringUtils.join(lstWords.subList(i+minLength, i+minLength+level), " "));                    
                    }

                    if (inputWords.size() > 0) {
                        String[] inputWordArray = new String[inputWords.size()];
                        inputWordArray = inputWords.toArray(inputWordArray);

                        fields.add(new FacetField(nameOfField, inputWordArray));
                    }
                }
            }        
        }
        
        return fields;
    }
    
    public static List<FacetField> getMultipleValueFacets(String nameOfField, int minLength, int maxLength, String value) {
        List<String> lstWords = Utils.cleanEx(value);        
        //System.out.println("Indexing: " + lstWords.toString());
        Set<String> recentWords = new HashSet<String>();
        
        List<FacetField> fields = new ArrayList<FacetField>();
        for (int i=0; i< lstWords.size(); i++) {
            if (i+minLength < lstWords.size()) {
                String startWord = StringUtils.join(lstWords.subList(i,i+minLength), " ");
                
                int deepLevel = maxLength - minLength;
                for (int level=0; level<=deepLevel; level++){
                    String word = startWord;
                    if (i + minLength + level + 1 < lstWords.size()) 
                        startWord += " " + lstWords.get(i + minLength + level + 1);
                    
                    if (word.length()> 0 && !word.isEmpty() && !BlockPhrases.contains(word) && !recentWords.contains(word)) {                        
                        fields.add(new FacetField(nameOfField, word));
                        recentWords.add(word);
                    }
                    //inputWords.addAll( StringUtils.join(lstWords.subList(i+minLength, i+minLength+level), " "))
                }
            }
        }
        
        return fields;
    }
}
