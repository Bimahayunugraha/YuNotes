package com.example.yunotes.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yunotes.R;
import com.example.yunotes.database.NotesDB;
import com.example.yunotes.entities.Note;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity {

    //Mendeklarasikan variabel dengan tipe data EditText
    private EditText noteTitle, noteSubtitle, noteText;

    //Mendeklarasikan variabel dengan tipe data TextView
    private TextView txtDateTime;

    //Mendeklarasikan variabel dengan tipe data View
    private View viewSubtitleIndicator;

    //Mendeklarasikan variabel dengan tipe data ImageView
    private ImageView imageNote;

    //Mendeklarasikan variabel dengan tipe data TextVuew
    private TextView textWebURL;

    //Mendeklarasikan variabel dengan tipe data LinearLayout
    private LinearLayout layoutWebURL;

    //Mendeklarasikan variabel dengan tipe data String
    private String selectedNoteColor;
    private String selectedImagePath;

    //Mendeklarasikan kode permintaan untuk meminta izin pada penyimpanan dengan kode permintaan 1
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;

    //Mendeklarasikan kode permintaan untuk memilih gambar dengan kode permintaan 2
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;

    //Mendeklarasikan variabel dengan tipe data AlertDialog
    private AlertDialog dialogAddURL;

    //Mendeklarasikan variabel dengan tipe data AlertDialog
    private AlertDialog dialogDeleteNote;

    //Memanggil class Note pada package entities untuk mengecek note yang tersedia
    private Note alreadyAvailableNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        //Menghubungkan variabel imageBack dengan komponen ImageView pada layout
        ImageView imageBack = findViewById(R.id.imageBack);

        //Membuat event setOnclickListener pada variabel imageBack
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mendeklarasikan fungsi onBackPressed untuk memanggil activity "MainMenuActivity"
                onBackPressed();
            }
        });

        //Menghubungkan variabel noteTitle dengan komponen EditText pada layout
        noteTitle = findViewById(R.id.inputNoteTitle);

        //Menghubungkan variabel noteSubtitle dengan komponen EditText pada layout
        noteSubtitle = findViewById(R.id.inputNoteSubtitle);

        //Menghubungkan variabel noteText dengan komponen EditText pada layout
        noteText = findViewById(R.id.inputNote);

        //Menghubungkan variabel txtDateTime dengan komponen TextView pada layout
        txtDateTime = findViewById(R.id.textDateTime);

        //Menghubungkan variabel viewSubtitleIndicator dengan komponen View pada layout
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator);

        //Menghubungkan variabel imageNote dengan komponen ImageView pada layout
        imageNote = findViewById(R.id.imageNote);

        //Menghubungkan variabel textWebURL dengan komponen TextView pada layout
        textWebURL = findViewById(R.id.textWebURL);

        //Menghubungkan variabel layoutWebURL dengan komponen LinearLayout pada layout
        layoutWebURL = findViewById(R.id.layoutWebURL);

        //Menampilkan value dari variabel dateFormat dengan nilai local ke dalam txDateTime
        txtDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMMM yyyy HH:mm a", Locale.getDefault()).format(new Date())
        );

        //Menghubungkan variabel imageSave dengan komponen ImageView pada layout
        ImageView imageSave = findViewById(R.id.imageSave);

        //Membuat event setOnclickListener pada variabel imageSave
        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Memanggil fungsi saveNote yang sudah dibuat untuk menyimpan note ke dalam room database
                saveNote();
            }
        });

        //Mendeklarasikan default dari NoteColor dengan hex color "#333333"
        selectedNoteColor = "#333333";

        //Mendeklarasikan ImagePath dengan image yang akan dimasukkan ke dalam note
        selectedImagePath = "";

        //Membuat fungsi if dengan Intent dan nilai Boolean untuk melakukan update pada note
        if (getIntent().getBooleanExtra("isViewOrUpdate", false)) {

            //Membuat fungsi dengan memanggil class Note pada package entities
            /* Dengan menggunakan fungsi Serializable untuk mengubah objek note menjadi byte
             * Kemudian data dari note akan disimpan ke dalam room database
             */
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");

            //Memanggil pada fungsi setVieworUpdateNote yang sudah dibuat untuk mengubah atau melihat note
            setViewOrUpdateNote();
        }

        //Menghubungkan variabel imageRemoveWebURL dengan komponen ImageView pada layout
        //Dan membuat event setOnclickListener pada variabel imageRemoveWebURL
        findViewById(R.id.imageRemoveWebURL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Menampilkan value textWebURL dengan nilai null
                textWebURL.setText(null);

                //Set pada layoutWebURL dengan visibility View Gone
                layoutWebURL.setVisibility(View.GONE);
            }
        });

        //Menghubungkan variabel imageRemoveImage dengan komponen ImageView pada layout
        //Dan membuat event setOnclickListener pada variabel imageRemoveImage
        findViewById(R.id.imageRemoveImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fungsi setImageBitmap untuk memberikan nilai bernilai null pada variabel imageNote
                imageNote.setImageBitmap(null);

                //Mengatur view menjadi gone pada variabel imageNote
                imageNote.setVisibility(View.GONE);

                //Menghubungkan variabel imageRemoveImage dengan komponen ImageView pada layout
                //Dan mengatur view menjadi gone pada variabel imageRemoveImage
                findViewById(R.id.imageRemoveImage).setVisibility(View.GONE);

                //Memberilai nilai kosong pada variabel selectedImagePath
                selectedImagePath = "";
            }
        });

        //Mengecek untuk mengembalikan nilai Intent ke "isFromQuickActions" dan bernilai salah
        if (getIntent().getBooleanExtra("isFromQuickActions", false)) {

            //Deklarasi variabel dengan tipe data String
            //Dan mengembalikan nilai dari intent ke variabel type
            String type = getIntent().getStringExtra("quickActionType");

            //Mengecek jika variabel type bernilai tidak sama dengan null
            if (type != null) {

                //Mengecek jika nilai dari variabel type memiliki persamaa dengan variabel image atau tidak
                if (type.equals("image")) {
                    //Method untuk mengembalikan nilai ke variabel selectedImagePath
                    selectedImagePath = getIntent().getStringExtra("imagePath");

                    //Fungsi setImageBitmap untuk memberikan nilai pada variabel imageNote
                    //Dan Fungsi BitmapFactory untuk mencari penyimpanan yang ada di handphone
                    imageNote.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));

                    //Mengatur view menjadi visible atau terlihat pada variabel imageNote
                    imageNote.setVisibility(View.VISIBLE);

                    //Menghubungkan variabel imageRemoveImage dengan komponen ImageView pada layout
                    //Dan mengatur view menjadi visible atau terlihat pada variabel imageRemoveImage
                    findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);

                    //Mengecek jika nilai dari variabel type memiliki persamaa dengan variabel image atau tidak
                } else if (type.equals("URL")) {
                    //Method untuk mengembalikan nilai ke variabel textWebURL
                    textWebURL.setText(getIntent().getStringExtra("URL"));

                    //Mengatur view menjadi visible atau terlihat pada variabel layoutWebURL
                    layoutWebURL.setVisibility(View.VISIBLE);
                }
            }
        }

        //Memanggil method initMiscellaneous
        initMiscellaneous();

        //Memanggil method setSubtitleIndicatorColor
        setSubtitleIndicatorColor();
    }

    //Membuat method untuk memberikan nilai jika ingin melihat atau memperbaharui note
    private void setViewOrUpdateNote() {
        //Memberikan nilai pada variabel noteTitle
        //Dan jika sudah terdapat note title, maka akan dikembalikan nilainya ke variabel Title
        noteTitle.setText(alreadyAvailableNote.getTitle());

        //Memberikan nilai pada variabel noteSubtitle
        //Dan jika sudah terdapat note Subtitle, maka akan dikembalikan nilainya ke variabel Subtitle
        noteSubtitle.setText(alreadyAvailableNote.getSubtitle());

        //Memberikan nilai pada variabel noteText
        //Dan jika sudah terdapat note Text, maka akan dikembalikan nilainya ke variabel Text
        noteText.setText(alreadyAvailableNote.getTextNote());

        //Memberikan nilai pada variabel txtDateTime
        //Dan jika sudah terdapat note DateTime, maka akan dikembalikan nilainya ke variabel DateTime
        txtDateTime.setText(alreadyAvailableNote.getDateTime());

        //Mengecek jika note sudah tersedia gambar akan dikembalikan nilai nya ke variabel imageNote dengan bernilai tidak sama dengan null
        //Dan untuk mengecek jika note yang sudah tersedia gambar bernilai kosong
        if (alreadyAvailableNote.getImagePath() != null && !alreadyAvailableNote.getImagePath().trim().isEmpty()) {

            //Fungsi setImageBitmap untuk memberikan nilai pada variabel imageNote
            //Dan Fungsi BitmapFactory untuk mencari penyimpanan yang ada di handphone dengan mengembalikan nilai ke imageNote
            imageNote.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath()));

            //Mengatur view menjadi visible atau terlihar pada variabel imageNote
            imageNote.setVisibility(View.VISIBLE);

            //Menghubungkan variabel imageRemoveImage dengan komponen ImageView pada layout
            //Dan mengatur view menjadi visible atau terlihat pada variabel imageRemoveImage
            findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);

            //Mengembalilkan nilai ke variabel selectedImagePath jika sudah tersedia gambar pada note
            selectedImagePath = alreadyAvailableNote.getImagePath();
        }

        //Mengecek jika note yang sudah tersedia URL akan dikembalikan nilai nya ke variabel imageNote dengan bernilai tidak sama dengan null
        //Dan untuk mengecek jika note yang sudah tersedia URL bernilai kosong
        if (alreadyAvailableNote.getLinkWeb() != null && !alreadyAvailableNote.getLinkWeb().trim().isEmpty()) {

            //Memberikan nilai pada variabel textWebURL
            //Dan jika sudah terdapat URL, maka akan dikembalikan nilainya ke variabel URL
            textWebURL.setText(alreadyAvailableNote.getLinkWeb());

            //Mengatur view menjadi visible atau terlihat pada variabel layoutWebURL
            layoutWebURL.setVisibility(View.VISIBLE);
        }
    }

    //Membuat method untuk menyimpan note ke dalam database
    private void saveNote() {
        //Mengecek jika variabel noteTitle bernilai kosong
        if (noteTitle.getText().toString().trim().isEmpty()) {

            //Membuat variabel Toast dan menampilkan pesan "Note title can't empty"
            Toast.makeText(this, "Note title can't empty", Toast.LENGTH_LONG).show();
            return;//Mengembalikan nilai

            //Mengecek jika variabel noteSubtitle dan noteText bernilai kosong
        }else if (noteSubtitle.getText().toString().trim().isEmpty()
                || noteText.getText().toString().trim().isEmpty()) {

            //Membuat variabel Toast dan menampilkan pesan "Note can't empty"
            Toast.makeText(this, "Note can't empty", Toast.LENGTH_LONG).show();
            return;//Mengembalikan nilai
        }

        //Membuat objek Note
        final Note note = new Note();

        //Menyimpan input user di EditText noteTitle ke dalam variabel noteTitle
        note.setTitle(noteTitle.getText().toString());

        //Menyimpan input user di noteSubtitlenoteTitle ke dalam variabel noteSubtitle
        note.setSubtitle(noteSubtitle.getText().toString());

        //Menyimpan input user di EditText noteText ke dalam variabel noteText
        note.setTextNote(noteText.getText().toString());

        //Menyimpan input user di TextView txtDateTime ke dalam variabel txtDateTime
        note.setDateTime(txtDateTime.getText().toString());

        //Menyimpan input user di Frame selectedNoteColor ke dalam variabel selectedNoteColor
        note.setColor(selectedNoteColor);

        //Menyimpan input user di ImageView selectedImagePath ke dalam variabel selectedImagePath
        note.setImagePath(selectedImagePath);

        //Mengecek jika variabel layoutWebURL diatur view nya menjadi visible atau terlihat
        if (layoutWebURL.getVisibility() == View.VISIBLE) {
            //Menyimpan input user di EditText textWebURL ke dalam variabel textWebURL
            note.setLinkWeb(textWebURL.getText().toString());
        }

        //Mengecek jika note yang tersedia tidak bernilai null
        if (alreadyAvailableNote != null) {
            //Menyimpan id note di dalam database
            //Dan jika sudah tersedia note, maka akan mengembalikan nilai id tersebut
            note.setId(alreadyAvailableNote.getId());
        }

        //Membuat class SaveNoteTask untuk menyimpan note ke dalam Room Database
        //Disini fungsi AsyncTask adalah agar saat mennyimpan ke dalam Room Database dapat dilakukan dengan baik
        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                //Method untuk memanggil Room Database "NotesDB"
                NotesDB.getDatabase(getApplicationContext()).noteDao().insertNote(note);
                return null;//Mengembalikan nilai dengan bernilai null
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //Membuat method intent
                Intent intent = new Intent();

                //Membuat fungsi hasil menjadi OK pada intent
                setResult(RESULT_OK, intent);

                //Fungsi untuk membuat method selesai beroperasi
                finish();
            }
        }
        //Fungsi untuk menjalankan method SaveNoteTask
        new SaveNoteTask().execute();
    }

    //Membuat method initMiscellaneous
    //Fungsi dari initMiscellaneous untuk menampilkan menu yang disembunyikan dibawah activity
    private void initMiscellaneous() {
        //Menghubungkan variabel layoutMiscellaneous dengan komponen LinearLayout pada layout
        final LinearLayout layoutMiscellaneous = findViewById(R.id.layoutMiscellaneous);

        //Fungsi BottomSheetBehavior sendiri adalah untuk menampilkan menu yang disembunyikan di bawah activity
        //dengan menarik menu ke atas
        //Disini menu pada layoutMiscellaneous akan muncul jika diklik pada imageMiscellaneous
        //ke atas pada class CreateNoteActivity
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);

        //Menghubungkan variabel imageMiscellaneous dengan komponen ImageView pada layout
        //Memberi event setOnClickListener pada imageMiscellaneous
        layoutMiscellaneous.findViewById(R.id.imageMiscellaneous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mengecek saat menu ditarik ke atas akan diperluas dengan cara di klik pada imageMiscellaneous
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    //Memberikan nilai pada variabel bottomSheetBehavior untuk menarik ke atas menu yang disembunyikan
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    //Menu akan di turunkan kembali jika ditarik turun dengan cara di klik kembali pada imageMiscellaneous
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        //Menghubungkan variabel imageColor1 dengan komponen ImageView pada layout
        final ImageView imageColor1 = layoutMiscellaneous.findViewById(R.id.imageColor1);

        //Menghubungkan variabel imageColor2 dengan komponen ImageView pada layout
        final ImageView imageColor2 = layoutMiscellaneous.findViewById(R.id.imageColor2);

        //Menghubungkan variabel imageColor3 dengan komponen ImageView pada layout
        final ImageView imageColor3 = layoutMiscellaneous.findViewById(R.id.imageColor3);

        //Menghubungkan variabel imageColor4 dengan komponen ImageView pada layout
        final ImageView imageColor4 = layoutMiscellaneous.findViewById(R.id.imageColor4);

        //Menghubungkan variabel imageColor5 dengan komponen ImageView pada layout
        final ImageView imageColor5 = layoutMiscellaneous.findViewById(R.id.imageColor5);

        //Menghubungkan variabel imageColor6 dengan komponen ImageView pada layout
        final ImageView imageColor6 = layoutMiscellaneous.findViewById(R.id.imageColor6);

        //Menghubungkan variabel viewColor1 dengan komponen View pada layout
        //Memberi event setOnClickListener viewColor1
        layoutMiscellaneous.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Memberikan nilai color "#333333"
                selectedNoteColor = "#333333";

                //Memberikan nilai icon done atau ceklis jika dipilih pada variabel imageColor1
                imageColor1.setImageResource(R.drawable.ic_done);

                //Memberikan nilai 0 karena variabel imageColor2 tidak dipilih
                imageColor2.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor3 tidak dipilih
                imageColor3.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor4 tidak dipilih
                imageColor4.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor5 tidak dipilih
                imageColor5.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor6 tidak dipilih
                imageColor6.setImageResource(0);

                //Memberikan nilai pada varibel SubtitleIndicatorColor
                setSubtitleIndicatorColor();
            }
        });

        //Menghubungkan variabel viewColor2 dengan komponen View pada layout
        //Memberi event setOnClickListener viewColor2
        layoutMiscellaneous.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Memberikan nilai color "#FDBE3B"
                selectedNoteColor = "#FDBE3B";

                //Memberikan nilai 0 karena variabel imageColor1 tidak dipilih
                imageColor1.setImageResource(0);
                //Memberikan nilai icon done atau ceklis jika dipilih pada variabel imageColor2
                imageColor2.setImageResource(R.drawable.ic_done);

                //Memberikan nilai 0 karena variabel imageColor3 tidak dipilih
                imageColor3.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor4 tidak dipilih
                imageColor4.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor5 tidak dipilih
                imageColor5.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor6 tidak dipilih
                imageColor6.setImageResource(0);

                //Memberikan nilai pada varibel SubtitleIndicatorColor
                setSubtitleIndicatorColor();
            }
        });

        //Menghubungkan variabel viewColor3 dengan komponen View pada layout
        //Memberi event setOnClickListener viewColor3
        layoutMiscellaneous.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Memberikan nilai color "#FF4842"
                selectedNoteColor = "#FF4842";

                //Memberikan nilai 0 karena variabel imageColor1 tidak dipilih
                imageColor1.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor2 tidak dipilih
                imageColor2.setImageResource(0);

                //Memberikan nilai icon done atau ceklis jika dipilih pada variabel imageColor3
                imageColor3.setImageResource(R.drawable.ic_done);

                //Memberikan nilai 0 karena variabel imageColor4 tidak dipilih
                imageColor4.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor5 tidak dipilih
                imageColor5.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor6 tidak dipilih
                imageColor6.setImageResource(0);

                //Memberikan nilai pada varibel SubtitleIndicatorColor
                setSubtitleIndicatorColor();
            }
        });

        //Menghubungkan variabel viewColor4 dengan komponen View pada layout
        //Memberi event setOnClickListener viewColor4
        layoutMiscellaneous.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Memberikan nilai color "#3A52FC"
                selectedNoteColor = "#3A52FC";

                //Memberikan nilai 0 karena variabel imageColor1 tidak dipilih
                imageColor1.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor2 tidak dipilih
                imageColor2.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor3 tidak dipilih
                imageColor3.setImageResource(0);

                //Memberikan nilai icon done atau ceklis jika dipilih pada variabel imageColor4
                imageColor4.setImageResource(R.drawable.ic_done);

                //Memberikan nilai 0 karena variabel imageColor5 tidak dipilih
                imageColor5.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor6 tidak dipilih
                imageColor6.setImageResource(0);

                //Memberikan nilai pada varibel SubtitleIndicatorColor
                setSubtitleIndicatorColor();
            }
        });

        //Menghubungkan variabel viewColor5 dengan komponen View pada layout
        //Memberi event setOnClickListener viewColor5
        layoutMiscellaneous.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Memberikan nilai color "#000000"
                selectedNoteColor = "#000000";

                //Memberikan nilai 0 karena variabel imageColor1 tidak dipilih
                imageColor1.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor2 tidak dipilih
                imageColor2.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor3 tidak dipilih
                imageColor3.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor4 tidak dipilih
                imageColor4.setImageResource(0);

                //Memberikan nilai icon done atau ceklis jika dipilih pada variabel imageColor5
                imageColor5.setImageResource(R.drawable.ic_done);

                //Memberikan nilai 0 karena variabel imageColor6 tidak dipilih
                imageColor6.setImageResource(0);

                //Memberikan nilai pada varibel SubtitleIndicatorColor
                setSubtitleIndicatorColor();
            }
        });

        //Menghubungkan variabel viewColor6 dengan komponen View pada layout
        //Memberi event setOnClickListener viewColor6
        layoutMiscellaneous.findViewById(R.id.viewColor6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Memberikan nilai color "#EAEAEA"
                selectedNoteColor = "#EAEAEA";

                //Memberikan nilai 0 karena variabel imageColor1 tidak dipilih
                imageColor1.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor2 tidak dipilih
                imageColor2.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor3 tidak dipilih
                imageColor3.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor4 tidak dipilih
                imageColor4.setImageResource(0);

                //Memberikan nilai 0 karena variabel imageColor5 tidak dipilih
                imageColor5.setImageResource(0);

                //Memberikan nilai icon done atau ceklis jika dipilih pada variabel imageColor6
                imageColor6.setImageResource(R.drawable.ic_done);

                //Memberikan nilai pada varibel SubtitleIndicatorColor
                setSubtitleIndicatorColor();
            }
        });

        //Mengecek note yang sudah tersedia tidak bernilai null
        //Dan mengecek jika note yang sudah tersedia Color akan dikembalikan nilai nya ke variabel ViewColor dengan bernilai tidak sama dengan null
        //Dan untuk mengecek jika note yang sudah tersedia Color bernilai kosong
        if (alreadyAvailableNote != null && alreadyAvailableNote.getColor() != null && !alreadyAvailableNote.getColor().trim().isEmpty()) {

            //Fungsi untuk mengganti color dan mengambalikan nilai ke color
            switch (alreadyAvailableNote.getColor()) {
                //Membuat case untuk color "#FDBE3B"
                case "#FDBE3B":
                    //Menghubungkan variabel viewColor2 dengan komponen View pada layout
                    layoutMiscellaneous.findViewById(R.id.viewColor2).performClick();
                    break; //Membuat fungsi break untuk membuat case viewColor2 berhenti berulang

                //Membuat case untuk color "#FF4842"
                case "#FF4842":
                    //Menghubungkan variabel viewColor3 dengan komponen View pada layout
                    layoutMiscellaneous.findViewById(R.id.viewColor3).performClick();
                    break; //Membuat fungsi break untuk membuat case viewColor3 berhenti berulang

                //Membuat case untuk color "#3A52FC"
                case "#3A52FC":
                    //Menghubungkan variabel viewColor4 dengan komponen View pada layout
                    layoutMiscellaneous.findViewById(R.id.viewColor4).performClick();
                    break; //Membuat fungsi break untuk membuat case viewColor4 berhenti berulang

                //Membuat case untuk color "#000000"
                case "#000000":
                    //Menghubungkan variabel viewColor5 dengan komponen View pada layout
                    layoutMiscellaneous.findViewById(R.id.viewColor5).performClick();
                    break; //Membuat fungsi break untuk membuat case viewColor5 berhenti berulang

                //Membuat case untuk color "#EAEAEA"
                case "#EAEAEA":
                    //Menghubungkan variabel viewColor6 dengan komponen View pada layout
                    layoutMiscellaneous.findViewById(R.id.viewColor6).performClick();
                    break; //Membuat fungsi break untuk membuat case viewColor6 berhenti berulang

            }
        }

        //Menghubungkan variabel layoutAddImage dengan komponen LinearLayout pada layout
        layoutMiscellaneous.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fungsi saat menu ditarik ke atas akan diperluas dengan cara di klik pada imageMiscellaneous
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                //Mengecek untuk mendapatkan izin ke penyimpanan handphone
                if (ContextCompat.checkSelfPermission(
                        //Method untuk meminta izin mengakses penyimpanan
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE

                        //Mengecek jika izin mengakses penyimpanan diterima
                ) != PackageManager.PERMISSION_GRANTED) {
                    //Fungsi untuk meminta akses ke penyimpanan saat menampilkan note yang sudah dibuat dan telah ditambahkan gambar
                    //pada activity "MainMenuActivity"
                    ActivityCompat.requestPermissions(
                            //Activity
                            CreateNoteActivity.this,
                            //Method untuk meminta izin mengakses penyimpanan
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION//Kode permintaan untuk meminta izin mengakses penyimpanan
                    );
                } else {
                    //Memanggil method selectImage untuk memilih gambar dari penyimpanan
                    selectImage();
                }
            }
        });

        //Menghubungkan variabel imageAddUrl dengan komponen ImageView pada layout
        layoutMiscellaneous.findViewById(R.id.layoutAddUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Fungsi saat menu ditarik ke atas akan diperluas dengan cara di klik pada imageMiscellaneous
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                //Memanggil method showAddURLDialog untuk memilih gambar dari penyimpanan
                showAddURLDialog();
            }
        });

        //Mengecek jika note yang tersedia tidak bernilai null
        if(alreadyAvailableNote != null) {
            //Menghubungkan variabel layoutDeleteNote dengan komponen LinearLayout pada layout
            //Dan mengatur view menjadi visible atau terlihat pada variabel layoutDeleteNote
            layoutMiscellaneous.findViewById(R.id.layoutDeleteNote).setVisibility(View.VISIBLE);

            //Menghubungkan variabel layoutDeleteNote dengan komponen LinearLayout pada layout
            //Memberi event setOnClickListener pada layoutDeleteNote
            layoutMiscellaneous.findViewById(R.id.layoutDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Fungsi saat menu ditarik ke atas akan diperluas dengan cara di klik pada imageMiscellaneous
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                    //Memanggil method showDeleteNoteDialog
                    showDeleteNoteDialog();
                }
            });
        }
    }

    //Membuat method showDeleteNoteDialog untuk menampilkan dialog saat user ingin menghapus note
    private void showDeleteNoteDialog() {

        //Mengecek jika variabel dialogDeleteNote bernilai null
        if (dialogDeleteNote == null) {

            //Membuat Fungsi AlertDialog saat user ingin menghapus note
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);

            //Mengatur Layout untuk menampilkan item
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_note,
                    (ViewGroup) findViewById(R.id.layoutAddUrlContainer)
            );
            //Menampilkan data View
            builder.setView(view);

            //Memberi nilai dialogDeleteNote untuk membuat dialog
            dialogDeleteNote = builder.create();

            //Mengecek jika variabel dialogDeleteNote tidak bernilai null
            if (dialogDeleteNote.getWindow() != null) {
                //Fungsi getWindow untuk mengembalikan data ke dialogDeleteNote
                //Dan menampilkan data pada background
                dialogDeleteNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            //Menghubungkan variabel textDeleteNote dengan komponen TextView pada layout
            //Dan membuat event setOnClickListener pada variabel textDeleteNote
            view.findViewById(R.id.textDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    @SuppressLint("StaticFieldLeak")
                    class DeleteNoteTask extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {

                            //Method untuk memanggil Room Database "NotesDB"
                            NotesDB.getDatabase(getApplicationContext()).noteDao()
                                    .deleteNote(alreadyAvailableNote);
                            return null;//Mengembalikan nilai dengan bernilai null
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            //Membuat method intent
                            Intent intent = new Intent();

                            //Memberikan nilai pada "isNoteDeleted" dan bernilai true
                            intent.putExtra("isNoteDeleted", true);


                            //Membuat fungsi hasil menjadi OK pada intent
                            setResult(RESULT_OK, intent);

                            //Fungsi untuk membuat method selesai beroperasi
                            finish();
                        }
                    }
                    //Fungsi untuk menjalankan method SaveNoteTask
                    new DeleteNoteTask().execute();
                }
            });

            //Menghubungkan variabel textCancel dengan komponen TextView pada layout
            //Dan membuat event setOnClickListener pada variabel textCancel
            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Fungsi untuk menutup dialogDeleteNote
                    dialogDeleteNote.dismiss();
                }
            });
        }
        //Fungsi untuk menampilkan dialogDeleteNote
        dialogDeleteNote.show();
    }

    //Membuat method untuk mengatur color pada SubtitleIndicatorColor
    private void setSubtitleIndicatorColor(){
        //Deklarasikan GradientDrawable untuk menampilkan color pada variabel viewSubtitleIndicator
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        //Memberikan color saat color dipilih
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }

    //Membuat method untuk memilih gambar
    @SuppressLint("QueryPermissionsNeeded")
    private void selectImage() {
        //Method untuk memanggil aksi pilih gambar dari media penyimpanan handphone
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        //Mengecek jika nilai tidak sama dengan null
        if (intent.resolveActivity(getPackageManager()) != null) {

            //Fungsi untuk memulai activity
            //Dan Kode permintaan untuk meminta izin memilih gambar
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
    }

    //Fungsi ini akan di panggil jika permintaan akses penyimpanan berhasil
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Mengecek jika memiliki kode permintaan untuk mengakses penyimpanan dan mempunyai hasil lebih dari 0
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {

            //Mengecek jika result adalah 0, maka akses di izinkan
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Memanggil method selectImage untuk memilih gambar dari penyimpanan
                selectImage();
            } else {
                //Membuat variabel Toast dan menampilkan pesan "Permission Denied!"
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Method untuk hasil dari activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Mengecek jika memiliki kode permintaan untuk memilih gambar dan set hasil menjadi OK
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            //Mengecek jika data tidak bernilai null
            if (data != null) {
                //Mengembalikan data ke ImageUri
                Uri selectedImageUri = data.getData();

                //Mengecek jika imageUri tidak bernilai null
                if (selectedImageUri != null) {
                    try {
                        //Fungsi inputStream untuk memilih penyimpanan yang ada di handphone
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);

                        //Dan Fungsi BitmapFactory untuk mencari penyimpanan yang ada di handphone
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        //Memberikan nilai ke variabel imageNote
                        imageNote.setImageBitmap(bitmap);

                        //Mengatur view menjadi visible atau terlihat pada variabel imageNote
                        imageNote.setVisibility(View.VISIBLE);

                        //Menghubungkan variabel imageRemoveImage dengan komponen ImageView pada layout
                        //Dan mengatur view menjadi visible atau terlihat pada variabel imageRemoveImage
                        findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);

                        //Mengembalikan nilai dari Uri ke variabel  selectedImagePath
                        selectedImagePath = getPathFromUri(selectedImageUri);
                    }catch (Exception exception) {

                        //Membuat variabel Toast dan mengembalikan nilai dari pesan ketika ada eror
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    //Fungsi untuk mengembalikan nilai dari Uri
    private String getPathFromUri(Uri contentUri) {

        //Deklarasi variabel dengan jenis data String
        String filePath;

        //Mengatur cursor untuk mengembalikan nilai dari Content Resolver
        Cursor cursor = getContentResolver()
                .query(contentUri, null, null, null,null);

        //Mengecek jika variabel cursor bernilai null
        if (cursor == null) {

            //Mengatur filePath untuk mengembalikan nilai dari Path
            filePath = contentUri.getPath();
        } else {

            //Mengatur cursor untuk bergerak
            cursor.moveToFirst();

            //Menampilkan nilai index dengan mengembalikan nilai dari ColumnIndex "_data"
            int index = cursor.getColumnIndex("_data");

            //Mengembalikan nilai ke dalam filePath
            filePath = cursor.getString(index);

            //Fungsi untuk menutup cursor
            cursor.close();
        }
        //Mengembalikan data ke filePath
        return filePath;
    }

    //Membuat method untuk menampilkan dialog menambahkan URL
    private void showAddURLDialog() {
        //Mengecek jika variabel dialogAddURL bernilai null
        if (dialogAddURL == null) {
            //Membuat Fungsi AlertDialog saat ingin menambahkan URL
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);

            //Mengatur Layout untuk menampilkan item
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_add_url,
                    (ViewGroup) findViewById(R.id.layoutAddUrlContainer)
            );

            //Menampilkan data View
            builder.setView(view);

            //Memberi nilai dialogAddURl untuk membuat dialog
            dialogAddURL = builder.create();

            //Mengecek jika variabel dialogAddURL tidak bernilai null
            if (dialogAddURL.getWindow() != null) {
                //Fungsi getWindow untuk mengembalikan data ke dialogAddURL
                //Dan menampilkan data pada background
                dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            //Menghubungkan variabel inputURL dengan komponen EditText pada layout
            final EditText inputURL = view.findViewById(R.id.inputURL);

            //Mengatur focus pada EditText inputURL
            inputURL.requestFocus();

            //Menghubungkan variabel textAdd dengan komponen TextView pada layout
            //Dan membuat event setOnClickListener pada variabel textAdd
            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Mengecek jika saat memasukkan URL bernilai kosong
                    if (inputURL.getText().toString().trim().isEmpty()) {
                        //Membuat variabel Toast dan menampilkan pesan "Enter URL"
                        Toast.makeText(CreateNoteActivity.this, "Enter URL", Toast.LENGTH_SHORT).show();

                        //Mengecek jika URL yang dimasukkan tidak sesuai dengan URL WEb
                    } else if (!Patterns.WEB_URL.matcher(inputURL.getText().toString()).matches()) {
                        //Membuat variabel Toast dan menampilkan pesan "Enter valid URL"
                        Toast.makeText(CreateNoteActivity.this, "Enter valid URL", Toast.LENGTH_SHORT).show();

                        //Mengecek jika memasukkan URL dengan benar
                    } else {

                        //Menyimpan input user di EditText textWebURL ke dalam variabel  textWebURL
                        textWebURL.setText(inputURL.getText().toString());

                        //Mengatur view menjadi visible atau terlihat pada variabel layoutWebURL
                        layoutWebURL.setVisibility(View.VISIBLE);

                        //Fungsi untuk menutup dialogAddURL
                        dialogAddURL.dismiss();
                    }
                }
            });

            //Menghubungkan variabel textCancel dengan komponen TextView pada layout
            //Dan membuat event setOnClickListener pada variabel textCancel
            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Fungsi untuk menutup dialogAddURL
                    dialogAddURL.dismiss();
                }
            });
        }
        //Fungsi untuk menampilkan dialogAddURL
        dialogAddURL.show();
    }
}