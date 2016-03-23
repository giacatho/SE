package assignment1.searcher.model;

import java.util.List;

/**
 *
 * @author nguyentritin
 */
public class SearchResultItem {
	private final List<String> keywords;
	
	private final float searchScore;
	private final String docKey;
	private final String docTitle;
	private final List<String> docAuthors;
	private final String docPubYear;
	private final String docPubVenue;

	public SearchResultItem(List<String> keywords,
			float searchScore, String docKey, String docTitle, 
			List<String> docAuthors, String docPubYear, String docPubVenue) {
		this.keywords = keywords;
		
		this.searchScore = searchScore;
		this.docKey = docKey;
		this.docTitle = docTitle;
		this.docAuthors = docAuthors;
		this.docPubYear = docPubYear;
		this.docPubVenue = docPubVenue;
	}
	
	public float getSearchScore() {
		return searchScore;
	}

	public String getDocKey() {
		return docKey;
	}

	public String getDocTitle() {
		return docTitle;
	}
	
	public String getDocTitleBeauty() {
		return this.highlight(this.docTitle);
	}

	public List<String> getDocAuthors() {
		return docAuthors;
	}
	
	public String getDocAuthorsBeauty() {
		String strRet = "";
		int lastIndex = this.docAuthors.size() - 1;
		
		for (int i = 0; i < lastIndex; i++) {
			strRet += this.docAuthors.get(i) + ", ";
		}
		strRet += this.docAuthors.get(lastIndex);
		
		return this.highlight(strRet);
	}
	
	public String getDocPubYear() {
		return docPubYear;
	}

	public String getDocPubVenue() {
		return docPubVenue;
	}
	
	/**
	 * High light the input string with keyword
	 * @param str
	 * @return 
	 */
	private String highlight(String str) {
		String retStr = str;
		
		for (String keyword : keywords) {
			retStr = retStr.replaceAll("(?i)(" + keyword + ")", "<em>$1</em>");
		}
		
		return retStr;
	}
}
