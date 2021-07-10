package com.example.yunotes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yunotes.R;
import com.example.yunotes.database.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;


public class EditAccount extends AppCompatActivity {

    //Deklarasi variabel dengan jenis data TextInputEdiText
    private TextInputEditText editName, editEmail, editPassword;

    //Deklarasi variabel dengan jenis data Button
    private Button btnEdit;

    //Menghubungkan class dengan database
    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    //Deklarasi variabel dengan jenis data String
    private String id, nm, email, pw;

    //Deklarasi variabel dengan jenis data ImageView
    private ImageView backImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        //Menghubungkan variabel editName dengan komponen TextInputEditText pada layout
        editName = findViewById(R.id.txtEditName);

        //Menghubungkan variabel editEmail dengan komponen TextInputEditText pada layout
        editEmail = findViewById(R.id.txtEditEmail);

        //Menghubungkan variabel editPassword dengan komponen TextInputEditText pada layout
        editPassword = findViewById(R.id.txtEditPassword);

        //Menghubungkan variabel btnEdit dengan komponen Button pada layout
        btnEdit = findViewById(R.id.btnEdit);

        //Menghubungkan variabel backImage dengan komponen ImageView pada layout
        backImage = findViewById(R.id.backImage);

        //Membuat fungsi untuk mengembalikan nilai dari database column user_id ke dalam variabel id
        id = getIntent().getStringExtra("user_id");

        //Membuat fungsi untuk mengembalikan nilai dari database column user_name ke dalam variabel nm
        nm = getIntent().getStringExtra("user_name");

        //Membuat fungsi untuk mengembalikan nilai dari database column user_email ke dalam variabel email
        email = getIntent().getStringExtra("user_email");

        //Membuat fungsi untuk mengembalikan nilai dari database column user_password ke dalam variabel password
        pw = getIntent().getStringExtra("user_password");

        //Memasukkan value ke dalam variabel editName
        editName.setText(nm);

        //Memasukkan value ke dalam variabel editEmail
        editEmail.setText(email);

        //Memasukkan value ke dalam variabel editPassword
        editPassword.setText(pw);

        //Membuat event setOnClickListener pada btnEdit
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Mengecek jika data yang ingin di edit ada yang kosong
                if(editName.getText().toString().equals("")||editEmail.getText().toString().equals("") || editPassword.getText().toString().equals("")) {
                    //Membuat variabel Toast dan menampilkan pesan "Data Note Complete"
                    Toast.makeText(getApplicationContext(), "Data Not Complete", Toast.LENGTH_SHORT).show();
                } else {
                    //Mengembalikan nilai dari variabel nm ke variabel dalam editName
                    nm = editName.getText().toString();

                    //Mengembalikan nilai dari variabel email ke variabel dalam editEmail
                    email = editEmail.getText().toString();

                    //Mengembalikan nilai dari variabel password ke variabel dalam editPassword
                    pw = editPassword.getText().toString();

                    //Membuat objek HashMap dari user
                    HashMap<String,String> user =  new HashMap<>();

                    //Memberikan value dari column user_id ke dalam variabel id
                    user.put("user_id", id);

                    //Memberikan value dari column user_name ke dalam variabel nm
                    user.put("user_name", nm);

                    //Memberikan value dari column user_email ke dalam variabel email
                    user.put("user_email", email);

                    //Memberikan value dari column user_password ke dalam variabel pw
                    user.put("user_password", pw);

                    //Membuat variabel Toast dan menampilkan pesan "Update Account Success"
                    Toast.makeText(getApplicationContext(),"Update Account Success", Toast.LENGTH_LONG).show();

                    //Memanggil method updateUser yang ada pada class DatabaseHelper di package database
                    databaseHelper.updateUser(user);

                    //Memanggil method callLogin yang dibuat
                    callLogin();
                }
            }
        });

        //Membuat event setOnClickListener pada variabel backImage
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Method untuk memanggil activity "Profile"
                Intent intent = new Intent(getApplicationContext(), Profile.class);

                //Fungsi untuk memulai activity
                startActivity(intent);
            }
        });
    }

    //Membuat method untuk menuju ke halaman Profile jika sudah berhasil melakukan updateAccount
    public void callLogin(){
        //Method untuk memanggil activity "Profile"
        Intent intent = new Intent(EditAccount.this,Profile.class);

        //Fungsi untuk memulai activity
        startActivity(intent);
    }
}