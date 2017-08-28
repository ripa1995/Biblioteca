package it.uninsubria.studenti.rripamonti.biblioteca.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rober on 08/07/2017.
 */

public class BookCover {

    @SerializedName("ASIN")
    @Expose
    private String asin;

    @SerializedName("SmallImage")
    @Expose
    private Image image;

    public BookCover(){

    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public class Image{
        @SerializedName("URL")
        @Expose
        private String url;

        public Image(){

        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
