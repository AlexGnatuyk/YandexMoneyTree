package com.sancho.gettest;

/**
 * Created by Sancho on 18.08.2015.
 */

import java.io.File;
import java.util.ArrayList;

public class FileItem implements Item {

    File file;
    ArrayList<Item> childs;

    public FileItem (File f) {
        file = f;
    }

    @Override
    public String getTitle() {
        return file.getName();
    }

    @Override
    public ArrayList<Item> getChilds() {
        if (childs != null)
            return childs;

        childs = new ArrayList<Item>();

        File[] files = file.listFiles();

        if (files != null) {
            for (File f : files)
                childs.add(new FileItem(f));
        }

        return childs;
    }

    private int getCountChilds() {

        if (childs != null)
            return childs.size();

        File[] files = file.listFiles();
        if (files == null)
            return 0;
        return files.length;
    }
}

