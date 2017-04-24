package it.uninsubria.studenti.rripamonti.biblioteca.model;

/**
 * Created by rober on 13/04/2017.
 */

public class User {
    private long id;
    private String name;
    private String surname;
    private String password;
    private String email;
    private boolean admin = false;

    public User(){
        id = -1;
        name = null;
        surname = null;
        password = null;
        email = null;
    }

    public User(String mName, String mSurname,  String mEmail, String mPassword){
        name = mName;
        surname = mSurname;
        password = mPassword;
        email = mEmail;
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


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
