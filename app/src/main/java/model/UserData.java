package model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.ryans.androidphotos.MainActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;



/**
 * Class for generating, saving, and retrieving user data
 *
 */
public class UserData {

    public static User user;
    public static Album activeAlbum;
    public static Photo activePhoto;

    private static String DATA_PATH;

    public User getUser(){
        return user;
    }


    public void setDataPath(String path){
        //this.DATA_PATH = context.getFilesDir().getPath() + "AndroidPhotos.data";
        this.DATA_PATH = path;
    }

    public String getDataPath(){
        return this.DATA_PATH;
    }

    public Album getActiveAlbum(){
        return activeAlbum;
    }

    public Photo getActivePhoto(){
        return activePhoto;
    }

    public void setUser(User u){
        user = u;
    }

    public void setActiveAlbum(Album a){
        activeAlbum = a;
    }

    public void setActivePhoto(Photo p){
        activePhoto = p;
    }

    /**
     * Serializes the user data and outputs to a file named, [username].save
     * @param u User object to serialize
     */
    public void saveUserData(User u) {

        File file = new File(DATA_PATH + "/AndroidPhotos.data");

        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(u);
            oos.close();
        }catch (Exception e){
            System.out.println("ERROR" + e.getLocalizedMessage());

            System.exit(0);
        }

    }

    /**
     * Attemps to load locally saved user data from within the application workspace
     * If no save file is found, a new User is created.
     */
    public User loadUserData() {
        File file = new File(DATA_PATH + "/AndroidPhotos.data");
        User u = new User();
        if(!file.exists()) {
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            u = (User) ois.readObject();
            ois.close();
        }catch (Exception e){
            System.out.println("ERROR" + e.getLocalizedMessage());
            System.exit(0);
        }

        setUser(u);
        return u;
    }


    public Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            System.out.println("ERERERERRERERER:" + e.getLocalizedMessage());
        }
        return bm;
    }

    public int numberOfPhotos(){
        int totalPhotos = 0;

        for(Album a : getUser().getAlbums()){
            for(Photo p : a.getPhotos()){
                totalPhotos ++;
            }
        }
        return totalPhotos;
    }
}
