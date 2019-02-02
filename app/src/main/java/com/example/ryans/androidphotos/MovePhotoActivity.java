package com.example.ryans.androidphotos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import model.Album;
import model.Photo;
import model.Tag;
import model.UserData;

public class MovePhotoActivity extends AppCompatActivity {

    static UserData uData = new UserData();

    ListView albumList;
    ArrayList<String> albumNames;

    Album destination;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_move_layout);

        albumList = findViewById(R.id.list_moveAlbums);


        albumNames = new ArrayList<String>();
        for(Album a : uData.getUser().getAlbums()){
            if(a != uData.getActiveAlbum()){
                albumNames.add(a.getName());
            }
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, albumNames);
        albumList.setAdapter(adapter);


        albumList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                destination = albumWithName(albumNames.get(position));
                new AlertDialog.Builder(MovePhotoActivity.this)
                        .setTitle("Move? or Copy?")
                        .setMessage("Do you want to delete the photo from the source album?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Photo photo = deepCopy(uData.getActivePhoto());

                                uData.getActiveAlbum().getPhotos().remove(uData.getActivePhoto());
                                destination.getPhotos().add(photo);
                                Intent i = new Intent(MovePhotoActivity.this, MainActivity.class);
                                startActivity(i);

                            }})
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Photo photo = deepCopy(uData.getActivePhoto());
                                destination.getPhotos().add(photo);
                                Intent i = new Intent(MovePhotoActivity.this, MainActivity.class);
                                startActivity(i);
                            }}).show();
            }
        });
    }

    public Album albumWithName(String name){
        for(Album a : uData.getUser().getAlbums()){
            if(a.getName().equals(name)){
                return a;
            }
        }
        return null;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            gotoPhotoView();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onStop(){
        super.onStop();
        uData.saveUserData(uData.getUser());
    }

    public void backFromMove(View view){
        gotoPhotoView();
    }

    public void gotoPhotoView(){
        Intent i = new Intent(this, PhotoActivity.class);
        startActivity(i);
    }

    public Photo deepCopy(Photo ph){
        Photo photo = new Photo(ph.getBitmap());
        photo.getTags().addAll(ph.getTags());
        return photo;
    }

}
