package it.uninsubria.studenti.rripamonti.biblioteca.model;

import it.uninsubria.studenti.rripamonti.biblioteca.model.enums.Type;

/**
 * Created by rober on 23/04/2017.
 */

public class Loan {
    private String libraryObjectId;
    private long start_date;

    private String userId;
    private boolean start;
    private String idLoan;
    private Type tipo;
    private String title;

    public Loan() {

    }





    public Loan(String mLo, String uid, String mId, Type mTipo, String mTitle) {
        this.idLoan = mId;
        this.title = mTitle;
        this.libraryObjectId = mLo;
        this.userId = uid;
        start_date = 0;
        start = false;
        tipo = mTipo;


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLibraryObjectId() {
        return libraryObjectId;
    }

    public void setLibraryObjectId(String libraryObjectId) {
        this.libraryObjectId = libraryObjectId;
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
}
