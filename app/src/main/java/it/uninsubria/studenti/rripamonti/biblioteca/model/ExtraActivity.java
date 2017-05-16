package it.uninsubria.studenti.rripamonti.biblioteca.model;

import java.text.SimpleDateFormat;

/**
 * Created by rober on 13/04/2017.
 */

public class ExtraActivity {
    private String title;
    private String name;
    private String surname;
    private long date;

    public ExtraActivity(){
        date = 0;
        name = null;
        surname = null;

        title = null;
    }

    public ExtraActivity(String mTitle, String mName, String mSurname, long mDate){
        date = mDate;
        title = mTitle;
        name = mName;
        surname = mSurname;

    }
    public ExtraActivity(long mDate, String mName, String mSurname,String mTitle ){
        date = mDate;
        title = mTitle;
        name = mName;
        surname = mSurname;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        String data = new SimpleDateFormat("dd/MM/yyyy").format(date);
        return "ExtraActivity{" +
                "title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", date=" + data +
                '}';
    }
}
