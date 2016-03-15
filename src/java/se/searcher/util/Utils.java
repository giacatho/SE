/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.searcher.util;

import java.util.ArrayList;
import java.util.List;
import se.searcher.model.SearchInput;

/**
 *
 * @author nguyentritin
 */
public class Utils {
	public static List<String> getKeywords (List<SearchInput> inputs) {
		List<String> keyWords = new ArrayList();
        for (SearchInput input : inputs) {
            String[] split = input.getKey().split(" ");
            for (String key : split) {
                keyWords.add(key);
            }
        }
		
		return keyWords;
	}
}
