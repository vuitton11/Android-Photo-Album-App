package view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import model.Photo;

public class ThumbnailView {

    private Context context;
    private ArrayList<Photo> images;

    public ThumbnailView(Context con, ArrayList<Photo> images){
        this.context = context;
        this.images = images;
    }


    public View getItem(int x){
        return null;
    }

}
