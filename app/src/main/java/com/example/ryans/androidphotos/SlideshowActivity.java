package com.example.ryans.androidphotos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import model.UserData;

public class SlideshowActivity extends AppCompatActivity {

    private static UserData uData = new UserData();
    private int photo_index = 0;

    ImageView photoDisplay;
    View layoutView;

    Button buttonPrev;
    Button buttonNext;

    Bitmap[] bitmaps = new Bitmap[uData.getActiveAlbum().getPhotos().size()];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.slideshow_layout);

        layoutView = getLayoutInflater().inflate(R.layout.slideshow_layout, null);

        photoDisplay = findViewById(R.id.slideshow_display);

        buttonPrev = findViewById(R.id.button_slideshowPrevious);
        buttonNext = findViewById(R.id.button_slideshowNext);

        for (int i = 0; i < bitmaps.length; i++){
            bitmaps[i] = uData.getActiveAlbum().getPhotos().get(i).getBitmap();
        }

        checkButtonState();

    }

    public void backFromSlideshow(View view){
        Intent i = new Intent(this, AlbumActivity.class);
        startActivity(i);
    }

    public void nextPhoto(View view){
        photo_index++;
        checkButtonState();
    }

    public void prevPhoto(View view){
        photo_index--;
        checkButtonState();
    }

    protected void onStop(){
        super.onStop();
        uData.saveUserData(uData.getUser());
    }

    public void checkButtonState(){
        try {
            photoDisplay.setImageBitmap(bitmaps[photo_index]);
        }catch (Exception e){
            e.printStackTrace();
        }

        int nPhotos = uData.getActiveAlbum().getPhotos().size();
        buttonPrev.setVisibility(View.VISIBLE);
        buttonNext.setVisibility(View.VISIBLE);

        if(photo_index == 0){
            buttonPrev.setVisibility(View.INVISIBLE);
        }
        if(photo_index == nPhotos - 1){
            buttonNext.setVisibility(View.INVISIBLE);
        }
    }
}
