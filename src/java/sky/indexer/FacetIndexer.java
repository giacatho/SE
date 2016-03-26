/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sky.indexer;

import sky.util.FacetUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.taxonomy.TaxonomyWriter;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import sky.util.FieldNaming;

/**
 *
 * @author cnaquoc
 */
public class FacetIndexer implements Indexer{
    
    IndexWriter iw;
    TaxonomyWriter taxo;
    FacetsConfig conf;
            
    public FacetIndexer(String indexDir, String taxoDir, Analyzer analyzer) throws IOException {
        
        // create and open an index writer
        iw = getIndexWriter(indexDir, analyzer);
                
        // create and open a taxonomy writer
        taxo = getTaxonomyWriter(taxoDir);
        
        //create config
        conf = FacetUtils.getConfig();
    }    
    
    @Override
    public void index(DBLPItem item) throws IOException  {
        Document doc = getDocument(item);
                
        iw.addDocument(conf.build(taxo, doc));
    }
    
    @Override
    public void Finish() throws IOException {         
        taxo.commit();
        iw.commit();

        taxo.close();
        iw.close();
    }
    
    private IndexWriter getIndexWriter(String indexDir, Analyzer analyzer) throws IOException {
        Path pathIndexStore = Paths.get(indexDir);
        Directory indexStoreDir = FSDirectory.open(pathIndexStore);
        
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        
        return new IndexWriter(indexStoreDir, iwc);   
    }
    
    private TaxonomyWriter getTaxonomyWriter(String taxoDir) throws IOException {
        Path pathIndexStore = Paths.get(taxoDir);
        Directory indexStoreDir = FSDirectory.open(pathIndexStore);
        
        return new DirectoryTaxonomyWriter(indexStoreDir, OpenMode.CREATE);
    }
    
    private Document getDocument(DBLPItem item) {
        Document doc = new Document();        
        
        String year = item.getPubyear();
        if (year == null || year.isEmpty()) year = "unknown year";
        String venue = item.getPubvenue();
        if (venue == null || venue.isEmpty()) venue = "unknown venue";
        		
        doc.add(new TextField("title", item.getTitle(), Field.Store.YES));
        for(String author : item.getAuthors()){
            doc.add(new TextField("author", author, Field.Store.YES));
        }
        doc.add(new StringField(FieldNaming.Term.Year, year, Field.Store.YES));
        doc.add(new StringField(FieldNaming.Term.Venue, venue, Field.Store.YES));
        
        doc.add(new FacetField(FieldNaming.Facet.Year, year));
        doc.add(new FacetField(FieldNaming.Facet.Venue,venue));
        //doc.add(new FacetField(FacetUtils.getVenueYearField(), venue, year));   

        //List<FacetField> titleFields = FacetUtils.getHierarchicalFacets(FieldNaming.Facet.Title, 1, 2, item.getTitle());
        List<FacetField> titleFields = FacetUtils.getMultipleValueFacets(FieldNaming.Facet.Title, 1, 2, item.getTitle());
        for (FacetField field : titleFields) {
            doc.add(field);
        }
        
//        List<FacetField> titleFields = FacetUtils.getMultipleValueFacets(FieldNaming.Facet.Title1, 1, 1, item.getTitle());
//        for(FacetField field : titleFields) {
//            doc.add(field);
//        }
//        titleFields = FacetUtils.getMultipleValueFacets(FieldNaming.Facet.Title2, 2, 2, item.getTitle());
//        for(FacetField field : titleFields) {
//            doc.add(field);
//        }
//        titleFields = FacetUtils.getMultipleValueFacets(FieldNaming.Facet.Title3, 3, 3, item.getTitle());
//        for(FacetField field : titleFields) {
//            doc.add(field);
//        }
//        titleFields = FacetUtils.getMultipleValueFacets(FieldNaming.Facet.Title4, 4, 4, item.getTitle());
//        for(FacetField field : titleFields) {
//            doc.add(field);
//        }
        
        return doc;
    }
}

