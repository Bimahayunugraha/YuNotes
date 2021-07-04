package com.example.yunotes.listeners;

import com.example.yunotes.entities.Note;

public interface NotesListener {

    void onNoteClicked(Note note, int position);
}
