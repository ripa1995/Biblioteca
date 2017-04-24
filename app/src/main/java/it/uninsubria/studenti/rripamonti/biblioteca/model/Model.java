package it.uninsubria.studenti.rripamonti.biblioteca.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import it.uninsubria.studenti.rripamonti.biblioteca.model.enums.Type;

/**
 * Created by rober on 15/04/2017.
 */

public class Model {
    private LinkedList<Object> listItem = new LinkedList<Object>();
    private LinkedList<Loan> listLoan = new LinkedList<Loan>();
    private LinkedList<User> listUser = new LinkedList<User>();
    private LinkedList<LibraryObject> listLibraryObject = new LinkedList<LibraryObject>();
    private LibraryObject libraryObject;
    private ExtraActivity extraActivity;
    private Loan loan;
    private User user;
    private DbHelper dbHelper;
    private static Model instance;
    private User userActuallyLoggedIn;

    private Model(Context context){

        dbHelper = new DbHelper(context);
    }

    public static Model setInstance(Context context){
        if (instance == null) {
            instance = new Model(context);
        }
        return instance;
    }
    public static Model getInstance(){
        return instance;
    }

    public void logOut(){
        userActuallyLoggedIn =null;
    }

    public boolean newUser(String mName, String mSurname, String mEmail, String mPassword){
        user = new User(mName,mSurname,mEmail,mPassword);
        long id = dbHelper.insertObject(user);
        if (id==-1){
            //errore
            return false;
        } else {
            user.setId(id);
            listUser.add(user);
            return true;
        }
    }

    public void newBook(String mTitle, String mAuthor, String mCategory){
        libraryObject = new LibraryObject(mTitle, mAuthor, mCategory, Type.BOOK);
        long id = dbHelper.insertObject(libraryObject);
        if (id==-1) {
            //errore
        } else {
            libraryObject.setId(id);
            listItem.add(libraryObject);
        }

    }

    public void newFilm(String mTitle, String mAuthor, String mCategory){
        libraryObject = new LibraryObject(mTitle, mAuthor, mCategory, Type.FILM);
        long id = dbHelper.insertObject(libraryObject);
        if (id==-1) {
            //errore
        } else {
            libraryObject.setId(id);
            listItem.add(libraryObject);
        }
    }

    public void newMusic(String mTitle, String mAuthor, String mCategory){
        libraryObject = new LibraryObject(mTitle, mAuthor, mCategory, Type.MUSIC);
        long id = dbHelper.insertObject(libraryObject);
        if (id==-1) {
            //errore
        } else {
            libraryObject.setId(id);
            listItem.add(libraryObject);
        }
    }

    public void newActivity(String mTitle, String mName, String mSurname, GregorianCalendar mDate){
        extraActivity = new ExtraActivity(mTitle, mName, mSurname, mDate);
        long id = dbHelper.insertObject(extraActivity);
        if (id==-1) {
            //errore
        } else {
            extraActivity.setId(id);
            listItem.add(extraActivity);
        }
    }

    public LinkedList<Object> getListItem() {
        listItem.clear();
        listItem.addAll(dbHelper.getAllItem());
        return listItem;
    }

    public LinkedList<Loan> getListLoanStartable() {
        listLoan.clear();
        listLoan.addAll(dbHelper.getLoanStartable());
        return listLoan;
    }

    public LinkedList<LibraryObject> getListLibraryObject() {
        listLibraryObject.clear();
        listLibraryObject.addAll(dbHelper.getAllLibraryObject());
        return listLibraryObject;
    }

    public int tryLogin(String mEmail, String mPassword){
        userActuallyLoggedIn = dbHelper.userLoginConfirm(mEmail,mPassword);
        if (userActuallyLoggedIn==null) {
            return 0;
        } else if (userActuallyLoggedIn.getAdmin()) {
            return 1;
        } else {
            return 2;
        }

    }

    public LinkedList<LibraryObject> getLastAddedObject(){
        //return last 15 added objects
        listLibraryObject.clear();
        listLibraryObject.addAll(dbHelper.getLast15AddedItem());
        return listLibraryObject;
    }

}
