/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2b.indexer;

import assignment2b.indexer.model.DocSimilarity;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import se.constant.Constants;
import se.searcher.util.Utils;

/**
 *
 * @author giacatho
 */
public class A2bSimilarIndexBuilder {
	private IndexReader _reader;
	private IndexWriter _writer;
	
	
	public void buildIndex() throws IOException {
		initIndexWriter(Constants.INDEX_ASSIGNMENT2B_DIR, new StandardAnalyzer());

		_reader = DirectoryReader.open(FSDirectory.open(
				Paths.get(Constants.INDEX_ASSIGNMENT2B_DIR_TMP)));
		
		long startTS = System.currentTimeMillis();

		int numDocs = _reader.maxDoc();
		System.out.println("Process total documents " + numDocs);
		for (int i = 0; i < numDocs; i++) {
			
			List<DocSimilarity> docSims = new ArrayList();
			for (int j = 0; j < numDocs; j++) {
				if (j == i)
					continue;
				
				double cosSim = Utils.getCosineSimilarity(_reader, i, j);
				
				docSims.add(new DocSimilarity(j, cosSim));
			}
			
			Collections.sort(docSims, Collections.reverseOrder());
			
			addToIndex(i, docSims);
			if (i % 99 == 0) {
				long endTS = System.currentTimeMillis();
				System.out.println("Comlete 100, took " + (endTS - startTS)/1000 + " seconds");
			}
		}
		
		this.close();
    }

    private void initIndexWriter(String storePath, Analyzer analyzer) throws IOException {
        Path pathIndexStore = Paths.get(storePath);
        Directory indexStoreDir = FSDirectory.open(pathIndexStore);
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        _writer = new IndexWriter(indexStoreDir, iwc);
    }
	
	private void addToIndex(int docId, List<DocSimilarity> docSims) throws IOException {
		Document docToIndex = new Document();
		
		Document currDoc = _reader.document(docId);
		docToIndex.add(new StringField("pubyear", currDoc.get("pubyear"), Field.Store.YES));
		docToIndex.add(new StringField("pubvenue", currDoc.get("pubvenue"), Field.Store.YES));
		
		for (int i = 0; i < 10 && i < docSims.size(); i++) {
			DocSimilarity docSim = docSims.get(i);
			
			Document lSimDoc = _reader.document(docSim.getLuceneDocId());
			String simDoc = lSimDoc.get("pubvenue") + " " + lSimDoc.get("pubyear") + " - Similarity " + docSim.getCosSim();
			docToIndex.add(new StringField("simdoc", simDoc, Field.Store.YES));
		}
		
        _writer.addDocument(docToIndex);
    }
	
    private void close() throws IOException {
        this._writer.close();
    }
	
	public static void main(String[] args) throws IOException {
		new A2bSimilarIndexBuilder().buildIndex();
	}
	
}
