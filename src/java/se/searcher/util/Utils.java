/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.searcher.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.util.BytesRef;
import se.searcher.model.SearchInput;

/**
 *
 * @author nguyentritin
 */
public class Utils {
	public static List<String> getKeywords (List<SearchInput> inputs) {
		List<String> keyWords = new ArrayList();
        for (SearchInput input : inputs) {
            String[] split = input.getKey().replaceAll("\"","").split(" ");
            for (String key : split) {
                keyWords.add(key);
            }
        }
		
		return keyWords;
	}
	
	public static void printAllTerms(IndexSearcher searcher, int luceneDocId, String field) 
			throws IOException {
		Terms terms = searcher.getIndexReader().getTermVector(luceneDocId, field);
		
		TermsEnum termsEnum = terms.iterator();
		BytesRef term;
		System.out.println("Start to print terms: ");
		while((term = termsEnum.next()) != null) {
			// enumerate through documents, in this case only one
			DocsEnum docsEnum = termsEnum.docs(null, null); 
			int docIdEnum;
			while ((docIdEnum = docsEnum.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
				// get the term frequency in the document 
				System.out.println(term.utf8ToString()+ " " + docIdEnum + " " + docsEnum.freq()); 
			}
		}
		System.out.println();
	}
	
	public static void printDocumentInfo(IndexSearcher searcher, int luceneDocId) throws IOException {
		Document doc = searcher.doc(luceneDocId);
		
		List<IndexableField> allFields = doc.getFields();
		//System.out.println("Start to print document: ");
		for (IndexableField field : allFields) {
			System.out.println(field.name() + ": " + doc.get(field.name()));
		}
	}
	
	public static void printDocumentYearAndVenue(IndexSearcher searcher, int luceDocId) throws IOException {
		Document doc = searcher.doc(luceDocId);
		
		System.out.println("Year: " + doc.get("pubyear") + ". Venue " + doc.get("pubvenue") + ".");
	}
	
	public static double getCosineSimilarity(IndexReader reader, int docId1, int docId2)
            throws IOException {
		Set<String> allTerms = new HashSet();
		
		Map<String, Integer> termFrequencies1 = getTermFrequencies(reader, docId1, allTerms);
		Map<String, Integer> termFrequencies2 = getTermFrequencies(reader, docId2, allTerms);
		
		// Called only after term frequencies have been call
		RealVector v1 = toRealVector(termFrequencies1, allTerms);
		RealVector v2 = toRealVector(termFrequencies2, allTerms);
		
		return (v1.dotProduct(v2)) / (v1.getNorm() * v2.getNorm());
    }
	
	public static Map<String, Integer> getTermFrequencies(IndexReader reader, int docId, Set<String> allTerms)
            throws IOException {
        Terms vector = reader.getTermVector(docId, "title");
		if (vector == null) 
			return null;
		
        TermsEnum termsEnum = vector.iterator();
		
        Map<String, Integer> frequencies = new HashMap();
        BytesRef text;
        while ((text = termsEnum.next()) != null) {
            String term = text.utf8ToString();
            int freq = (int) termsEnum.totalTermFreq();
            frequencies.put(term, freq);
			allTerms.add(term);
        }
        return frequencies;
    }
	
	private static RealVector toRealVector(Map<String, Integer> map, Set<String> allTerms) {
        RealVector vector = new ArrayRealVector(allTerms.size());
        int i = 0;
        for (String term : allTerms) {
            int value = map.containsKey(term) ? map.get(term) : 0;
            vector.setEntry(i++, value);
        }
        return (RealVector) vector.mapDivide(vector.getL1Norm());
    }
	
}
