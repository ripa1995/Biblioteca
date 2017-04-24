package it.uninsubria.studenti.rripamonti.biblioteca.model;

import java.util.GregorianCalendar;

/**
 * Created by rober on 23/04/2017.
 */

public class Loan {
    private long libraryObjectID;
    private long userID;
    private long loanID;
    private GregorianCalendar start_date;
    private GregorianCalendar end_date;
    private boolean accepted;
    public Loan(){
        libraryObjectID = -1;
        userID = -1;
        start_date = null;
        end_date = null;
        accepted = false;
    }
    public Loan(int mLibraryObjectID, int mUserID){
        libraryObjectID = mLibraryObjectID;
        userID = mUserID;
    }

    public long getLibraryObjectID() {
        return libraryObjectID;
    }

    public void setLibraryObjectID(long libraryObjectID) {
        this.libraryObjectID = libraryObjectID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public GregorianCalendar getStart_date() {
        return start_date;
    }

    public void setStart_date(GregorianCalendar start_date) {
        this.start_date = start_date;
    }

    public GregorianCalendar getEnd_date() {
        return end_date;
    }

    public void setEnd_date(GregorianCalendar end_date) {
        this.end_date = end_date;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public long getLoanID() {
        return loanID;
    }

    public void setLoanID(long loanID) {
        this.loanID = loanID;
    }
}
