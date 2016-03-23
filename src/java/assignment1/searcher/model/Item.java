/* 
    Document   : Item
    Created on : 03-Mar-2016, 07:51:14
    Author     : Quoc
*/

package assignment1.searcher.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Item {
    private int value;
    private String name;
    
    public Item() {        
    }   
    
    public Item(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public <T extends Enum<T>> T convert(Class<T> enumClass) {
        return enumClass.getEnumConstants()[value];
    }
    
    public static <T extends Enum<T>> List<Item> asItems(Class<T> enumClass) {
        List<Item> items = new ArrayList<Item>();
        for (T f : enumClass.getEnumConstants()) {
            items.add(new Item(f.ordinal(), f.toString()));                        
        }
        
        return items;
    }
}