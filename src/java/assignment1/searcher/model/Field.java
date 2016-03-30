/* 
    Document   : SearchField
    Created on : 03-Mar-2016, 07:47:22
    Author     : Quoc
*/

package assignment1.searcher.model;

import com.google.gson.annotations.SerializedName;


public enum Field {
    @SerializedName("0")
    Title,
    
    @SerializedName("1")
    Author,
	
	@SerializedName("2")
	Pubvenue,
	
	@SerializedName("3")
	Pubyear
}
