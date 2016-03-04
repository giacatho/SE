/* 
    Document   : SearchOperator
    Created on : 03-Mar-2016, 17:23:00
    Author     : Quoc
*/

package se.searcher.gui;

import com.google.gson.annotations.SerializedName;


public enum Operator {
    @SerializedName("0")
    AND,
    @SerializedName("1")
    OR,
    @SerializedName("2")
    NOT
}
