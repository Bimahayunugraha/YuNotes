package com.example.yunotes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yunotes.R;
import com.example.yunotes.database.DatabaseHelper;
import com.example.yunotes.helper.InputValidation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    //Deklarasi activity
    private final Activity activity = MainActivity.this;

    //Deklarasi variabel dengan jenis data TextInputLayout
    private TextInputLayout textInputLayoutEmail;

    //Deklarasi variabel dengan jenis data TextInputLayout
    private TextInputLayout textInputLayoutPassword;

    //Deklarasi variabel dengan jenis data TextInputEditText
    private TextInputEditText textInputEditTextEmail;

    //Deklarasi variabel dengan jenis data TextInputEditText
    private TextInputEditText textInputEditTextPassword;

    //Deklarasi variabel dengan jenis data Button
    private Button ButtonLogin;

    //Deklarasi variabel dengan jenis data TextView
    private TextView Register;

    //Membuat objek InputValidation
    private InputValidation inputValidation;

    //Membuat objek DatabaseHelper
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Menghubungkan variabel textInputLayoutEmail dengan komponen TextInputLayout pada layout
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);

        //Menghubungkan variabel textInputLayoutPassword dengan komponen TextInputLayout pada layout
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        //Menghubungkan variabel textInputEditTextEmail dengan komponen TextInputEditText pada layout
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.tietEmail);

        //Menghubungkan variabel  textInputEditTextPassword dengan komponen TextInputEditText pada layout
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.tietPassword);

        //Menghubungkan variabel ButtonLogin dengan komponen Button pada layout
        ButtonLogin = (Button) findViewById(R.id.btnLogin);

        //Menghubungkan variabel Register dengan komponen Button pada layout
        Register = (TextView) findViewById(R.id.tvCreateAccount);

        //Memanggil method initObjects
        initObjects();

        //Membuat event setOnclickListener pada ButtonLogin
        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Memanggil method verifyFromSQLite
                verifyFromSQLite();
            }
        });

        //Membuat event setOnclickListener pada Register
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Method untuk memanggil activity "RegisterActivity"
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
            }
        });
    }

    //Membuat method initObjects untuk memanggil class InputValidation dan class DatabaseHelpers
    private void initObjects() {

        //Membuat objek databaseHelper
        databaseHelper = new DatabaseHelper(activity);

        //Membuat objek inputValidation
        inputValidation = new InputValidation(activity);
    }

    //Membuat method verifyFromSQLite untuk memveritifikasi value yang ada di dalam database
    private void verifyFromSQLite() {

        //Fungsi untuk memvalidasi jika email tidak diisi, maka akan muncul pesan "Enter Valid Email"
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }

        //Fungsi untuk memvalidasi jika password tidak diisi, maka akan muncul pesan "Enter Password"
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }

        //Fungsi untuk mengecek pada data yang dimasukkan ke database
        if (databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim()
                , textInputEditTextPassword.getText().toString().trim())) {

            //Membuat variabel Toast dan menampilkan pesan "Login Berhasil"
            Toast.makeText(getApplicationContext(),"Login Berhasil", Toast.LENGTH_LONG).show();

            //Method untuk memanggil activity "MainMenuActivity"
            Intent accountsIntent = new Intent(activity, MainMenuActivity.class);

            //Menampilkan data Email
            accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());

            //Memanggil method emptyInputEdittext yang sudah dibuat
            emptyInputEditText();

            //Fungsi untuk memulai activity
            startActivity(accountsIntent);
        } else {
            //Membuat variabel Toast dan menampilkan pesan "Email or Password is Wrong"
            Toast.makeText(getApplicationContext(),"Email or Password is Wrong", Toast.LENGTH_LONG).show();
        }
    }

    //Membuat method jika InputEditText bernilai null atau kosong.
    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
}