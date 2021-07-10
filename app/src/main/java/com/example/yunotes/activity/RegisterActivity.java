package com.example.yunotes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yunotes.R;
import com.example.yunotes.database.DatabaseHelper;
import com.example.yunotes.database.User;
import com.example.yunotes.helper.InputValidation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    //Deklarasi activity
    private final Activity activity = RegisterActivity.this;

    //Deklarasi variabel dengan jenis data TextInputLayout
    private TextInputLayout textInputLayoutName;

    //Deklarasi variabel dengan jenis data TextInputLayout
    private TextInputLayout textInputLayoutEmail;

    //Deklarasi variabel dengan jenis data TextInputLayout
    private TextInputLayout textInputLayoutPassword;

    //Deklarasi variabel dengan jenis data TextInputLayout
    private TextInputLayout textInputLayoutConfirmPassword;

    //Deklarasi variabel dengan jenis data TextInputEditText
    private TextInputEditText textInputEditTextName;

    //Deklarasi variabel dengan jenis data TextInputEditText
    private TextInputEditText textInputEditTextEmail;

    //Deklarasi variabel dengan jenis data TextInputEditText
    private TextInputEditText textInputEditTextPassword;

    //Deklarasi variabel dengan jenis data TextInputEditText
    private TextInputEditText textInputEditTextConfirmPassword;

    //Deklarasi variabel dengan jenis data Button
    private Button btnRegister;

    //Deklarasi variabel dengan jenis data TextView
    private TextView tvLogin;

    //Membuat objek InputValidation
    private InputValidation inputValidation;

    //Membuat objek DatabaseHelper
    private DatabaseHelper databaseHelper;

    //Membuat objek User
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Menghubungkan variabel textInputLayoutName dengan komponen TextInputLayout pada layout
        textInputLayoutName = (TextInputLayout) findViewById(R.id.til1);

        //Menghubungkan variabel textInputLayoutEmail dengan komponen TextInputLayout pada layout
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.til2);

        //Menghubungkan variabel textInputLayoutPassword dengan komponen TextInputLayout pada layout
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.til4);

        //Menghubungkan variabel textInputLayoutConfirmPassword dengan komponen TextInputLayout pada layout
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.til5);

        //Menghubungkan variabel textInputEditTextName dengan komponen TextInputEditText pada layout
        textInputEditTextName = (TextInputEditText) findViewById(R.id.tietName);

        //Menghubungkan variabel textInputEditTextEmail dengan komponen TextInputEditText pada layout
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.tietEmail);

        //Menghubungkan variabel textInputEditTextPassword dengan komponen TextInputEditText pada layout
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.tietPassword);

        //Menghubungkan variabel textInputEditTextConfirmPassword dengan komponen TextInputEditText pada layout
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.tietConfirmPassword);

        //Menghubungkan variabel btnRegister dengan komponen Button pada layout
        btnRegister = (Button) findViewById(R.id.btnRegister);

        //Menghubungkan variabel tvLogin dengan komponen TextView pada layout
        tvLogin = (TextView) findViewById(R.id.tvLogin);

        //Memanggil method initObjects
        initObjects();

        //Membuat event setOnclickListener pada btnRegister
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Memanggil method  postDataToSQLite
                postDataToSQLite();
            }
        });

        //Membuat event setOnclickListener pada tvLogin
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Membuat fungsi selesai
                finish();
            }
        });


    }

    //Membuat method initObjects untuk memanggil class InputValidation dan class DatabaseHelpers
    private void initObjects() {

        //Membuat objek databaseHelper
        inputValidation = new InputValidation(activity);

        //Membuat objek inputValidation
        databaseHelper = new DatabaseHelper(activity);

        //Membuat objek user
        user = new User();
    }

    //Membuat method postDataToSQLite untuk mengimput value ke dalam database
    private void postDataToSQLite() {

        //Fungsi untuk memvalidasi jika name tidak diisi, maka akan muncul pesan "Enter Full Name"
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }

        //Fungsi untuk memvalidasi jika email tidak diisi, maka akan muncul pesan "Enter Valid Email"
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }

        //Fungsi untuk memvalidasi jika email tidak diisi, maka akan muncul pesan "Enter Valid Email"
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }

        //Fungsi untuk memvalidasi jika password tidak diisi, maka akan muncul pesan "Enter Password"
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }

        //Fungsi untuk memvalidasi jika confirmpassword tidak sesuai, maka akan muncul pesan "Password Does Note Matches"
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }

        //Fungsi untuk mengecek pada data yang dimasukkan ke database
        if (!databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim())) {

            //Memasukkan value ke dalam textInputEditTextName
            user.setName(textInputEditTextName.getText().toString().trim());

            //Memasukkan value ke dalam textInputEditTextEmail
            user.setEmail(textInputEditTextEmail.getText().toString().trim());

            //Memasukkan value ke dalam textInputEditTextPassword
            user.setPassword(textInputEditTextPassword.getText().toString().trim());

            //Memanggil method addUser yang ada pada class DatabaseHelper di package database
            databaseHelper.addUser(user);

            //Membuat variabel Toast dan menampilkan pesan "Register Successful"
            Toast.makeText(getApplicationContext(), "Register Successful", Toast.LENGTH_LONG).show();

            //Memanggil method emptyInputEditText yang sudah dibuat
            emptyInputEditText();
        } else {
            //Membuat variabel Toast dan menampilkan pesan "Email Already Exists"
            Toast.makeText(getApplicationContext(), "Email Already Exists", Toast.LENGTH_LONG).show();
        }
    }

    //Membuat method jika InputEditText bernilai null atau kosong.
    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }
}