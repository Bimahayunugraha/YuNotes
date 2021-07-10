package com.example.yunotes.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//Membuat entitiy tabel dengan nama "notes"
@Entity(tableName = "notes")

//Menambahkan fungsi serializable untuk mengubah objek menjadi byte kemudian disimpan ke dalam database
public class Note implements Serializable {

    //Membuat column id dengan tipe data int dan set menjadi primary key
    @PrimaryKey(autoGenerate = true)
    private int id;

    //Membuat column title dengan tipe data string
    @ColumnInfo(name = "title")
    private String title;

    //Membuat column dateTime dengan tipe data string
    @ColumnInfo(name = "date_time")
    private String dateTime;

    //Membuat column subtitle dengan tipe data string
    @ColumnInfo(name = "subtitle")
    private String subtitle;

    //Membuat column textNote dengan tipe data string
    @ColumnInfo(name = "text_note")
    private String textNote;

    //Membuat column imagePath dengan tipe data string
    @ColumnInfo(name = "image_path")
    private String imagePath;

    //Membuat column color dengan tipe data string
    @ColumnInfo(name = "color")
    private String color;

    //Membuat column linkWeb dengan tipe data string
    @ColumnInfo(name = "link_web")
    private String linkWeb;

    //Membuat method getId untuk mengembalikan nilai ke variabel id
    public int getId() {
        return id;
    }

    //Membuat method setId untuk mengembalikan nilai ke variabel id
    public void setId(int id) {
        this.id = id;
    }

    //Membuat method getTitle untuk mengembalikan nilai ke variabel title
    public String getTitle() {
        return title;
    }

    //Membuat method setTitle untuk mengembalikan nilai ke variabel title
    public void setTitle(String title) {
        this.title = title;
    }

    //Membuat method getDateTime untuk mengembalikan nilai ke variabel dateTime
    public String getDateTime() {
        return dateTime;
    }

    //Membuat method setDateTime untuk mengembalikan nilai ke variabel dateTime
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    //Membuat method getSubtitle untuk mengembalikan nilai ke variabel subtitle
    public String getSubtitle() {
        return subtitle;
    }

    //Membuat method setSubtitle untuk mengembalikan nilai ke variabel subtitle
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    //Membuat method getTextNote untuk mengembalikan nilai ke variabel textNote
    public String getTextNote() {
        return textNote;
    }

    //Membuat method setTextNote untuk mengembalikan nilai ke variabel textNote
    public void setTextNote(String textNote) {
        this.textNote = textNote;
    }

    //Membuat method getImagePath untuk mengembalikan nilai ke variabel imagePath
    public String getImagePath() {
        return imagePath;
    }

    //Membuat method setImagePath untuk mengembalikan nilai ke variabel imagePath
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    //Membuat method getColor untuk mengembalikan nilai ke variabel color
    public String getColor() {
        return color;
    }

    //Membuat method setColor untuk mengembalikan nilai ke variabel color
    public void setColor(String color) {
        this.color = color;
    }

    //Membuat method getLinkWeb untuk mengembalikan nilai ke variabel linkWeb
    public String getLinkWeb() {
        return linkWeb;
    }

    //Membuat method setLinkWeb untuk untuk mengembalikan nilai ke variabel linkWeb
    public void setLinkWeb(String linkWeb) {
        this.linkWeb = linkWeb;
    }

    //Mmebuat method to String untuk mengembalikan nilai ke variabel title dan dateTime
    @NonNull
    @Override
    public String toString() {
        return title + " : " + dateTime;
    }
}
