/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.searcher.model;

import java.util.List;

/**
 *
 * @author nguyentritin
 */
public class SearchResultItem {
	private final float searchScore;
	private final String docKey;
	private final String docTitle;
	private final List<String> docAuthors;

	public SearchResultItem(float searchScore, String docKey, String docTitle, List<String> docAuthors) {
		this.searchScore = searchScore;
		this.docKey = docKey;
		this.docTitle = docTitle;
		this.docAuthors = docAuthors;
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

	public List<String> getDocAuthors() {
		return docAuthors;
	}
}
