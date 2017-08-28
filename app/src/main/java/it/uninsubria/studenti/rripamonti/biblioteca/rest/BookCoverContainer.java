package it.uninsubria.studenti.rripamonti.biblioteca.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rober on 08/07/2017.
 */

public class BookCoverContainer {
    @SerializedName("item")
    @Expose
    private BookCover bookCover;

    public BookCoverContainer(){

    }

    public BookCover getBookCover() {
        return bookCover;
    }

    public void setBookCover(BookCover bookCover) {
        this.bookCover = bookCover;
    }
}
