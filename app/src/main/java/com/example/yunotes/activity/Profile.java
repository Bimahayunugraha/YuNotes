package com.example.yunotes.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yunotes.R;
import com.example.yunotes.adapter.UserAdapter;
import com.example.yunotes.database.DatabaseHelper;
import com.example.yunotes.database.User;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    //Deklarasi activity
    private Activity activity = Profile.this;

    //Deklarasi variabel dengan jenis data TextView
    private TextView tvName;

    //Deklarasi variabel dengan jenis data ImageView
    private ImageView back;

    //Deklarasi variabel dengan jenis data List
    private List<User> listUsers;

    //Deklarasi variabel dengan jenis data RecyclerView
    private RecyclerView recyclerViewUsers;

    //Membuat objek UserAdapter
    private UserAdapter userAdapter;

    //Membuat objek DatabaseHelper
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Menghubungkan variabel tvName dengan komponen TextView pada layout
        tvName = (TextView) findViewById(R.id.tvName);

        //Menghubungkan variabel tvback dengan komponen ImageView pada layout
        back = findViewById(R.id.back);

        //Menghubungkan variabel recyclerViewUsers dengan komponen RecyclerView pada layout
        recyclerViewUsers = (RecyclerView) findViewById(R.id.userRecyclerView);

        //Membuat event setOnclickListener pada ImageView variabel Back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Method untuk memanggil activity "MainActivity"
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);

                //Memulai intent
                startActivity(intent);
            }
        });

        //Memberikan nilai arrayList
        listUsers = new ArrayList<>();

        //Membuat objek dari userAdapter
        userAdapter = new UserAdapter(listUsers);

        //Method untuk mengatur recyclerView pada layout
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        //Menampilkan value dari variabel LayoutManager ke dalam recyclerViewUsers
        recyclerViewUsers.setLayoutManager(mLayoutManager);

        //Menampilkan value dari variabel ItemAnimator ke dalam recyclerViewUsers
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());

        //Memberikan size nilai pada recyclerViewUsers
        recyclerViewUsers.setHasFixedSize(true);

        //Binds adapter ke RecyclerView
        recyclerViewUsers.setAdapter(userAdapter);

        //Membuat objeck dari databaseHelper
        databaseHelper = new DatabaseHelper(activity);

        //Memanggil nilai dari Email untuk ditampilkan datanya
        String emailFromIntent = getIntent().getStringExtra("EMAIL");

        //Memanggil method getDataFromSQLite
        getDataFromSQLite();

        //Memanggil method initMiscellaneousProfile
        initMiscellaneousProfile();
    }

    //Membuat method getDataFromSQLite
    private void getDataFromSQLite() {

        //Membuat fungsi AsyncTask untuk menampilkan data dari database
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                //Fungsi untuk membersihkan list data
                listUsers.clear();

                //Menambahkan semua elemen ke list
                listUsers.addAll(databaseHelper.getAllUser());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //memanggil userAdapter
                userAdapter.notifyDataSetChanged();
            }

        }.execute();
    }

    //Membuat method untuk fungsi miscellaneous di clas Profile
    private void initMiscellaneousProfile() {
        //Menghubungkan variabel layoutMiscellaneousProfile dengan komponen LinearLayout pada layout
        final LinearLayout layoutMiscellaneous = findViewById(R.id.layoutMiscellaneousProfile);

        //Fungsi BottomSheetBehavior sendiri adalah untuk menampilkan menu yang disembunyikan di bawah activity
        //dengan menarik menu ke atas
        //Disini menu pada layoutMiscellaneous akan muncul jika diklik pada imageMiscellaneousProfile
        //ke atas pada class Profile
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);

        //Menghubungkan variabel imageMiscellaneousProfile dengan komponen ImageView pada layout
        //Memberi event setOnClickListener pada imageMiscellaneousProfile
        layoutMiscellaneous.findViewById(R.id.imageMiscellaneousProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mengecek saat menu ditarik ke atas akan diperluas dengan cara di klik pada imageMiscellaneousProfile
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    //Memberikan nilai pada variabel bottomSheetBehavior untuk menarik ke atas menu yang disembunyikan
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    //Menu akan di turunkan kembali jika ditarik turun dengan cara di klik kembali pada imageMiscellaneousProfile
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        //Menghubungkan variabel layoutLogout dengan komponen LinearLayout pada layout
        //Memberi event setOnClickListener pada layoutLogout
        layoutMiscellaneous.findViewById(R.id.layoutLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Membuat fungsi bottomSheetBehavior untuk menampilkan menu yang disembunyikan di bawah activity
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                //Membuat Fungsi AlertDialog saat ingin melakukan logout
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());

                //Menampilkan judul pesan "Do you want to logout?"
                alertDialog.setTitle("Do you want to logout?");

                //Menampilkan pesan alertDialog "click 'Yes' to logout"
                alertDialog.setMessage("Click 'Yes' to logout")
                        //Membuat value Cancelable untuk fungsi saat di klik 'Yes' tidak akan batal
                        .setCancelable(false)

                        //Membuat AlertDialog "Iya" jika ingin melanjutkan untuk logout
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //Membuat variabel Toast dan menampilkan pesan "Logout Success"
                                Toast.makeText(view.getContext(), "Logout Success", Toast.LENGTH_LONG).show();

                                //Method untuk memanggil activity "MainActivity"
                                Intent intent1 = new Intent(view.getContext(), MainActivity.class);

                                //Fungsi untuk memulai activity
                                view.getContext().startActivity(intent1);
                            }
                        })
                        //Membuat AlertDialog "Tidak" jika ingin membatalkan logout
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Membuat dialog cancel untuk logout
                                dialog.cancel();
                            }
                        });
                //Membuat alertDIalog
                AlertDialog alertDialog1 = alertDialog.create();

                //Memunculkan AlertDialog
                alertDialog1.show();
            }
        });
    }

}