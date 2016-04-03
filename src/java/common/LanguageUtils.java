/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cnaquoc
 */
public class LanguageUtils {

    static List<String> getNounPhrases(Sentence sent) {
        List<String> results = new ArrayList<String>();
        
        int iCheck = sent.words().size() - 1;
        while (iCheck >= 0) {            
            if (sent.posTag(iCheck).startsWith("NN")) {
                String phrase = sent.lemma(iCheck);
                
                int iRelation = iCheck-1;
                while (iRelation >= 0 && (sent.posTag(iRelation).startsWith("NN") || sent.posTag(iRelation).startsWith("JJ"))) { //if still related with check word
                    
                    phrase = sent.lemma(iRelation) + " " + phrase;
                    results.add(phrase);
                    iRelation--;
                }
                
                if (iRelation == iCheck-1) results.add(phrase);
                
                iCheck = iRelation+1;
            }
            iCheck--;
        }
        
        return results;
    } 
  
    
    public static List<String> getNounPhrases(String title) {
        List<String> results = new ArrayList<String>();
                
        Document doc = new Document(title.toLowerCase());
        for (Sentence sent : doc.sentences()) {            
            for (String phrase : getNounPhrases(sent)) {
                if (!results.contains(phrase))
                    results.add(phrase);
            }
        }
        
        return results;
    }
    
    static List<String> getNounPhrasesForWeb(Sentence sent) {
        List<String> results = new ArrayList<String>();
        
        int iCheck = sent.words().size() - 1;
        while (iCheck >= 0) {            
            if (sent.posTag(iCheck).startsWith("NN")) {
                String phrase = sent.word(iCheck);
                
                int iRelation = iCheck-1;
                while (iRelation >= 0 && (sent.posTag(iRelation).startsWith("NN") || sent.posTag(iRelation).startsWith("JJ"))) { //if still related with check word
                    
                    phrase = sent.word(iRelation) + " " + phrase;
                    //results.add(phrase);
                    iRelation--;
                }
                
                //if (iRelation == iCheck-1) results.add(phrase);
                results.add(phrase);
                
                iCheck = iRelation+1;
            }
            iCheck--;
        }
        
        return results;
    } 
  
    
    public static List<String> getNounPhrasesForWeb(String title) {
        List<String> results = new ArrayList<String>();
                
        Document doc = new Document(title.toLowerCase());
        for (Sentence sent : doc.sentences()) {            
            for (String phrase : getNounPhrasesForWeb(sent)) {
                if (!results.contains(phrase))
                    results.add(phrase);
            }
        }
        
        return results;
    }
}

       
//    static List<String> getNounPhrases(Sentence sent) {
//        List<String> results = new ArrayList<String>();
//        SortedSet<Integer> DependencyIndexes = new TreeSet<Integer>();
//        
//        for (int i=0; i < sent.words().size(); i++) {
//            if (!DependencyIndexes.contains(i) && (sent.posTag(i).startsWith("NN") || sent.posTag(i).startsWith("JJ"))) {
//                Integer pointToIndex = sent.governor(i).get();
//                if (pointToIndex != -1) {
//                    if (sent.posTag(pointToIndex).startsWith("NN") && !sent.incomingDependencyLabel(i).get().equals("conj")) {                        
//                        DependencyIndexes.add(i);
//                        if (!DependencyIndexes.contains(pointToIndex))
//                            DependencyIndexes.add(pointToIndex);
//                        
//                        if (pointToIndex - i == 1) {
//                            results.add(sent.lemma(i) + " " + sent.lemma(pointToIndex));
//                        }
//                    }
//                }
//            }
//        }
//        
//        
//        List<Integer> ListDependencyIndexes = new ArrayList<Integer>(DependencyIndexes);                
//        int startIndex, endIndex;
//        for (int i=0; i<ListDependencyIndexes.size(); i++) {
//            startIndex = endIndex = ListDependencyIndexes.get(i);
//            for (int j=i+1; j<ListDependencyIndexes.size(); j++) {
//                int newIndex = ListDependencyIndexes.get(j);
//                if (newIndex - endIndex == 1) 
//                    endIndex = newIndex;
//                else
//                    break; 
//            }
//            
//            String phrase = "";
//            for (int k=startIndex; k<=endIndex; k++) {
//                phrase += sent.lemma(k) + " ";
//            }
//            phrase = phrase.trim();
//            if (!results.contains(phrase)) results.add(phrase);
//            
//            i += (endIndex - startIndex);
//        }
//        
//        return results;
//    }
    
  
//    static List<String> getNounPhrases(Sentence sent) {
//        System.out.println(sent.posTags().toString());
//        
//        List<String> results = new ArrayList<String>();
//        
//        int iCheck = sent.words().size() - 1;
//        while (iCheck >= 0) {            
//            if (sent.posTag(iCheck).startsWith("NN")) {
//                String phrase = sent.lemma(iCheck);
//                
//                int iRelation = iCheck-1;
//                while (iRelation >= 0 && sent.governor(iRelation).get() == iCheck) { //if still related with check word
//                    //if (!(sent.posTag(iRelation).startsWith("NN") || sent.posTag(iRelation).startsWith("JJ"))) break;
//                    
//                    phrase = sent.lemma(iRelation) + " " + phrase;
//                    results.add(phrase);
//                    iRelation--;
//                }
//                
//                if (iRelation == iCheck-1) results.add(phrase);
//                
//                iCheck = iRelation+1;
//            }
//            iCheck--;
//        }
//        
//        return results;
//    } 