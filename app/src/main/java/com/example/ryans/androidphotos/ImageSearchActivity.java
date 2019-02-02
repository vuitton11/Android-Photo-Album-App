package com.example.ryans.androidphotos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import model.Album;
import model.Photo;
import model.Tag;
import model.UserData;

public class ImageSearchActivity extends AppCompatActivity {

    private static UserData uData = new UserData();

    Switch nTags;
    Switch andOr;

    Spinner tagKey1;
    Spinner tagKey2;

    EditText tagValue1;
    EditText tagValue2;

    TextView orText;
    TextView andText;

    private int tagsQ = 1;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_search_layout);

        nTags = findViewById(R.id.switch_nTags);
        andOr = findViewById(R.id.switch_andOr);

        tagKey1 = findViewById(R.id.spinner_tagKey1);
        tagKey2 = findViewById(R.id.spinner_tagKey2);

        tagValue1 = findViewById(R.id.text_tagValue1);
        tagValue2 = findViewById(R.id.text_tagValue2);

        orText = findViewById(R.id.text_orText);
        andText = findViewById(R.id.text_andText);

        ArrayList<String> tagKeys = new ArrayList<String>();
        tagKeys.add("Person");
        tagKeys.add("Location");
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tagKeys);

        tagKey1.setAdapter(spinner_adapter);
        tagKey2.setAdapter(spinner_adapter);


        tagValue1.setText("");
        tagValue2.setText("");
        setStateOne();


        nTags.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tagsQ = 2;
                    setStateTwo();
                }else{
                    tagsQ = 1;
                    setStateOne();
                }
            }
        });

    }

    public void leaveSearch(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


    public void setStateOne(){
        tagKey2.setVisibility(View.INVISIBLE);
        tagValue2.setVisibility(View.INVISIBLE);
        andOr.setVisibility(View.INVISIBLE);
        orText.setVisibility(View.INVISIBLE);
        andText.setVisibility(View.INVISIBLE);
    }

    public void setStateTwo(){
        tagKey2.setVisibility(View.VISIBLE);
        tagValue2.setVisibility(View.VISIBLE);
        andOr.setVisibility(View.VISIBLE);
        orText.setVisibility(View.VISIBLE);
        andText.setVisibility(View.VISIBLE);
    }


    public void Search(View view){
        if(tagsQ == 1 && tagValue1.getText().toString().equals("")){
            showToast("Do not leave values blank!");
            return;
        }

        if(tagsQ == 2 && (tagValue1.getText().toString().equals("") || tagValue2.getText().toString().equals(""))) {
            showToast("Do not leave values blank!");
            return;
        }
        if(tagsQ == 1){
            SearchForOneTag();
        }else{
            SearchForTwoTags();
        }
    }

    private void SearchForOneTag(){
        String key = tagKey1.getSelectedItem().toString();
        String value = tagValue1.getText().toString();

        Album searchAlbum = new Album("Searched: " + key + " : " + value);

        for(Album a : uData.getUser().getAlbums()){
            for(Photo p : a.getPhotos()){
                for(Tag t : p.getTags()){
                    if(t.getKey().toLowerCase().equals(key.toLowerCase()) && t.getValue().toLowerCase().contains(value.toLowerCase())){
                        searchAlbum.getPhotos().add(deepCopy(p));
                    }
                }
            }
        }

        if(searchAlbum.getPhotos().size() == 0){
            showToast("No matches found.");
            return;
        }

        uData.setActiveAlbum(searchAlbum);

        Intent i = new Intent(this, AlbumActivity.class);
        startActivity(i);
    }

    private void SearchForTwoTags(){

        String key1 = tagKey1.getSelectedItem().toString();
        String value1 = tagValue1.getText().toString();

        String key2 = tagKey2.getSelectedItem().toString();
        String value2 = tagValue2.getText().toString();

        Album search1 = new Album("");
        Album search2 = new Album("");

        for(Album a : uData.getUser().getAlbums()){
            for(Photo p : a.getPhotos()){
                for(Tag t : p.getTags()){
                    //showToast(key2 + " vs " + t.getKey() + "     " + value2 + " vs " + t.getValue());
                    if(t.getKey().toLowerCase().equals(key1.toLowerCase()) && t.getValue().toLowerCase().contains(value1.toLowerCase())){
                        search1.getPhotos().add(p);
                    }

                    if(t.getKey().toLowerCase().equals(key2.toLowerCase()) && t.getValue().toLowerCase().contains(value2.toLowerCase())){
                        search2.getPhotos().add(p);
                    }
                }
            }
        }

        Album searchAlbum = new Album("Searched: " + key1 + " : " + value1 + "," + key2 + " : " + value2);
        if(andOr.isChecked()){ // And
            for(int i = 0; i < search1.getPhotos().size(); i++){
                if(search2.getPhotos().contains(search1.getPhotos().get(i))){
                    searchAlbum.getPhotos().add(deepCopy(search1.getPhotos().get(i)));
                }
            }
        }else{ // Or

            for(Photo ph : search1.getPhotos()){
                if(!search2.getPhotos().contains(ph)){
                    search2.getPhotos().add(ph);
                }
            }

            for (Photo ph : search2.getPhotos()){
                searchAlbum.getPhotos().add(deepCopy(ph));
            }

        }


        if(searchAlbum.getPhotos().size() == 0){
            showToast("No matches found.");
            return;
        }

        uData.setActiveAlbum(searchAlbum);

        Intent i = new Intent(this, AlbumActivity.class);
        startActivity(i);

    }

    public void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public Photo deepCopy(Photo ph){
        Photo photo = new Photo(ph.getBitmap());
        photo.getTags().addAll(ph.getTags());
        return photo;
    }
}
