package it.uninsubria.studenti.rripamonti.biblioteca.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by rober on 13/04/2017.
 */

public class ExtraActivity extends Item{
    private String title;
    private String name;
    private String surname;
    private GregorianCalendar date;
    private long id = -1;

    public ExtraActivity(){
        title = null;
        name = null;
        surname = null;
        date = null;
    }

    public ExtraActivity(String mTitle, String mName, String mSurname, GregorianCalendar mDate){
        title = mTitle;
        name = mName;
        surname = mSurname;
        date = mDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    @Override
    public String toString() {
        String data = new SimpleDateFormat("dd/MM/yyyy").format(date.getTime());
        return "ExtraActivity{" +
                "title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", date=" + data +
                '}';
    }
}
