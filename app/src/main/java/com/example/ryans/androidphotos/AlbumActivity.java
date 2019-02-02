package com.example.ryans.androidphotos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import model.Album;
import model.Photo;
import model.User;
import model.UserData;

public class AlbumActivity extends AppCompatActivity {

    UserData uData = new UserData();
    User USER;
    Album ActiveAlbum;
    Photo ActivePhoto;

    ListView thumbnails;
    //Photo[] images;

    View layoutView;
    Button addImage;

    public static final int SELECT_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_album_layout);

        Intent i = getIntent();

        //uData.setDataPath(this);
        ActiveAlbum = uData.getActiveAlbum();

        USER = uData.getUser();
        setTitle(ActiveAlbum.getName());

        thumbnails = findViewById(R.id.in_album_thumbnails);
        layoutView = getLayoutInflater().inflate(R.layout.in_album_layout, null);
        addImage = layoutView.findViewById(R.id.button_addPhoto);
        thumbnails.setAdapter(new ThumbnailAdapater());

        thumbnails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Photo toView = uData.getActiveAlbum().getPhotos().get(position);
                uData.setActivePhoto(toView);
                gotoPhotoActivity(position);
            }
        });


    }

    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            gotoMainActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void gotoMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void backHome(View view){
        gotoMainActivity();
    }


    public void gotoPhotoActivity(int index){
        try {
            Intent gotoPhotoActivityIntent = new Intent(this, PhotoActivity.class);

            startActivityForResult(gotoPhotoActivityIntent, 1001);
        }catch (Exception e){
           showToast(e.getLocalizedMessage());
        }
    }


    public void gotoSlideShow(View view){
        if(ActiveAlbum.getPhotos().size() == 0){
            showToast("No photos to view!");
            return;
        }
        Intent i = new Intent(this, SlideshowActivity.class);
        startActivity(i);
    }


    @Override
    protected void onActivityResult(int requestCode, int result, Intent data){


        if(requestCode == 1001){
           // showToast(Integer.toString(result));
        }else {
            if(data != null) {
                Uri uri = data.getData();
                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                }catch (Exception e){
                    showToast("Unable to import local file.");
                    return;
                }

                Photo ph = new Photo(bitmap);
                ActiveAlbum.getPhotos().add(ph);
                refreshAlbum();
            }
        }

    }

    public void refreshAlbum(){
       // showToast("Refreshing Album");
        thumbnails.setAdapter(new ThumbnailAdapater());

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
            return ActiveAlbum.getPhotos().size();
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

            //view_text.setText(ActiveAlbum.getPhotos().get(position).get);
           // File f = ActiveAlbum.getPhotos().get(position).getFile();

            //view_image.setImageURI(Uri.fromFile(new File(ActiveAlbum.getPhotos().get(position).getUri())));
            view_image.setImageBitmap(ActiveAlbum.getPhotos().get(position).getBitmap());
            view_text.setText("");

            return view;
        }
    }


    public void showFileChooser(View view) {
        /*
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture to Import"), SELECT_IMAGE);
    */

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image to Import");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, SELECT_IMAGE);
    }


    public void editAlbumName(View view){
        Intent intent = new Intent();
    }
}
