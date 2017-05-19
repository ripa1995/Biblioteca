package it.uninsubria.studenti.rripamonti.biblioteca.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rober on 19/05/2017.
 */

public class AlbumContainer {
    @SerializedName("album")
    @Expose
    private Album album;

    public AlbumContainer(){

    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
