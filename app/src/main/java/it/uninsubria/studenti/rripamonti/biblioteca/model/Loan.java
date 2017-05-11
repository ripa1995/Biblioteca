package it.uninsubria.studenti.rripamonti.biblioteca.model;

import java.util.GregorianCalendar;

/**
 * Created by rober on 23/04/2017.
 */

public class Loan {
    private LibraryObject lo;
    private long start_date;
    private long end_date;


    public Loan() {
        lo = null;
        start_date = 0;


    }

    public Loan(LibraryObject mLo) {
        this.lo = mLo;

        start_date = new GregorianCalendar().getTimeInMillis();

    }

    public LibraryObject getLibraryObject() {
        return lo;
    }

    public void setLibraryObject(LibraryObject libraryObjectID) {
        this.lo = libraryObjectID;
    }

    public long getStart_date() {
        return start_date;
    }

    public void setStart_date(long start_date) {
        this.start_date = start_date;
    }


}
