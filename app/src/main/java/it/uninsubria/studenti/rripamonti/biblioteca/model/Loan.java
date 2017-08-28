package it.uninsubria.studenti.rripamonti.biblioteca.model;

import it.uninsubria.studenti.rripamonti.biblioteca.model.enums.Type;

/**
 * Created by rober on 23/04/2017.
 */

public class Loan {

    private long start_date;

    private String userId;
    private boolean start;
    private String idLoan;
    private Type tipo;
    private String title;
    private String isbn;
    private String author;
    private String loId;
    public Loan() {

    }





    public Loan(String mLo, String uid, String mId, Type mTipo, String mTitle, String mISBN,String mAuthor) {
        idLoan = mId;
        title = mTitle;
        loId = mLo;
        userId = uid;
        start_date = 0;
        start = false;
        tipo = mTipo;
        isbn = mISBN;
        author = mAuthor;

    }

    public String getLoId() {
        return loId;
    }

    public void setLoId(String loId) {
        this.loId = loId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public long getStart_date() {
        return start_date;
    }

    public void setStart_date(long start_date) {
        this.start_date = start_date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public String getIdLoan() {
        return idLoan;
    }

    public void setIdLoan(String idLoan) {
        this.idLoan = idLoan;
    }

    public Type getTipo() {
        return tipo;
    }

    public void setTipo(Type tipo) {
        this.tipo = tipo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author){
        this.author = author;
    }
}
