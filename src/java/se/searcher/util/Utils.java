/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.searcher.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DocsEnum;
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
	
}
