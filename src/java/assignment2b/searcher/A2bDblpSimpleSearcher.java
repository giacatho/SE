/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2b.searcher;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import se.constant.Constants;

/**
 *
 * @author nguyentritin
 */
public class A2bDblpSimpleSearcher {
	public static void main(String[] args) throws IOException {
		IndexReader reader = DirectoryReader.open(FSDirectory.open(
				Paths.get(Constants.INDEX_ASSIGNMENT2B_DIR)));
		IndexSearcher is = new IndexSearcher(reader);
		
		printDocumentInfo(reader, 1);
		printAllTerms(reader, 1, "title");
	}
	
	public static void printDocumentInfo(IndexReader reader, int luceneDocId) throws IOException {
		Document doc = reader.document(luceneDocId);
		
		List<IndexableField> allFields = doc.getFields();
		System.out.println("Start to print document: ");
		for (IndexableField field : allFields) {
			System.out.println(field.name() + ": " + doc.get(field.name()));
		}
	}
	
	public static void printAllTerms(IndexReader reader, int luceneDocId, String field) 
			throws IOException {
		Terms terms = reader.getTermVector(1, "title");
		
		TermsEnum termsEnum = terms.iterator();
		BytesRef text;
		System.out.println("Start to print terms: ");
		while((text = termsEnum.next()) != null) {
			System.out.print(text.utf8ToString() + ", ");
		}
	}
}
