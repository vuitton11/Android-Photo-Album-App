package model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Class representation of a photo album
 *
 */
public class Album implements Comparable<Album>, Serializable{
    /**
     * Name of album
     */
    private String name;

    /**
     * List of photos contained in the album
     */
    private ArrayList<Photo> photos = new ArrayList<Photo>();


    /**
     * Album Constructor
     * @param name Name to assign to the album
     */
    public Album(String name) {
        this.name = name;
    }

    /**
     * Sets name of album
     * @param name Name to assign to the album
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets name of album
     * @return Album name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns album as a string. If the album contains no photos, the String will just be the album name.
     * Otherwise, the string will contain the album name, number of photos, and date ranges for the photos in the album.
     * However, if the dates are unable to be parsed, the date ranges will be left out of the string.
     */
    public String toString() {
        int numOfPhotos = photos.size();
        if(numOfPhotos == 0) {
            return this.name;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        Calendar mostRecent = Calendar.getInstance();
        mostRecent.clear();
        try {
            mostRecent.setTime(sdf.parse("12/31/9999"));
        } catch (ParseException e2) {
            e2.printStackTrace();
        }
        mostRecent.set(Calendar.MILLISECOND, 0);
        Calendar leastRecent = Calendar.getInstance();
        leastRecent.clear();
        try {
            leastRecent.setTime(sdf.parse("12/31/1000"));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        leastRecent.set(Calendar.MILLISECOND, 0);


        Date pDate = new Date();
        Calendar checkCal = Calendar.getInstance();
        checkCal.clear();
        for(int i = 0; i < numOfPhotos; i++) {
            Photo p = photos.get(i);

            try {
                pDate = sdf.parse(p.getDate());
            } catch(Exception e){
                return this.name + "  ||  " + "Photos: " + numOfPhotos + "    " + "Date Range: " + " unknown ";
            }
            checkCal.setTime(pDate);
            checkCal.set(Calendar.MILLISECOND, 0);

            if(checkCal.before(mostRecent)) {
                mostRecent.setTime(checkCal.getTime());
            }

            if(checkCal.after(leastRecent)) {
                leastRecent.setTime(checkCal.getTime());
            }
        }

        String d1 = getDateString(mostRecent);
        String d2 = getDateString(leastRecent);
        return this.name + "  ||  " + "Photos: " + numOfPhotos + "    " + "Date Range: " + d1 + " - " + d2;
    }

    /**
     * Turns calendar into MM/dd/yyyy string format
     * @param c Calendar to format
     * @return Formatted date string in MM/dd/yyyy format
     */
    private String getDateString(Calendar c) {
        int m = c.get(Calendar.MONTH)+1;
        int d = c.get(Calendar.DATE);
        int y = c.get(Calendar.YEAR);
        return m + "/" + d + "/" + y;
    }

    /**
     * compareTo implementation of Comparable interface
     * @param a Album to compare this album to
     * @return 0 as a default
     */
    public int compareTo(Album a) {
        return 0;
    }

    /**
     * Retrieves the list of photos that are a part of this album
     * @return ArrayList of photos in this album
     */
    public ArrayList<Photo> getPhotos(){
        return this.photos;
    }

    /**
     * Removes a photo from this albums list of photos
     * @param photo Photo object to remove from this album
     */
    public void removePhoto(Photo photo) {
        this.photos.remove(photo);
    }
}
