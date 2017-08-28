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
    private String id;
    private String year;
    private String nIngresso;


    private boolean available = false;

    public LibraryObject(){
        id = null;
        title = null;
        author = null;
        category = null;
        type = null;
        year = null;
        nIngresso = null;
    }

    public LibraryObject(String mTitle, String mAuthor, String mCategory, Type mType, String mid){
        id = mid;
        isbn ="0";
        year = "";
        title = mTitle;
        author = mAuthor;
        category = mCategory;
        type = mType;
        nIngresso = "";
    }
    public LibraryObject(String mTitle, String mAuthor, String mCategory, Type mType, String mIsbn, String mid){
        id = mid;
        isbn =mIsbn;
        title = mTitle;
        author = mAuthor;
        category = mCategory;
        type = mType;
        year = "";
        nIngresso = "";
    }
    public LibraryObject(String mTitle, String mAuthor, String mCategory, Type mType, String mIsbn, String mid, String mYear){
        id = mid;
        isbn =mIsbn;
        title = mTitle;
        author = mAuthor;
        category = mCategory;
        type = mType;
        year = mYear;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getnIngresso() {
        return nIngresso;
    }

    public void setnIngresso(String nIngresso) {
        this.nIngresso = nIngresso;
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
