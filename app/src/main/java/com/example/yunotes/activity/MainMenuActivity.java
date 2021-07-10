package com.example.yunotes.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yunotes.R;
import com.example.yunotes.adapter.NotesAdapter;
import com.example.yunotes.database.NotesDB;
import com.example.yunotes.entities.Note;
import com.example.yunotes.listeners.NotesListener;

import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity implements NotesListener {

    //Mendeklarasikan kode permintaan untuk menambahkan note dengan kode permintaan 1
    public static final int REQUEST_CODE_ADD_NOTE = 1;

    //Mendeklarasikan kode permintaan untuk memperbaharui note dengan kode permintaan 2
    public static final int REQUEST_CODE_UPDATE_NOTE = 2;

    //Mendeklarasikan kode permintaan untuk menampilkan note dengan kode permintaan 3
    public static final int REQUEST_CODE_SHOW_NOTES = 3;

    //Mendeklarasikan kode permintaan untuk memilih gambar dengan kode permintaan 4
    public static final int REQUEST_CODE_SELECT_IMAGE = 4;

    //Mendeklarasikan kode permintaan untuk meminta izin pada penyimpanan dengan kode permintaan 5
    public static final int REQUEST_CODE_STORAGE_PERMISSION = 5;

    //Mendeklarasikan variabel dengan tipe data RecyclerView
    private RecyclerView notesRecyclerView;

    //Mendeklarasikan variabel dengan tipe data List
    private List<Note> noteList;

    //Membuat objek NotesAdapter
    private NotesAdapter notesAdapter;

    //Membuat objecy noteCLickedPosition dengan nilai -1
    private int noteCLickedPosition = -1;

    //Mendeklarasikan variabel dengan tipe data AlertDialog
    private AlertDialog dialogAddURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Menghubungkan variabel imageAddNoteMain dengan komponen ImageView pada layout
        ImageView imageAddNoteMain = findViewById(R.id.imageAddNoteMain);

        //Menghubungkan variabel imageProfile dengan komponen ImageView pada layout
        ImageView imageProfile = findViewById(R.id.imageProfile);

        //Membuat event setOnClickListener pada variabel imageAddNoteMain
        imageAddNoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fungsi untuk memulai activity
                startActivityForResult(
                        //Method untuk memanggil activity "CreateNoteActivity"
                        new Intent(getApplicationContext(), CreateNoteActivity.class),
                        REQUEST_CODE_ADD_NOTE //Kode permintaan untuk menambahkan note baru
                );
            }
        });

        //Membuat event setOnClickListener pada variabel imageProfile
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Method untuk memanggil activity "Profile"
                Intent intent = new Intent(getApplicationContext(), Profile.class);

                //Fungsi untuk memulai activity
                startActivity(intent);
            }
        });

        //Menghubungkan variabel notesRecyclerView dengan komponen RecyclerView pada layout
        notesRecyclerView = findViewById(R.id.notesRecyclerView);


        //Mengatur data pada variabel notesRecyclerView untuk di tampilkan dalam keadaan vertical
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        //Membuat onjek dari class noteList menjadi arrayList
        noteList = new ArrayList<>();

        //Menberikan nilai pada variabel noteList dengan memanggil notesAdapter
        notesAdapter = new NotesAdapter(noteList, this);

        //Binds adapter ke RecyclerView
        notesRecyclerView.setAdapter(notesAdapter);

        //Mengembalikan nilai dari Notes untuk memberikan fungsi false saat mengirim kode permintaan untuk menampilkan note
        getNotes(REQUEST_CODE_SHOW_NOTES, false);

        //Menghubungkan variabel inputSearch dengan komponen EditText pada layout
        EditText inputSearch = findViewById(R.id.inputSearch);

        //Membuat event addTextChangedListener pada variabel inputSearch
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Membuat objeck untuk membatalkan mengganti saat memasukkan keywordSearch
                notesAdapter.cancelTimes();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Mengecek apakah nilai dari list note tidak sama dengan o
                if (noteList.size() != 0) {
                    //Menampilkan hasil dari pencarian note
                    notesAdapter.searchNotes(s.toString());
                }
            }
        });

        //Menghubungkan variabel imageAddNote dengan komponen ImageView pada layout
        findViewById(R.id.imageAddNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fungsi untuk memulai activity
                startActivityForResult(
                        //Method untuk memanggil activity "CreateNoteActivity"
                        new Intent(getApplicationContext(), CreateNoteActivity.class),
                        REQUEST_CODE_ADD_NOTE //Kode permintaan untuk menambahkan note baru
                );
            }
        });

        //Menghubungkan variabel imageAddImage dengan komponen ImageView pada layout
        findViewById(R.id.imageAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mengecek untuk mendapatkan izin ke penyimpanan handphone saat ingin menambah gambar
                if (ContextCompat.checkSelfPermission(
                        //Method untuk meminta izin mengakses penyimpanan
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                  //Mengecek jika izin mengakses penyimpanan diterima
                ) != PackageManager.PERMISSION_GRANTED) {
                    //Fungsi untuk meminta akses saat menampilkan note yang sudah dibuat dan telah ditambahkan gambar
                    //pada activity "MainMenuActivity"
                    ActivityCompat.requestPermissions(
                            //Activity
                            MainMenuActivity.this,
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
        findViewById(R.id.imageAddUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Memanggil method showAddURLDialog untuk memilih gambar dari penyimpanan
                showAddURLDialog();
            }
        });
    }

    //Membuat method untuk memilih gambar saat akses ke penyimpanan di izinkan
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

    //Membuat method dengan event click pada Note
    @Override
    public void onNoteClicked(Note note, int position) {

        //Mengatur posisi note saat di klik
        noteCLickedPosition = position;

        //Method untuk memanggil activity "CreateNoteActivity"
        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);

        //Memberikan nilai pada"isViewOrUpdate" dan bernilai benar
        intent.putExtra("isViewOrUpdate", true);

        //Memberikan nilai pada "note" dengan mengambil data dari variabel note
        intent.putExtra("note", note);

        //Fungsi untuk memulai activity
        //Dan kode permintaan untuk meminta izin memperbaharui note
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
    }

    //Method getNotes untuk mengembalikan dari note
    private void getNotes(final int requestCode, final boolean isNoteDeleted) {
        @SuppressLint("StaticFieldLeak")

        //Fungsi untuk mengembalikan nilai dan menyimpan nilai pada Room Database
        class GetNotesTask extends AsyncTask<Void, Void, List<Note>> {
            @Override
            //Mengatur list data
            protected List<Note> doInBackground(Void... voids) {

                //Method untuk memanggil database dan dao
                return NotesDB
                        .getDatabase(getApplicationContext())//Method untuk memanggil database
                        .noteDao().getAllNotes();//Method untuk memanggil dao dan menambahkan semua element ke database
            }

            //Method untuk mengatur list data dari note
            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                //Mengecek kode permintaan untuk menampilkan note
                if (requestCode == REQUEST_CODE_SHOW_NOTES) {
                    //Menambahkan semua element note ke LisView
                    noteList.addAll(notes);

                    //Binds adapter
                    notesAdapter.notifyDataSetChanged();

                    //Mengecek kode permintaan untuk menambahkan note
                } else if (requestCode == REQUEST_CODE_ADD_NOTE) {

                    //Menambahkan element dengan index 0 ke ListView
                    noteList.add(0, notes.get(0));

                    //Binds adapter dengan position 0
                    notesAdapter.notifyItemInserted(0);

                    //Mengatur RecyclerView agar saat di scrool akan berjalan smooth
                    notesRecyclerView.smoothScrollToPosition(0);

                    //Mengecek kode permintaan untuk memperbaharui note
                } else if (requestCode == REQUEST_CODE_UPDATE_NOTE) {

                    //Fungsi untuk menghapus note dari LisView
                    noteList.remove(noteCLickedPosition);

                    //Mengecek jika note sudah dihapus
                    if (isNoteDeleted) {

                        //Fungsi untuk binds adapter saat note dihapus
                        notesAdapter.notifyItemRemoved(noteCLickedPosition);
                    } else {
                        //Fungsi untuk menambah note ke LisView dan mengembalikan nilai dari notePosition
                        noteList.add(noteCLickedPosition, notes.get(noteCLickedPosition));

                        //Fungsi untuk binds adapter jika ada perubahan position
                        notesAdapter.notifyItemChanged(noteCLickedPosition);
                    }
                }
            }
        }

        //Fungsi GetNotesTask untuk mengembalikan nilai dari note
        new GetNotesTask().execute();
    }

    //Method untuk hasil dari activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Mengecek jika memiliki kode permintaan untuk mengakses penyimpanan dan set hasil menjadi OK
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            //Fungsi getNotes untuk mengembalikan nilai dari kode permintaan untuk menambahkan note
            //Dan fungsi jika note dihapus, maka bernilai salah
            getNotes(REQUEST_CODE_ADD_NOTE, false);

            //Mengecek jika memiliki kode permintaan untuk memperbaharui note dan set hasil menjadi OK
        } else if (requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK) {
            //Mengecek jika data tidak bernilai null
            if (data != null) {
                //Fungsi getNotes untuk mengembalikan nilai dari kode permintaan memperbaharui note
                //Dan fungsi mengembalikan data jika note dihapus, maka bernilai salah
                getNotes(REQUEST_CODE_UPDATE_NOTE, data.getBooleanExtra("isNoteDeleted", false));
            }
            //Mengecek jika memiliki kode permintaan untuk memilih gambar dan set hasil menjadi OK
        } else if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            //Mengecek jika data tidak bernilai null
            if (data != null) {
                //Mengembalikan data ke ImageUri
                Uri selectedImageUri = data.getData();

                //Mengecek jika imageUri tidak bernilai null
                if (selectedImageUri != null) {
                    try {
                        //Deklarasi variabel dengan tipe data String
                        //Dan mengembalikan nilai dari PathFrom uri ke imagePath
                        String selectedImagePath = getPathFromUri(selectedImageUri);

                        //Method untuk memanggil activity "CreateNoteActivity"
                        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);

                        //Memberikan nilai pada "isFromQuickActions" dan bernilai benar
                        intent.putExtra("isFromQuickActions", true);

                        //Memberikan nilai pada "isFromQuickActions" dan bernilai image
                        intent.putExtra("quickActionType", "image");

                        //Memberikan nilai pada "imagePath" dengan mengambil data dari variabel ImagePath
                        intent.putExtra("imagePath", selectedImagePath);

                        //Fungsi untuk memulai activity
                        //Dan kode permintaan untuk menambahkan note
                        startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
                    } catch (Exception exception) {
                        //Membuat variabel Toast dan mengembalikan nilai dari pesan ketika ada eror
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    //Membuat method untuk menampilkan dialog menambahkan URL
    private void showAddURLDialog() {
        //Mengecek jika variabel dialogAddURL bernilai null
        if (dialogAddURL == null) {
            //Membuat Fungsi AlertDialog saat ingin menambahkan URL
            AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this);

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
                        Toast.makeText(MainMenuActivity.this, "Enter URL", Toast.LENGTH_SHORT).show();

                        //Mengecek jika URL yang dimasukkan tidak sesuai dengan URL WEb
                    } else if (!Patterns.WEB_URL.matcher(inputURL.getText().toString()).matches()) {
                        //Membuat variabel Toast dan menampilkan pesan "Enter valid URL"
                        Toast.makeText(MainMenuActivity.this, "Enter valid URL", Toast.LENGTH_SHORT).show();

                        //Mengecek jika memasukkan URL dengan benar
                    } else {
                        //Fungsi untuk menutup dialogAddURL
                        dialogAddURL.dismiss();

                        //Method untuk memanggil activity "CreateNoteActivity"
                        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);

                        //Memberikan nilai pada "isFromQuickActions" dan bernilai benar
                        intent.putExtra("isFromQuickActions", true);

                        //Memberikan nilai pada "quickActionType" dan bernilai URL
                        intent.putExtra("quickActionType", "URL");

                        //Memberikan nilai pada "isFromQuickActions" dan mengembalikan nilai dari variabel inputURL
                        intent.putExtra("URL", inputURL.getText().toString());

                        //Fungsi untuk memulai activity
                        //Dan kode permintaan untuk menambahkan note jika URL yang dimasukkan benar
                        startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
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