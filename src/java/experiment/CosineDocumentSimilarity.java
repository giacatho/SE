package experiment;

import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;

public class CosineDocumentSimilarity {

    public static final String CONTENT = "Content";

    private final Set<String> terms = new HashSet();
    private final RealVector v1;
    private final RealVector v2;

    CosineDocumentSimilarity(String s1, String s2) throws IOException {
        Directory directory = createIndex(s1, s2);
        IndexReader reader = DirectoryReader.open(directory);
        Map<String, Integer> f1 = getTermFrequencies(reader, 0);
        Map<String, Integer> f2 = getTermFrequencies(reader, 1);
        reader.close();
        v1 = toRealVector(f1);
        v2 = toRealVector(f2);
    }

    Directory createIndex(String s1, String s2) throws IOException {
        Directory directory = new RAMDirectory();
        Analyzer analyzer = new SimpleAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(directory, iwc);
        addDocument(writer, s1);
        addDocument(writer, s2);
        writer.close();
        return directory;
    }

    void addDocument(IndexWriter writer, String content) throws IOException {
        Document doc = new Document();
		
		FieldType myFieldType = new FieldType(TextField.TYPE_STORED);
		myFieldType.setStoreTermVectors(true);
		
        doc.add(new Field(CONTENT, content, myFieldType));
        writer.addDocument(doc);
    }
	
	

    double getCosineSimilarity() {
        return (v1.dotProduct(v2)) / (v1.getNorm() * v2.getNorm());
    }

    Map<String, Integer> getTermFrequencies(IndexReader reader, int docId)
            throws IOException {
        Terms vector = reader.getTermVector(docId, CONTENT);
		if (vector == null) 
			return null;
		
        TermsEnum termsEnum = vector.iterator();
		
        Map<String, Integer> frequencies = new HashMap();
        BytesRef text;
        while ((text = termsEnum.next()) != null) {
            String term = text.utf8ToString();
            int freq = (int) termsEnum.totalTermFreq();
            frequencies.put(term, freq);
            terms.add(term);
        }
        return frequencies;
    }

    RealVector toRealVector(Map<String, Integer> map) {
        RealVector vector = new ArrayRealVector(terms.size());
        int i = 0;
        for (String term : terms) {
            int value = map.containsKey(term) ? map.get(term) : 0;
            vector.setEntry(i++, value);
        }
        return (RealVector) vector.mapDivide(vector.getL1Norm());
    }
	
	public static double getCosineSimilarity(String s1, String s2)
            throws IOException {
        return new CosineDocumentSimilarity(s1, s2).getCosineSimilarity();
    }
	
	public static void main(String []args) throws IOException {
		String s1 = "My name is Tin. How are you?";
		String s2 = "My name is Tin. How are you?";
		
		System.out.println(getCosineSimilarity(s1, s2));
	}

	private Exception RuntimeException(String can_not_get_the_TermVector) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}