package com.example.ryans.androidphotos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.jar.Attributes;

import model.Album;
import model.Photo;
import model.Tag;
import model.User;
import model.UserData;

public class MainActivity extends AppCompatActivity {

    UserData uData = new UserData();
    static User USER;
    Album ActiveAlbum;
    Photo ActivePhoto;

    ListView albums;
    //Photo[] images;

    View layoutView;
    Button addImage;

    private int selected_album_index = -1;

    private String album_name = "";

    public static final int SELECT_IMAGE = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            PackageManager manager = getPackageManager();
            String package_name = getPackageName();
            try {
                PackageInfo package_info = manager.getPackageInfo(package_name, 0);
                uData.setDataPath(package_info.applicationInfo.dataDir);
            }catch (PackageManager.NameNotFoundException e){
                showToast("Error.");
            }

            if(USER == null) {
                USER = uData.loadUserData();
                if (USER == null) {
                    uData.setUser(new User());
                    USER = uData.getUser();
                   // USER.getAlbums().add(new Album("Test Album"));
                }
            }

            albums = findViewById(R.id.thumbnail_list);

            layoutView = getLayoutInflater().inflate(R.layout.album_list_layout, null);

            albums.setAdapter(new ThumbnailAdapater());

            albums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    selected_album_index = position;
                    showAlbumOptions();
                }
            });

    }


    public void showAlbumOptions(){
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Select Action");
            String[] types = {"View Album", "Edit Album Name", "Delete Album"};
            b.setItems(types, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    switch(which){
                        case 0:
                            uData.setActiveAlbum(uData.getUser().getAlbums().get(selected_album_index));
                            gotoAlbumView(selected_album_index);
                            break;
                        case 1:
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Enter the new value for the tag");

                            final EditText input = new EditText(MainActivity.this);
                            input.setText(uData.getUser().getAlbums().get(selected_album_index).getName());
                            input.setInputType(InputType.TYPE_CLASS_TEXT);
                            builder.setView(input);

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(albumNameExists(input.getText().toString())){
                                       return;
                                    }else {
                                        Album a = uData.getUser().getAlbums().get(selected_album_index);
                                        a.setName(input.getText().toString());
                                        refreshList();
                                    }
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            builder.show();
                            break;
                        case 2:
                            confirmAlbumDeletion();
                            break;
                    }
                }

            });
            b.show();
    }

    public boolean albumNameExists(String name){
        for(Album a: uData.getUser().getAlbums()){
            if(a.getName().equals(name)){
                showToast("Error. An album with this name already exists!");
                return true;
            }
        }
        return false;
    }

    public void confirmAlbumDeletion(){
        new AlertDialog.Builder(this)
                .setTitle("Delete?")
                .setMessage("Do you really want to delete this Album?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        uData.getUser().getAlbums().remove(selected_album_index);
                        refreshList();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void gotoAlbumView(int index){
        try {
            Intent gotoAlbumViewIntent = new Intent(this, AlbumActivity.class);

            final int result = 1;

            startActivityForResult(gotoAlbumViewIntent, result);

        }catch (Exception e){
            showToast(e.getLocalizedMessage());
        }
    }

    public void gotoImageSearch(View view){
        Intent i = new Intent(this, ImageSearchActivity.class);
        startActivity(i);
    }


    @Override
    protected void onActivityResult(int requestCode, int result, Intent data){
        refreshList();
    }

    public void refreshList(){ ;
        albums.setAdapter(new ThumbnailAdapater());
    }

    protected void onStop(){
        super.onStop();

        uData.saveUserData(uData.getUser());
    }


    public void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    class ThumbnailAdapater extends BaseAdapter{

        @Override
        public int getCount() {
            return USER.getAlbums().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = getLayoutInflater().inflate(R.layout.album_list_layout, null);
            ImageView view_image = view.findViewById(R.id.album_preview);
            TextView view_text = view.findViewById(R.id.album_name);

            view_text.setText(USER.getAlbums().get(position).getName());
            if(USER.getAlbums().get(position).getPhotos().size() > 0) {
                view_image.setImageBitmap(USER.getAlbums().get(position).getPhotos().get(0).getBitmap());
            }

            return view;
        }
    }


    public void showFileChooser(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
    }



    public void addAlbum(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter a name for the album");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                album_name = input.getText().toString();
                if(albumNameExists(album_name)){
                    return;
                }
                uData.getUser().getAlbums().add(new Album(album_name));
                refreshList();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
        return;
    }


    public void editAlbumName(View view){
        Intent intent = new Intent();
    }

}
