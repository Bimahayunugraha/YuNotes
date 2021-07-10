package com.example.yunotes.listeners;

import com.example.yunotes.entities.Note;

public interface NotesListener {

    //Membuat event Listenener Note CLicked dengan class Note dan menentukan position pada note
    void onNoteClicked(Note note, int position);
}
