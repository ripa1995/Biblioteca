package it.uninsubria.studenti.rripamonti.biblioteca.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import it.uninsubria.studenti.rripamonti.biblioteca.model.enums.Type;

/**
 * Created by rober on 23/04/2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "biblioteca.db";
    private final static int DB_VERSION = 5;
    private final static String TABLE_ACTIVITIES = "activities";
    private final static String TABLE_OBJECTS = "objects";
    private final static String TABLE_USERS = "users";
    private final static String TABLE_LOANS = "loans";
    private final static String COLUMN_ID_A = "a_id";
    private final static String COLUMN_TITLE = "title";
    private final static String COLUMN_NAME = "name";
    private final static String COLUMN_SURNAME = "surname";
    private final static String COLUMN_INSERT_DATE = "insert_date";
    private final static String COLUMN_EVENT_DATE = "event_date";
    private final static String COLUMN_AUTHOR = "author";
    private final static String COLUMN_ID_O = "o_id";
    private final static String COLUMN_CATEGORY = "category";
    private final static String COLUMN_TYPE = "type";
    private final static String COLUMN_ID_L = "l_id";
    private final static String COLUMN_DATE_STARTLOAN = "date_startloan";
    private final static String COLUMN_DATE_ENDLOAN = "date_endloan";
    private final static String COLUMN_ACCEPTED = "accepted";
    private final static String COLUMN_ID_U = "u_id";
    private final static String COLUMN_EMAIL = "email";
    private final static String COLUMN_PASSWORD = "password";
    private final static String COLUMN_ADMIN = "admin";
    //private final static String TRIGGER_UPDATE_LOAN = "upd_check";
    private final static String COLUMN_ISBN = "isbn";

    public DbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ TABLE_ACTIVITIES + " (" + COLUMN_ID_A + " integer primary key autoincrement, "+ COLUMN_TITLE + " TEXT NOT NULL UNIQUE, " + COLUMN_NAME + " TEXT NOT NULL UNIQUE, " + COLUMN_SURNAME + " TEXT NOT NULL UNIQUE, "+ COLUMN_INSERT_DATE + " INTEGER NOT NULL, " + COLUMN_EVENT_DATE +" INTEGER NOT NULL UNIQUE);");
        db.execSQL("CREATE TABLE "+TABLE_OBJECTS+" ("+COLUMN_ID_O+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ COLUMN_ISBN + " TEXT default 0, "+COLUMN_TITLE+" TEXT NOT NULL, "+COLUMN_AUTHOR+" TEXT NOT NULL, "+COLUMN_CATEGORY+" TEXT NOT NULL, "+COLUMN_TYPE+" TEXT NOT NULL CHECK ("+COLUMN_TYPE+" IN ('BOOK','FILM','MUSIC')), " + COLUMN_INSERT_DATE+ " INTEGER NOT NULL);");
        db.execSQL("CREATE TABLE "+TABLE_USERS+" ("+COLUMN_ID_U+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_NAME + " TEXT NOT NULL, " + COLUMN_SURNAME + " TEXT NOT NULL, "+ COLUMN_EMAIL+" TEXT NOT NULL UNIQUE, " + COLUMN_PASSWORD + " TEXT NOT NULL, "+ COLUMN_ADMIN +" BOOLEAN DEFAULT FALSE);");
        db.execSQL("CREATE TABLE "+ TABLE_LOANS + " (" + COLUMN_ID_L + " INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_ID_O + " INTEGER NOT NULL, " + COLUMN_ID_U + " INTEGER NOT NULL, " + COLUMN_DATE_STARTLOAN + " INTEGER DEFAULT NULL, " + COLUMN_DATE_ENDLOAN + " INTEGER DEFAULT NULL, " + COLUMN_ACCEPTED + " BOOLEAN DEFAULT FALSE, " + "FOREIGN KEY (" + COLUMN_ID_O+") REFERENCES "+ TABLE_OBJECTS +"("+COLUMN_ID_O+"), " + "FOREIGN KEY (" + COLUMN_ID_U + ") REFERENCES " + TABLE_USERS +"("+COLUMN_ID_U+"));");
        //db.execSQL("CREATE TRIGGER " + TRIGGER_UPDATE_LOAN + " after UPDATE ON " + TABLE_LOANS+ " FOR EACH ROW BEGIN if exists(SELECT * FROM " + TABLE_LOANS + " WHERE "+ COLUMN_ID_U+ " != new." + COLUMN_ID_U+ " and " + COLUMN_ID_O+ " = new." + COLUMN_ID_O+" and " + COLUMN_DATE_ENDLOAN + " is null and " + COLUMN_ACCEPTED+" = true) then update "+TABLE_LOANS+" set new." + COLUMN_ACCEPTED+" = false, new." + COLUMN_DATE_STARTLOAN  +" = null; end if; END);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_LOANS);
        db.execSQL("drop table if exists "+TABLE_USERS);
        db.execSQL("drop table if exists "+TABLE_OBJECTS);
        db.execSQL("drop table if exists "+TABLE_ACTIVITIES);
       // db.execSQL("drop trigger " + TRIGGER_UPDATE_LOAN);
        this.onCreate(db);
    }

    public long insertObject(Object item){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        if (item instanceof LibraryObject){
            LibraryObject object = (LibraryObject) item;
            cv.put(COLUMN_ISBN, object.getIsbn());
            cv.put(COLUMN_TITLE, object.getTitle());
            cv.put(COLUMN_AUTHOR, object.getAuthor());
            cv.put(COLUMN_TYPE, object.getType().toString());
            cv.put(COLUMN_CATEGORY, object.getCategory());
            cv.put(COLUMN_INSERT_DATE, gregorianCalendar.getTimeInMillis());
            long id = db.insert(TABLE_OBJECTS, null, cv);
            db.close();
            return id;
        } else if (item instanceof ExtraActivity){
            ExtraActivity object = (ExtraActivity) item;
            cv.put(COLUMN_TITLE, object.getTitle());
            cv.put(COLUMN_NAME, object.getName());
            cv.put(COLUMN_SURNAME, object.getSurname());
            cv.put(COLUMN_INSERT_DATE, gregorianCalendar.getTimeInMillis());
            cv.put(COLUMN_EVENT_DATE, object.getDate().getTimeInMillis());
            long id = db.insert(TABLE_ACTIVITIES, null, cv);
            db.close();
            return id;
        } else if (item instanceof User){
            User object = (User) item;
            cv.put(COLUMN_NAME, object.getName());
            cv.put(COLUMN_SURNAME, object.getSurname());
            cv.put(COLUMN_EMAIL, object.getEmail());
            cv.put(COLUMN_PASSWORD, object.getPassword());
            cv.put(COLUMN_ADMIN, object.getAdmin());
            long id = db.insert(TABLE_USERS, null, cv);
            db.close();
            return id;
        } else if (item instanceof Loan){
            Loan object = (Loan) item;
            cv.put(COLUMN_ID_O,object.getLibraryObjectID());
            cv.put(COLUMN_ID_U,object.getUserID());
            gregorianCalendar = object.getStart_date();
            if (gregorianCalendar!=null) {
                cv.put(COLUMN_DATE_STARTLOAN, object.getStart_date().getTimeInMillis());
            }
            gregorianCalendar = object.getEnd_date();
            if (gregorianCalendar!=null) {
                cv.put(COLUMN_DATE_ENDLOAN, object.getEnd_date().getTimeInMillis());
            }
            cv.put(COLUMN_ACCEPTED, object.isAccepted());
            long id = db.insert(TABLE_LOANS, null, cv);
            db.close();
            return id;
        }
        return -1;
    }

    public List<ExtraActivity> getAllExtraActivity(){
        List<ExtraActivity> list = new LinkedList<ExtraActivity>();
        String query2 = "select * from " + TABLE_ACTIVITIES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query2, null);
        ExtraActivity activity;
        if (cursor.moveToFirst()){
            do{
                activity = new ExtraActivity();
                activity.setId(Integer.parseInt(cursor.getString(0)));
                activity.setTitle(cursor.getString(1));
                activity.setName(cursor.getString(2));
                activity.setSurname(cursor.getString(3));
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTimeInMillis(cursor.getLong(5));
                activity.setDate(gregorianCalendar);
                list.add(activity);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<LibraryObject> getAllLibraryObject(){
        List<LibraryObject> list = new LinkedList<LibraryObject>();
        String query = "select * from " + TABLE_OBJECTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        LibraryObject object;
        if (cursor.moveToFirst()){
            do{
                object = new LibraryObject();
                object.setId(Integer.parseInt(cursor.getString(0)));
                object.setIsbn(cursor.getString(1));
                object.setTitle(cursor.getString(2));
                object.setAuthor(cursor.getString(3));
                object.setCategory(cursor.getString(4));
                object.setType(Type.valueOf(cursor.getString(5)));
                list.add(object);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<Object> getAllItem(){
        List<Object> list = new LinkedList<Object>();
        list.addAll(getAllLibraryObject());
        list.addAll(getAllExtraActivity());
        return list;
    }

    public List<LibraryObject> getLast15AddedItem(){
        List<LibraryObject> list = new LinkedList<LibraryObject>();
        String query = "select * from " + TABLE_OBJECTS + " order by " + COLUMN_INSERT_DATE + " DESC limit 15";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        LibraryObject object;
        if (cursor.moveToFirst()){
            do{
                object = new LibraryObject();
                object.setId(Integer.parseInt(cursor.getString(0)));
                object.setIsbn(cursor.getString(1));
                object.setTitle(cursor.getString(2));
                object.setAuthor(cursor.getString(3));
                object.setCategory(cursor.getString(4));
                object.setType(Type.valueOf(cursor.getString(5)));
                list.add(object);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<Loan> getLoanStartable(){
        List<Loan> list = new LinkedList<Loan>();
        String query = "select * from " + TABLE_LOANS + " WHERE "+ COLUMN_ID_O + " NOT IN(SELECT " + COLUMN_ID_O +" FROM "+ TABLE_LOANS +" WHERE " + COLUMN_ACCEPTED + " = TRUE AND "+ COLUMN_DATE_ENDLOAN +" IS NULL);";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Loan loan;
        if (cursor.moveToFirst()){
            do {
                loan = new Loan();
                loan.setLoanID(Integer.parseInt(cursor.getString(0)));
                loan.setLibraryObjectID(Integer.parseInt(cursor.getString(1)));
                loan.setUserID(Integer.parseInt(cursor.getString(2)));
                loan.setAccepted(Boolean.parseBoolean(cursor.getString(5)));
                list.add(loan);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public User userLoginConfirm(String mEmail, String mPassword){
        SQLiteDatabase db = this.getWritableDatabase();
        User user = null;
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID_U,COLUMN_NAME,COLUMN_SURNAME,COLUMN_EMAIL,COLUMN_PASSWORD,COLUMN_ADMIN},COLUMN_EMAIL+"=? and "+COLUMN_PASSWORD +"=?",new String[]{mEmail, mPassword},null,null,null);
        if (cursor.moveToFirst()){
            user = new User();
            user.setId(Integer.parseInt(cursor.getString(0)));
            user.setName(cursor.getString(1));
            user.setSurname(cursor.getString(2));
            user.setEmail(cursor.getString(3));
            user.setPassword(cursor.getString(4));
            user.setAdmin(Boolean.parseBoolean(cursor.getString(5)));
        }
        db.close();
        return user;
    }

    public void deleteObject(Object item){
        SQLiteDatabase db = this.getWritableDatabase();
        if (item instanceof LibraryObject) {
            LibraryObject libraryObject = (LibraryObject) item;
            db.delete(TABLE_OBJECTS, COLUMN_ID_O + " = ?", new String[]{String.valueOf(libraryObject.getId())});
        } else if (item instanceof ExtraActivity) {
            ExtraActivity extraActivity = (ExtraActivity) item;
            db.delete(TABLE_ACTIVITIES, COLUMN_ID_A + " = ?", new String[]{String.valueOf(extraActivity.getId())});
        } else if (item instanceof User) {
            User user = (User) item;
            db.delete(TABLE_USERS, COLUMN_ID_U + " = ?", new String[]{String.valueOf(user.getId())});
        }
        db.close();
    }
}
