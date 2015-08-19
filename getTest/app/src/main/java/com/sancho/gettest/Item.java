package com.sancho.gettest;

/**
 * Created by Sancho on 18.08.2015.
 */
import java.util.ArrayList;

public interface Item {
    public String getTitle();
    public ArrayList<Item> getChilds();
}
