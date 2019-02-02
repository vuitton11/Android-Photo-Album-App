package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class representation of a User of the application
 *
 */
public class User implements Serializable {

    /**
     * List of this user's albums
     */
    public ArrayList<Album> albums = new ArrayList<Album>();

    /**
     * List of this user's tag types (tag keys)
     */
    private ArrayList<String> tagTypes = new ArrayList<String>();

    /**
     * User constructor
     */
    public User() {
        this.tagTypes.add("Person");
        this.tagTypes.add("Location");
    }

    /**
     * Retrieves albums of this user
     * @return ArrayList of this user's albums
     */
    public ArrayList<Album> getAlbums(){
        return this.albums;
    }

    /**
     * Retrieves tag types of this user
     * @return ArrayList of this user's tag types
     */
    public ArrayList<String> getTagTypes(){
        return this.tagTypes;
    }

    /**
     * Adds a new tag type as an option for when the user adds a new tag to a photo
     * @param type String to add as a new key option for tags
     */
    public void addTagType(String type) {
        this.tagTypes.add(type);
    }
}