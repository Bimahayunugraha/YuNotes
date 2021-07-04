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

    private TextInputEditText editName, editEmail, editPassword;
    private Button btnEdit;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    private String id, nm, email, pw;
    private ImageView backImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        editName = findViewById(R.id.txtEditName);
        editEmail = findViewById(R.id.txtEditEmail);
        editPassword = findViewById(R.id.txtEditPassword);
        btnEdit = findViewById(R.id.btnEdit);
        backImage = findViewById(R.id.backImage);


        id = getIntent().getStringExtra("user_id");
        nm = getIntent().getStringExtra("user_name");
        email = getIntent().getStringExtra("user_email");
        pw = getIntent().getStringExtra("user_password");

        editName.setText(nm);
        editEmail.setText(email);
        editPassword.setText(pw);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editName.getText().toString().equals("")||editEmail.getText().toString().equals("") || editPassword.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Data Not Complete", Toast.LENGTH_SHORT).show();
                } else {
                    nm = editName.getText().toString();
                    email = editEmail.getText().toString();
                    pw = editPassword.getText().toString();

                    HashMap<String,String> user =  new HashMap<>();
                    user.put("user_id", id);
                    user.put("user_name", nm);
                    user.put("user_email", email);
                    user.put("user_password", pw);

                    Toast.makeText(getApplicationContext(),"Update Account Success", Toast.LENGTH_LONG).show();
                    databaseHelper.updateUser(user);
                    callLogin();
                }
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });
    }

    public void callLogin(){
        Intent intent = new Intent(EditAccount.this,Profile.class);
        startActivity(intent);
    }
}