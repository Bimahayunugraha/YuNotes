package com.example.yunotes.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.yunotes.dao.NoteDao;
import com.example.yunotes.entities.Note;

//Membuat koneksi untuk menghubungkan ke database pada package entities dengan class Note
@Database(entities = Note.class, version = 1, exportSchema = false)

//Membuat database "NotesDB" dengan menggunakan RoomDatabase
public abstract class NotesDB extends RoomDatabase {

    //Mendeklarasikan database NotesDB
    private static NotesDB notesDB;

    //Membuat fungsi untuk mengembalikan variabel notesDB pada database
    public static synchronized NotesDB getDatabase(Context context) {
        if (notesDB == null) {
            notesDB = Room.databaseBuilder(
                    context,
                    NotesDB.class,
                    "notes_db"
            ).build();
        }
        return notesDB;
    }

    //Untuk memanggil komponen database NoteDao pada package dao ke dalam NotesDB
    public abstract NoteDao noteDao();
}
