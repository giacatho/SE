/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sky.searcher;

import assignment1.searcher.model.Field;
import assignment1.searcher.model.Operator;
import assignment1.searcher.model.SearchInput;
import common.Constants;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.facet.DrillDownQuery;
import org.apache.lucene.facet.DrillSideways;
import org.apache.lucene.facet.DrillSideways.DrillSidewaysResult;
import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.LabelAndValue;
import org.apache.lucene.facet.taxonomy.FacetLabel;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;
import sky.util.FacetUtils;
import sky.util.FieldNaming;

/**
 *
 * @author cnaquoc
 */
public class FacetSearcher {
    public static void main(String[] args) throws IOException, ParseException {
        long startTime = System.currentTimeMillis();        
        
        DirectoryReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(Constants.INDEX_FOLDER)));
        
        IndexSearcher searcher = new IndexSearcher(indexReader);
        TaxonomyReader taxoReader = new DirectoryTaxonomyReader(FSDirectory.open(Paths.get(Constants.TAXO_FOLDER)));
        FacetsConfig config = FacetUtils.getConfig();        
        
        List<SearchInput> inputs = new ArrayList<SearchInput>();        
        inputs.add(new SearchInput("life", Operator.AND, Field.Title));
        
        TermSearcher termSearcher = new TermSearcher();
        Query termQuery = termSearcher.getQuery(inputs);        
        
        for (int i=2010; i < 2016; i++) {
        //DrillDownQuery q = new DrillDownQuery(config, termQuery);
        DrillDownQuery q = new DrillDownQuery(config);
        //q.add(FieldNaming.Facet.Venue, "INFOCOM");
        //q.add(FieldNaming.Facet.Year, "2010");
        q.add(FieldNaming.Facet.Year, i + "");        
        
        //drilldown side
        DrillSideways ds = new DrillSideways(searcher, config, taxoReader);
        DrillSidewaysResult result = ds.search(q, 100);        
        
//        List<FacetResult> facets = result.facets.getAllDims(10);//getTopChildren(10, FieldNaming.Facet.Title);
//        for (FacetResult facet : facets) {
//            System.out.println("Facet: " + facet.dim + " - child: " + facet.childCount + " - path:" + facet.path);
//            for (LabelAndValue item : facet.labelValues) {                
//                System.out.println("Label: " + item.label + "/" + item.value);
//            }
//        }
        
        int total = 0;
        FacetResult facet = result.facets.getTopChildren(5000, FieldNaming.Facet.Title);
        System.out.println("Year: " + i);
        for (LabelAndValue item : facet.labelValues) {
            if (item.label.contains(" ")) {
                System.out.println("Label: " + item.label + "/" + item.value);
            
            //System.out.println("Label: " + item.label + "/" + item.value);
                if (total == 10) break;
                total++;
            }
//            FacetResult subFacet = result.facets.getTopChildren(3, FieldNaming.Facet.Title, item.label);
//            if (subFacet != null) {
//                for (LabelAndValue subItem : subFacet.labelValues) {
//                    System.out.println("Sub: " + subItem.label + "/" + subItem.value);
//                }
//            }
        }
//        TopDocs hits = result.hits;
//        for (ScoreDoc scoreDoc : hits.scoreDocs) {
//            Document doc = searcher.doc(scoreDoc.doc);
//
//            System.out.println("Score: " + scoreDoc.score + 
//                               " - Title: " + doc.get("title") +
//                               " - " + doc.get("pubvenue") + doc.get("pubyear"));                            
//        }
        }
        
        indexReader.close();
        taxoReader.close();
        
        long endTime = System.currentTimeMillis();
        long second = (endTime - startTime)/1000;
        System.out.println("Run time: " + second + "s " + (endTime - startTime)%1000);
    }
}

// facet only
//        FacetsCollector fc = new FacetsCollector();
//        FacetsCollector.search(searcher, new MatchAllDocsQuery(), 10, fc);
//        //FacetsCollector.search(searcher, q, 10, fc);
//
//        // Retrieve results
//        List<FacetResult> results = new ArrayList<FacetResult>();
//
//        //Facets author = new FastTaxonomyFacetCounts(FacetUtils.getVenueYearField(), taxoReader, config, fc);
//        Facets facets = new FastTaxonomyFacetCounts(taxoReader, config, fc);
//        //results.add(facets.getTopChildren(10, FacetUtils.getVenueYearField()));
//        results.add(facets.getTopChildren(10, FacetUtils.getVenueField()));
//        results.add(facets.getTopChildren(10, FacetUtils.getYearField()));
//        
//        System.out.println("Result :" + results.size() + " items");
//        for (FacetResult result : results) {
//            System.out.println(result.path + "-" + result.value);
//            //result.
//        }
        
//        List<FacetResult> facets = result.facets.getTopChildren(10, FacetUtils.getTitleField());//result.facets.getAllDims(10);
//        for (FacetResult facet :facets) {
//            for (LabelAndValue item : facet.labelValues) {
//                System.out.println("Facet: " + item.label + "/" + item.value);  
//            }
//        }