package com.example.yunotes.adapter;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yunotes.R;
import com.example.yunotes.entities.Note;
import com.example.yunotes.listeners.NotesListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    //Deklarasi variabel dengan jenis data List
    private List<Note> notes;

    //Deklarasi variabel dengan jenis data NotesListener
    private NotesListener notesListener;

    //Deklarasi variabel dengan jenis data Timer
    private Timer timer;

    //Deklarasi variabel dengan jenis data List
    private List<Note> notesSource;

    //Membuat method NotesAdapter
    public NotesAdapter(List<Note> notes, NotesListener notesListener) {
        this.notes = notes;
        this.notesListener = notesListener;
        notesSource = notes;
    }

    /*Fungsi ini secara otomatis dipanggil ketika tampilan item list siap
    untuk ditampilkan atau akan ditampilkan*/
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                //Mengatur Layout untuk menampilkan item
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_note,
                        parent,
                        false
                )
        );
    }

    //Membuat method constructor onBindViewHolder untuk menampilkan item sesuai position
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        //Set item ke notes dan mengatur position pada list
        holder.setNote(notes.get(position));

        //Membuat event setOnclickListener pada layoutNote
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Membuat event clicked pada notesListener untuk menyesuaikan position note pada list
                notesListener.onNoteClicked(notes.get(position), position);
            }
        });
    }

    //Fungsi getItemCount() mengembalikan jumlah item yang akan ditampilkan dalam list
    @Override
    public int getItemCount() {
        return notes.size();
    }

    /*Fungsi ini digunakan untuk mendapatkan data item yang terkait dengan posisi tertentu
    dalam kumpulan data untuk mendapatkan data yang sesuai dari lokasi tertentu dalam pengumpulan data item.*/
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    //Membuar method untuk menampilkan item pada RecyclerView
    static class NoteViewHolder extends RecyclerView.ViewHolder {

        //Deklarasi variabel dengan tipe data TextView
        TextView tvTitle, tvSubtitle, tvDateTime;

        //Deklarasi variabel dengan tipe data LinearLayout
        LinearLayout layoutNote;

        //Deklarasi variabel dengan tipe data RoundedImageView
        RoundedImageView imageNote;

        //Membuat method untuk mendapatkan data item
        NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            //Menghubungkan variabel tvTitle dengan komponen TextView pada layout
            tvTitle = itemView.findViewById(R.id.tvTitle);

            //Menghubungkan variabel tvSubtitle dengan komponen TextView pada layout
            tvSubtitle = itemView.findViewById(R.id.tvSubtitle);

            //Menghubungkan variabel tetvDateTime dengan komponen TextView pada layout
            tvDateTime = itemView.findViewById(R.id.tvDateTime);

            //Menghubungkan variabel layoutNote dengan komponen LinearLayout pada layout
            layoutNote = itemView.findViewById(R.id.layoutNote);

            //Menghubungkan variabel imageNote dengan komponen ImageView pada layout
            imageNote = itemView.findViewById(R.id.imageNote);
        }

        //Membuat method untuk set pada note
        void setNote(Note note) {

            //Menampilkan value dari variabel NoteTitle ke dalam tvTitle
            tvTitle.setText(note.getTitle());

            //Mengecek apakah TextView tvSubtitle terdaoat isi atau tidak
            if (note.getSubtitle().trim().isEmpty()) {
                //Membuat fungsi view Gone pada tvSubtitle
                tvSubtitle.setVisibility(View.GONE);
            } else {
                //Menampilkan value dari variabel NoteSubtitle ke dalam tvSubtitle
                tvSubtitle.setText(note.getSubtitle());
            }
            //Menampilkan value dari variabel DateTime ke dalam tvDateTime
            tvDateTime.setText(note.getDateTime());

            //Membuat gradientDrawable pada background layout note
            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();

            //Mengecek jika noteColor tidak bernilai null
            if (note.getColor() != null) {
                //Menampilkan value dari Color yang dipilih ke dalam gradientDrawable
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            } else {
                //Menampilkan value dari Color default ke dalam gradientDrawable
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }

            //Mengecek jika noteImage tidak bernilai null
            if (note.getImagePath() != null) {

                //Menampilkan value dari image yang dipilih dari directory ke dalam imageNote
                imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));

                //Membuat fungsi view visible atau terlihat pada imageNote
                imageNote.setVisibility(View.VISIBLE);
            } else {

                //Membuat fungsi view gone atau menghilang pada imageNote jika tidak memasukkan image ke dalam note
                imageNote.setVisibility(View.GONE);
            }
        }
    }

    //Membuat method searchNotes untuk mencari note yang tersedia dan sudah dibuat
    public void searchNotes(final String searchKeyword) {

        //Membuat objek timer untuk menentukan waktu search note sampai note muncul
        timer = new Timer();

        //Membuat fungsi timer menggunakan timerTask untuk memberi waktu hingga note yang di cari muncul
        timer.schedule(new TimerTask() {
            @Override

            //Membuat method run
            public void run() {

                //Mengecek jika searchKeyword atau kata kunci untuk mencari note kosong
                if (searchKeyword.trim().isEmpty()) {
                    //Fungsi untuk tidak menampilkan note yang di cari
                    notes = notesSource;
                } else {
                    ///Memberikan nilai arrayList dari class Note
                    ArrayList<Note> temp = new ArrayList<>();

                    //Membuat fungsi perulangan untuk menampilkan data dari notesSouce
                    for (Note note : notesSource) {

                        //Mengecek searchKeyword yang di masukkan untuk mencari Note
                        /**
                         * Membuat fungsi getTitle untuk memberikan nilai pada searchKeyword jika mencari note dengan memasukkan data dari noteTitle
                         * Membuat fungsi getSubtitle untuk memberikan nilai pada searchKeyword jika mencari note dengan memasukkan data dari noteSubtitle
                         * Membuat fungsi getTextNote untuk memberikan nilai pada searchKeyword jika mencari note dengan memasukkan data dari textNote
                         */
                        if (note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase())
                            || note.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase())
                            || note.getTextNote().toLowerCase().contains(searchKeyword.toLowerCase())) {
                            temp.add(note); //Menambahkan  data yang dimunculkan
                        }
                    }
                    notes = temp;
                }

                // Membuat fungsi handler pada looper untuk mengulangi note yang dicari
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //Membuat fungsi untuk mengubah recyclerView dengan menampilkan data yang baru ketika di cari
                        notifyDataSetChanged();
                    }
                });
            }
            //Membuat fungsi delay waktu 500 saat note di tampilkan
        }, 500);
    }

    //Membuat method untuk membatalkan pencarian note
    public void cancelTimes() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
