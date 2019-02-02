package com.example.ryans.androidphotos;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import model.Album;
import model.Photo;
import model.Tag;
import model.User;
import model.UserData;

public class PhotoActivity extends AppCompatActivity {

    UserData uData = new UserData();
    static User USER;
    Album ActiveAlbum;
    Photo ActivePhoto;

    ListView tagList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.image_view_layout);
            tagList = findViewById(R.id.list_tags);



            USER = uData.getUser();
            ActiveAlbum = uData.getActiveAlbum();
            ActivePhoto = uData.getActivePhoto();

            setTitle(ActiveAlbum.getName());

            ImageView img_view = findViewById(R.id.image_display);

            img_view.setImageBitmap(ActivePhoto.getBitmap());


            ArrayList<String> tagStrings = new ArrayList<String>();
            for(Tag t: ActivePhoto.getTags()){
                tagStrings.add(t.toString());
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tagStrings);
            tagList.setAdapter(adapter);

    }


    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            gotoAlbumActivity(null);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void gotoAlbumActivity(View view){
        Intent gotoAlbumActivityIntent = new Intent(this, AlbumActivity.class);
        startActivity(gotoAlbumActivityIntent);
    }

    public void deletePhoto(View view){

        new AlertDialog.Builder(this)
                .setTitle("Delete?")
                .setMessage("Do you really want to delete this photo?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        ActiveAlbum.getPhotos().remove(ActivePhoto);
                        gotoAlbumActivity(null);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void editTags(View view){
        Intent i = new Intent(this, TagActivity.class);
        startActivity(i);
    }

    protected void onStop(){
        super.onStop();
        uData.saveUserData(uData.getUser());
    }

    public void movePhoto(View view){
        if(uData.getUser().getAlbums().size() < 2){
            showToast("No albums to move to!");
            return;
        }
        gotoMovePhoto();
    }

    public void gotoMovePhoto(){
        Intent i = new Intent(this, MovePhotoActivity.class);
        startActivity(i);
    }



    public void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
