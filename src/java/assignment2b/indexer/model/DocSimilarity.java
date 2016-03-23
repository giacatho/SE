package assignment2b.indexer.model;

/**
 *
 * @author giacatho
 */
public class DocSimilarity implements Comparable<DocSimilarity> {
	int luceneDocId;
	double cosSim;

	public DocSimilarity(int luceneDocId, double cosSim) {
		this.luceneDocId = luceneDocId;
		this.cosSim = cosSim;
	}

	public int getLuceneDocId() {
		return luceneDocId;
	}

	public double getCosSim() {
		return cosSim;
	}
	
	@Override
	public int compareTo(DocSimilarity o) {
		if (this.cosSim > o.cosSim)
			return 1;

		if (this.cosSim < o.cosSim)
			return -1;

		return 0;
	}
	
	@Override
	public String toString() {
		return "DocID: " + this.luceneDocId + "; Cosine Similarity: " + this.cosSim + "\n";
	}
}
