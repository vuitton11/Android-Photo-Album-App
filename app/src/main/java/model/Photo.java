package model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Class representation of a photo
 *
 */
public class Photo implements Serializable{

    /**
     * Caption of photo
     */
    private String caption = "";

    /**
     * Path of image file
     */
    private String path;

    /**
     * List of tags for this photo
     */
    private ArrayList<Tag> tags = new ArrayList<Tag>();

    /**
     * Calendar holding date of photo's 'last modified' date
     */
    private Calendar calendar = Calendar.getInstance();

    private byte[] bitmap_bytes;

    /**
     * Photo constructor
     * @param uri File path of image
     */
    public Photo(String uri){
        this.path = uri;
    }


    public Photo(Bitmap bitmap){
        this.bitmap_bytes = BitmapToBytes(bitmap);
    }




    /**
     * Sets values of month, day, and year in calendar
     * @param month Month of date
     * @param day Day of date
     * @param year Year of date
     */
    public void setDate(int month, int day, int year) {
        this.calendar.clear();
        this.calendar.set(year, month-1, day);
        this.calendar.set(Calendar.MILLISECOND, 0);
    }

    public File getFile(){
        File file = new File(this.path.toString());
        return file;
    }

    public Bitmap getBitmap(){
        return BitmapFactory.decodeByteArray(this.bitmap_bytes, 0, this.bitmap_bytes.length);
    }

    /**
     * Retrieves this photo's date in MM/dd/yyyy format
     * @return Date string formatted as MM/dd/yyyy
     */
    public String getDate() {
        int m = this.calendar.get(Calendar.MONTH) + 1;
        int d = this.calendar.get(Calendar.DATE);
        int y = this.calendar.get(Calendar.YEAR);
        return m + "/" + d + "/" + y;
    }

    /**
     * Retrieves the calendar of the photo's date
     * @return Calendar of this photo
     */
    public Calendar getCalendar() {
        return this.calendar;
    }

    /**
     * Retrieves path of this photo's image file
     * @return file path
     */
    public String getUri() {
        return path;
    }


    /**
     * Retrieves this photo's caption
     * @return Caption of this photo
     */
    public String getCaption() {
        return this.caption;
    }

    /**
     * Sets this photo's caption
     * @param caption Text to set as this photo's caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * List of this photo's tags
     * @return ArrayList of this photo's tags
     */
    public ArrayList<Tag> getTags(){
        return this.tags;
    }

    public ArrayList<String> tagsAsStrings(){
        ArrayList<String> list = new ArrayList<String>();

        for(Tag t: tags){
            list.add(t.toString());
        }
        return list;
    }

    private byte[] BitmapToBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();

        return byteArray;
    }

}
