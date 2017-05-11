package it.uninsubria.studenti.rripamonti.biblioteca.model;

import java.io.Serializable;

import it.uninsubria.studenti.rripamonti.biblioteca.model.enums.Type;

/**
 * Created by rober on 13/04/2017.
 */

public class LibraryObject implements Serializable{

    private String title, author, isbn;
    private String category;
    private Type type;


    private boolean available = false;

    public LibraryObject(){

        title = null;
        author = null;
        category = null;
        type = null;
    }

    public LibraryObject(String mTitle, String mAuthor, String mCategory, Type mType){
        isbn ="0";
        title = mTitle;
        author = mAuthor;
        category = mCategory;
        type = mType;
    }
    public LibraryObject(String mTitle, String mAuthor, String mCategory, Type mType, String mIsbn){
        isbn =mIsbn;
        title = mTitle;
        author = mAuthor;
        category = mCategory;
        type = mType;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    @Override
    public String toString() {

            return "LibraryObject{" +
                    ", title='" + title + '\'' +
                    ", author='" + author + '\'' +
                    ", category=" + category +
                    ", type=" + type.toString() +
                    '}';

    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
