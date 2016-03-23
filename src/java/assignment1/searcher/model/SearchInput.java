/* 
    Document   : SearchInput
    Created on : 03-Mar-2016, 17:21:30
    Author     : Quoc
*/

package assignment1.searcher.model;


public class SearchInput {
    private String key;
    private Operator operator;
    private Field field;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }    
}
