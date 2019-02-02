package model;

import java.io.Serializable;


/**
 * Class representation of a photo tag
 *
 */
public class Tag implements Comparable<Tag>, Serializable{
    /**
     * The 'key' in the key:value pair of the tag
     */
    private String key;

    /**
     * The 'value' in the key:value pair of the tag
     */
    private String value;

    /**
     * Tag constructor
     * @param key key in key:value pair for tag
     * @param value value in key:value pair for tag
     */
    public Tag(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Retrieves the 'key' in the key:value pair of the tag
     * @return key of tag
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Retrieves the 'value' in the key:value pair of the tag
     * @return value of tag
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Sets the key in the key:value pair of the tag
     * @param key key of tag
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Sets the value in the key:value pair of the tag
     * @param value value of tag
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     *@return String joining this tag's key and value by a colon, key:value
     */
    public String toString() {
        return this.key + "\t:\t" + this.value;
    }

    /**
     * Comparable interface implementation of compareTo
     * @param a Tag to compare this tag to
     * @return 0 as a default
     */
    public int compareTo(Tag a) {
        return 0;
    }
}
