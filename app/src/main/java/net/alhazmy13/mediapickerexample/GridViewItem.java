package net.alhazmy13.mediapickerexample;

import android.graphics.Bitmap;

/**
 * Created by subash.b on 11/13/2017.
 */

public class GridViewItem {

    private String path;
    private boolean isDirectory;
    private Bitmap image;


    public GridViewItem(String path, boolean isDirectory, Bitmap image) {
        this.path = path;
        this.isDirectory = isDirectory;
        this.image = image;
    }


    public String getPath() {
        return path;
    }


    public boolean isDirectory() {
        return isDirectory;
    }


    public Bitmap getImage() {
        return image;
    }
}
