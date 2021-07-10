package com.example.yunotes.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.yunotes.entities.Note;

import java.util.List;

@Dao
public interface NoteDao {

    //Memanggil tabel "notes" dari database Note dengan mengurutkan data yang akan ditampilkan menurut id
    @Query("SELECT * FROM notes ORDER BY id DESC")

    //Menyimpan note di dalam listNote
    List<Note> getAllNotes();

    //Membuat fungsi insert note ke dalam database Note pada package entities
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    //Mmebuat fungsi delete note dar database Note pada package entities
    @Delete
    void deleteNote(Note note);
}
