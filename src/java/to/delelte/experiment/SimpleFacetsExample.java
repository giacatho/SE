package to.delelte.experiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.facet.DrillDownQuery;
import org.apache.lucene.facet.DrillSideways;
import org.apache.lucene.facet.DrillSideways.DrillSidewaysResult;
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.taxonomy.FastTaxonomyFacetCounts;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 *
 * @author giacatho
 */
public class SimpleFacetsExample {
	private final Directory indexDir = new RAMDirectory();
	private final Directory taxoDir = new RAMDirectory();
	private final FacetsConfig config = new FacetsConfig();
	
	public SimpleFacetsExample() {
		config.setHierarchical("Publish Date", true);
	}
	
	private void index() throws IOException {
		IndexWriter indexWriter = new IndexWriter(indexDir, new IndexWriterConfig(
				new WhitespaceAnalyzer()).setOpenMode(IndexWriterConfig.OpenMode.CREATE));
		
		// Write facet ords to a separate directory from the main index
		DirectoryTaxonomyWriter taxoWriter = new DirectoryTaxonomyWriter(taxoDir);
		
		Document doc = new Document();
		doc.add(new FacetField("Author", "Bob"));
		doc.add(new FacetField("Publish Date", "2010", "10", "15"));
		indexWriter.addDocument(config.build(taxoWriter, doc));
		
		doc = new Document();
		doc.add(new FacetField("Author", "Lisa"));
		doc.add(new FacetField("Publish Date", "2010", "10", "20"));
		indexWriter.addDocument(config.build(taxoWriter, doc));
		
		doc = new Document();
		doc.add(new FacetField("Author", "Lisa"));
		doc.add(new FacetField("Publish Date", "2012", "1", "1"));
		indexWriter.addDocument(config.build(taxoWriter, doc));
		
		doc = new Document();
		doc.add(new FacetField("Author", "Susan"));
		doc.add(new FacetField("Publish Date", "2012", "1", "7"));
		indexWriter.addDocument(config.build(taxoWriter, doc));
		
		doc = new Document();
		doc.add(new FacetField("Author", "Frank"));
		doc.add(new FacetField("Publish Date", "1999", "5", "5"));
		indexWriter.addDocument(config.build(taxoWriter, doc));
		
		indexWriter.close();
		taxoWriter.close();
	}
	
	private List<FacetResult> facetsOnly() throws IOException {
		DirectoryReader indexReader = DirectoryReader.open(indexDir);
		IndexSearcher searcher = new IndexSearcher(indexReader);
		TaxonomyReader taxoReader = new DirectoryTaxonomyReader(taxoDir);
		
		FacetsCollector fc = new FacetsCollector();
		
		searcher.search(new MatchAllDocsQuery(), fc);
		
		List<FacetResult> results = new ArrayList();
		
		Facets facets = new FastTaxonomyFacetCounts(taxoReader, config, fc);
		
		results.add(facets.getTopChildren(10, "Author"));
		results.add(facets.getTopChildren(10, "Publish Date"));
		
		indexReader.close();
		taxoReader.close();
		
		return results;
	}
	
	private List<FacetResult> facetsWithSearch() throws IOException {
		DirectoryReader indexReader = DirectoryReader.open(indexDir);
		IndexSearcher searcher = new IndexSearcher(indexReader);
		TaxonomyReader taxoReader = new DirectoryTaxonomyReader(taxoDir);
		
		FacetsCollector fc = new FacetsCollector();
		
		// MatchAllDocsQuery is for "browsing" (counts facets
		// for all non-deleted docs in the index); normally
		// you would use a "normal" query
		FacetsCollector.search(searcher, new MatchAllDocsQuery(), 10, fc);
		
		// Retrieve results
		List<FacetResult> results = new ArrayList();
		
		// Count both "Publish Date" and "Author" dimensions
		Facets facets = new FastTaxonomyFacetCounts(taxoReader, config, fc);
		results.add(facets.getTopChildren(10, "Author"));
		results.add(facets.getTopChildren(10, "Publish Date"));
		
		indexReader.close();
		taxoReader.close();
		
		return results;
	}
	
	private FacetResult drillDown() throws IOException {
		DirectoryReader indexReader = DirectoryReader.open(indexDir);
		IndexSearcher searcher = new IndexSearcher(indexReader);
		TaxonomyReader taxoReader = new DirectoryTaxonomyReader(taxoDir);
		
		// Passing no baseQuery means we drill down on 
		// all documents ("browse only")
		DrillDownQuery query = new DrillDownQuery(config);
		
		// Now user drills down on Publish Date /2010
		query.add("Publish Date", "2010");
		FacetsCollector fc = new FacetsCollector();
		FacetsCollector.search(searcher, query, 10, fc);
		
		Facets facets = new FastTaxonomyFacetCounts(taxoReader, config, fc);
		FacetResult result = facets.getTopChildren(10, "Author");
		
		indexReader.close();
		taxoReader.close();
		
		return result;
	}
	
	private List<FacetResult> drillSideways() throws IOException {
		DirectoryReader indexReader = DirectoryReader.open(indexDir);
		IndexSearcher searcher = new IndexSearcher(indexReader);
		TaxonomyReader taxoReader = new DirectoryTaxonomyReader(taxoDir);
		
		// Passing no baseQuery means we drill down on all
		// documents (browse only)
		DrillDownQuery query = new DrillDownQuery(config);
		
		// Now user drills down on Publish DAte /2010
		query.add("Publish Date", "2010");
		
		DrillSideways ds = new DrillSideways(searcher, config, taxoReader);
		DrillSidewaysResult result = ds.search(query, 10);
		
		// Retrieve results
		List<FacetResult> facets = result.facets.getAllDims(10);

		indexReader.close();
		taxoReader.close();
		
		return facets;
	}
	
	public List<FacetResult> runFacetOnly() throws IOException {
		index();
		return facetsOnly();
	}
	
	public List<FacetResult> runSearch() throws IOException {
		index();
		return facetsWithSearch();
	}
	
	public FacetResult runDrillDown() throws IOException {
		index();
		return drillDown();
	}
	
	public List<FacetResult> runDrillSideways() throws IOException {
		index();
		return drillSideways();
	}
	
	public static void main(String[] args) throws Exception {
		SimpleFacetsExample example = new SimpleFacetsExample();
		
//		System.out.println("Facet counting example:");
//		System.out.println("-----------------------");
//		List<FacetResult> results1 = example.runFacetOnly();
//		System.out.println("Author: " + results1.get(0));
//		System.out.println("Publish Date: " + results1.get(1));
//		
//		System.out.println("Facet counting example (combined facets and search):");
//		System.out.println("----------------------");
//		List<FacetResult> results2 = example.runSearch();
//		System.out.println("Author: " + results2.get(0));
//		System.out.println("Publish Date: " + results2.get(1));
//		
//		System.out.println("Facet drill-down example (Publish Date /2010:");
//		System.out.println("---------------------------------------------");
//		System.out.println("Author: " + example.runDrillDown());
//		
		System.out.println("Facet drill-sideways example (Publish Date /2010:)");
		System.out.println("------------------------------------------------");
		for (FacetResult result : example.runDrillSideways()) {
			System.out.println(result);
		}
	}
}


