package it.uninsubria.studenti.rripamonti.biblioteca.model;

import android.util.Log;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.uninsubria.studenti.rripamonti.biblioteca.model.enums.Type;

/**
 * Created by rober on 16/08/2017.
 */

public class CSVTools {
    private static final String PATH_TO_BOOK = "libri.csv";
    private static final String PATH_TO_MOVIE = "movie.csv";
    private static final String PATH_TO_MUSIC = "music.csv";

    public static List<LibraryObject> leggiCSVlibri(){
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = PATH_TO_BOOK;
        String filePath = baseDir + File.separator + fileName;
        LibraryObject object;
        String[] temp;
        List<LibraryObject> lista = new ArrayList<LibraryObject>();
        File f = new File(filePath );
        CSVReader reader;
        if(f.exists() && !f.isDirectory()){
            try {
                FileReader mFileReader = new FileReader(filePath);
                reader = new CSVReader(mFileReader);
                Iterator<String[]> iterator = reader.iterator();
                while(iterator.hasNext()){
                    temp = iterator.next();
                    object = new LibraryObject();
                    object.setType(Type.BOOK);
                    object.setAuthor(temp[0]);
                    object.setTitle(temp[1]);
                    object.setCategory(temp[2]);
                    object.setYear(temp[3]);
                    object.setIsbn(temp[4]);
                    object.setnIngresso(temp[5]);
                    lista.add(object);
                    Log.d("READCSV", "aggiungo a lista");
                }
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ReadCSV", "File non esistente");
        }
        Log.d("CSVREAD",String.valueOf(lista.size()));
        return lista;

    }

    public static List<LibraryObject> leggiCSVfilm(){
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = PATH_TO_MOVIE;
        String filePath = baseDir + File.separator + fileName;
        LibraryObject object;
        String[] temp;
        List<LibraryObject> lista = new ArrayList<LibraryObject>();
        File f = new File(filePath );
        CSVReader reader;
        if(f.exists() && !f.isDirectory()){
            try {
                FileReader mFileReader = new FileReader(filePath);
                reader = new CSVReader(mFileReader);
                Iterator<String[]> iterator = reader.iterator();
                while(iterator.hasNext()){
                    temp = iterator.next();
                    object = new LibraryObject(temp[0],temp[1],temp[3], Type.FILM, temp[2]);
                    lista.add(object);
                    Log.d("READCSV", "aggiungo a lista");
                }
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ReadCSV", "File non esistente");
        }
        Log.d("CSVREAD",String.valueOf(lista.size()));
        return lista;

    }

    public static List<LibraryObject> leggiCSVmusica(){
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = PATH_TO_MUSIC;
        String filePath = baseDir + File.separator + fileName;
        LibraryObject object;
        String[] temp;
        List<LibraryObject> lista = new ArrayList<LibraryObject>();
        File f = new File(filePath );
        CSVReader reader;
        if(f.exists() && !f.isDirectory()){
            try {
                FileReader mFileReader = new FileReader(filePath);
                reader = new CSVReader(mFileReader);
                Iterator<String[]> iterator = reader.iterator();
                while(iterator.hasNext()){
                    temp = iterator.next();
                    object = new LibraryObject(temp[0],temp[1],temp[3], Type.MUSIC, temp[2]);
                    lista.add(object);
                    Log.d("READCSV", "aggiungo a lista");
                }
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ReadCSV", "File non esistente");
        }
        Log.d("CSVREAD",String.valueOf(lista.size()));
        return lista;

    }


    public static void salvaCSV(List<LibraryObject> listaLibri, List<LibraryObject> listaFilm, List<LibraryObject> listaMusica) throws IOException {
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = PATH_TO_BOOK;
        String filePath = baseDir + File.separator + fileName;
        LibraryObject object;
        List<String[]> lista = new ArrayList<String[]>();
        File f = new File(filePath );
        CSVWriter writer;
        // File exist

            writer = new CSVWriter(new FileWriter(filePath));

        if (listaLibri.size()!=0) {
            Iterator<LibraryObject> libriIterator = listaLibri.iterator();
            while (libriIterator.hasNext()) {
                object = libriIterator.next();
                String[] array = new String[]{object.getAuthor(), object.getTitle(), object.getCategory(), object.getYear(), object.getIsbn(), object.getnIngresso()};
                lista.add(array);
                Log.d("salvaCSV", "1");
            }

            writer.writeAll(lista);
            writer.close();
            lista.clear();
        }

        String fileName2 = PATH_TO_MOVIE;
        String filePath2 = baseDir + File.separator + fileName2;
        f = new File(filePath2 );


            writer = new CSVWriter(new FileWriter(filePath2));

        if (listaFilm.size()!=0) {
            Iterator<LibraryObject> filmIterator = listaFilm.iterator();
            while (filmIterator.hasNext()) {
                object = filmIterator.next();
                String[] array = new String[]{object.getTitle(), object.getAuthor(), object.getIsbn(), object.getCategory()};
                lista.add(array);
                Log.d("salvaCSV", "1");
            }

            writer.writeAll(lista);
            writer.close();
            lista.clear();
        }


        String fileName3 = PATH_TO_MUSIC;
        String filePath3 = baseDir + File.separator + fileName3;
        f = new File(filePath3 );

        // File exist


            writer = new CSVWriter(new FileWriter(filePath3));

        if (listaMusica.size()!=0) {
            Iterator<LibraryObject> musicaIterator = listaMusica.iterator();
            while (musicaIterator.hasNext()) {
                object = musicaIterator.next();
                String[] array = new String[]{object.getTitle(), object.getAuthor(), object.getIsbn(), object.getCategory()};
                lista.add(array);
                Log.d("salvaCSV", "1");
            }

            writer.writeAll(lista);
            writer.close();
            lista.clear();
        }
    }
}
