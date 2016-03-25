/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.util.BytesRef;
import assignment1.searcher.model.SearchInput;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author nguyentritin
 */
public class Utils {
	public static IndexWriter initIndexWriter(String storePath, Analyzer analyzer) throws IOException {
        Path pathIndexStore = Paths.get(storePath);
        Directory indexStoreDir = FSDirectory.open(pathIndexStore);
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        return new IndexWriter(indexStoreDir, iwc);
    }
	
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
	
	/**
	 * Compute the cosine similarity of two documents, given their Lucene document ids.
	 * @param reader
	 * @param docId1
	 * @param docId2
	 * @return
	 * @throws IOException 
	 */
	public static double getCosineSimilarity(IndexReader reader, int docId1, int docId2)
            throws IOException {
		Set<String> allTerms = new HashSet();
		
		Map<String, Integer> termFrequencies1 = getTermFrequencies(reader, docId1, false, allTerms);
		Map<String, Integer> termFrequencies2 = getTermFrequencies(reader, docId2, false, allTerms);
		
		// Called only after term frequencies have been call
		RealVector v1 = toRealVector(termFrequencies1, allTerms);
		RealVector v2 = toRealVector(termFrequencies2, allTerms);
		
		return (v1.dotProduct(v2)) / (v1.getNorm() * v2.getNorm());
    }
	
	/**
	 * Retrieve the term frequencies map of a Lucene document, given its document id.<br/>
	 * Terms which is not found in allTerms set before will be added to the set
	 * @param reader
	 * @param docId
	 * @param mustBeBiword
	 * @param allTerms
	 * @return
	 * @throws IOException 
	 */
	public static Map<String, Integer> getTermFrequencies(IndexReader reader, int docId, 
			boolean mustBeBiword, Set<String> allTerms) throws IOException {
        Map<String, Integer> termFrequencies = new HashMap();
		
		Terms vector = reader.getTermVector(docId, "title");
		if (vector == null) 
			return null;
		
        TermsEnum termsEnum = vector.iterator();
		
        BytesRef text;
        while ((text = termsEnum.next()) != null) {
            String term = text.utf8ToString();
            int freq = (int) termsEnum.totalTermFreq();
			
			if (term.contains("_")) {
				continue;
			}
			
			if (mustBeBiword && term.split(" ").length !=2) {
				continue;
			}
			
            termFrequencies.put(term, freq);
			if (allTerms != null)
				allTerms.add(term);
        }
		
        return termFrequencies;
    }
	
	public static List<Entry<String, Integer>> getTopTermFrequencies(IndexReader reader,
			ScoreDoc[] scoreDocs, boolean mustBeBiword) throws IOException {
		Map<String, Integer> termFrequencies = Utils.getAllTermFrequencies(reader, scoreDocs, mustBeBiword);
		
		List<Entry<String, Integer>> sortedTermFrequencies = Utils.getEntriesSortedByValues(termFrequencies);
		
		List<Entry<String, Integer>> topTerms = new ArrayList();
		for (int i=0; i < 10 && i < sortedTermFrequencies.size(); i++) {
			topTerms.add(sortedTermFrequencies.get(i));
		}
		
		return topTerms;
	}
	
	private static Map<String, Integer> getAllTermFrequencies(IndexReader reader, 
			ScoreDoc[] scoreDocs, boolean mustBeBiword) throws IOException {
		Map<String, Integer> termFrequencies = new HashMap();
		
		for (ScoreDoc scoreDoc: scoreDocs) {
			Terms vector = reader.getTermVector(scoreDoc.doc, "title");
			if (vector == null) {
				// Some title is funny, just "@", so no term vector, we just igonre
				// System.out.println("No term vector for title field for document " + scoreDoc.doc + ", skipped");
				continue;
			}

			TermsEnum termsEnum = vector.iterator();

			BytesRef text;
			while ((text = termsEnum.next()) != null) {
				String term = text.utf8ToString();
				int freq = (int) termsEnum.totalTermFreq();

				if (term.contains("_")) {
					continue;
				}

				if (mustBeBiword && term.split(" ").length !=2) {
					continue;
				}

				if (termFrequencies.containsKey(term)) {
					termFrequencies.put(term, termFrequencies.get(term) + freq);
				} else {
					termFrequencies.put(term, freq);
				}
			}
		}
		
		return termFrequencies;
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
	
	// http://stackoverflow.com/a/11648106
	public static <K, V extends Comparable<? super V>> List<Entry<K, V>> getEntriesSortedByValues(
			Map<K,V> map) {
		List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());
		Collections.sort(sortedEntries,
			new Comparator<Entry<K,V>>() {
				@Override
				public int compare(Entry<K,V> e1, Entry<K,V>e2) {
					return e2.getValue().compareTo(e1.getValue());
				}
			}
		);
		
		return sortedEntries;
	}
	
	public static void printTopTerms(List<Entry<String, Integer>> topTerms) {
		for (Entry<String, Integer> term: topTerms) {
			System.out.println("Term: " + term.getKey() + ". Frequencies: " + term.getValue());
		}
	}
}
