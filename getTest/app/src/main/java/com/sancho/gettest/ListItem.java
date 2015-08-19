package com.sancho.gettest;

/**
 * Created by Sancho on 18.08.2015.
 */


        import java.util.ArrayList;

public class ListItem implements Item {

    private String title;
    private ArrayList<Item> childs;

    public ListItem (String title) {
        this.title = title;
        childs = new ArrayList<Item>();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public ArrayList<Item> getChilds() {
        return childs;
    }




    public void addChild (Item item) {
        childs.add(item);
    }
}

