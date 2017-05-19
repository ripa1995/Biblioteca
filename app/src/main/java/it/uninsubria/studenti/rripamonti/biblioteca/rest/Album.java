package it.uninsubria.studenti.rripamonti.biblioteca.rest;





import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rober on 19/05/2017.
 */

public class Album {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("artist")
    @Expose
    private String artist;

    @SerializedName("image")
    @Expose
    private List<Image> images;

    public Album(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public class Image{
        @SerializedName("#text")
        @Expose
        private String text;

        @SerializedName("size")
        @Expose
        private String size;

        public Image(){

        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }
}
