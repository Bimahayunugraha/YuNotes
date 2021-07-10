package com.example.yunotes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Membuat Database Version dengan version 1
    private static final int DATABASE_VERSION = 1;

    // Membuat Database Name dengan nama "UserManager.db"
    private static final String DATABASE_NAME = "UserManager.db";

    // Membuat User table name dengan nama "user"
    private static final String TABLE_USER = "user";

    // Membuat User table columns names dengan nama "user_id"
    private static final String COLUMN_USER_ID = "user_id";

    // Membuat User table columns names dengan nama "user_name"
    private static final String COLUMN_USER_NAME = "user_name";

    // Membuat User table columns names dengan nama "user_email"
    private static final String COLUMN_USER_EMAIL = "user_email";

    // Membuat User table columns names dengan nama "user_password"
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // Membuat table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("

            //Membuat query column user_id dengan tipe data integer dan set menjadi primary key autoincrement
            //Membuat query column user_name dengan tipe data text
            //Membuat query column user_email dengan tipe data text
            //Membuat query column user_password dengan tipe data text
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    // Membuat fungsi drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    // Membuat constructor pada DatabaseHelper
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Membuat method onCreate pada database dengan menggunakan SQLiteDatabase
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    // Membuat method onUpgrade pada database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Membuat fungis Drop User Table jika ada
        db.execSQL(DROP_USER_TABLE);

        // Membuat table baru
        onCreate(db);
    }

    // Membuat method addUser untuk menanmbahkan user dan merekam user
    public void addUser(User user) {

        // Memanggil SQLiteDatabase dan membuat database
        SQLiteDatabase db = this.getWritableDatabase();

        // Membuat value dari tabel user
        ContentValues values = new ContentValues();

        // Memambahkan value ke dalam column user_name dengan memanggil getter Name
        // pada class user di dalam package database
        values.put(COLUMN_USER_NAME, user.getName());

        // Memambahkan value ke dalam column user_email dengan memanggil getter Email
        // pada class user di dalam package database
        values.put(COLUMN_USER_EMAIL, user.getEmail());

        // Memambahkan value ke dalam column user_password dengan memanggil getter Password
        // pada class user di dalam package database
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // Memasukkan baris ke dalam tabel
        db.insert(TABLE_USER, null, values);

        // Membuat fungsi untuk menutup database
        db.close();
    }

    // Membuat method untuk mengambil semua data user dan mengembalikan ke dalam list user
    public List<User> getAllUser() {

        // Mengambil column array
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };

        // Mengurutkan order dari column user_name
        String sortOrder =
                COLUMN_USER_NAME + " ASC";

        // Memanggil list user
        List<User> userList = new ArrayList<User>();

        //Memanggil SQLiteDatabse dan membaca database
        SQLiteDatabase db = this.getReadableDatabase();

        // query dari tabel user
        /**
         * Fungsi query disini digunakan untuk mengambil record dari tabel user, fungsi ini seperti menggunakan SQL Query
         * SQL Query yang setara dengan fungsi ini adalah
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Tabel untuk query
                columns,    //columns untuk mengembalikan
                null,        //columns untuk klausa WHERE
                null,        //Nilai untuk klausa WHERE
                null,       //Kelompok dari row
                null,       //Memfilter dari kelompok row
                sortOrder); //Mengurutkan order

        // Melintas dari semua row dan menambahkan data ke dalam list user
        if (cursor.moveToFirst()) {
            do {
                // Memanggil class user pada package database
                User user = new User();

                // Memanggil fungsi setter id dari class user untuk menambahkan user record ke dalam column user_id
                user.setId(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)));

                // Memanggil fungsi setter Name dari class user untuk menambahkan user record ke dalam column user_name
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));

                // Memanggil fungsi setter Email dari class user untuk menambahkan user record ke dalam column user_email
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));

                // Memanggil fungsi setter Password dari class user untuk menambahkan user record ke dalam column user_password
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));

                // Menambahkan user record ke dalam list
                userList.add(user);

                // Method while untuk menuju ke data selanjutnya
            } while (cursor.moveToNext());
        }

        // Fungsi menutup cursor
        cursor.close();

        // Fungsi menutup database
        db.close();


        // Mengembalikan user list
        return userList;
    }

    // Membuat method untuk melakukan update pada user record
    public void updateUser(HashMap<String, String> user) {

        // Memanggil SQLiteDatabase dan membuat database
        SQLiteDatabase db = this.getWritableDatabase();

        // Membuat value dari tabel user
        ContentValues values = new ContentValues();

        // Melakukan update dengan mengganti value ke dalam column user_name dengan memanggil getter Name
        // pada class user di dalam package database
        values.put(COLUMN_USER_NAME, user.get(COLUMN_USER_NAME));

        // Melakukan update dengan mengganti value ke dalam column user_email dengan memanggil getter Email
        // pada class user di dalam package database
        values.put(COLUMN_USER_EMAIL, user.get(COLUMN_USER_EMAIL));

        // Melakukan update dengan mengganti value ke dalam column user_passowrd dengan memanggil getter Password
        // pada class user di dalam package database
        values.put(COLUMN_USER_PASSWORD, user.get(COLUMN_USER_PASSWORD));

        // Melakukan update pada row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{user.get(COLUMN_USER_ID)});

        // Fungsi menutup database
        db.close();
    }

    // Membuat method untuk melakukan delete pada user record
    public void deleteUser(HashMap<String, String> user) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Menghapus user record dari id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{user.get(COLUMN_USER_ID)});
        db.close();
    }

    // Membuat method untuk mengecek user ada atau tidak berdasarkan email
    public boolean checkUser(String email) {

        // Mengambil array pada columns dengan menggunakan column user_id
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // Memilih kriteria column
        String selection = COLUMN_USER_EMAIL + " = ?";


        // Memilih argument
        String[] selectionArgs = {email};

        // query dari dari user dengan kondisi
        /**
         * Fungsi query disini digunakan untuk mengambil record dari tabel user, fungsi ini seperti menggunakan SQL Query
         * SQL Query yang setara dengan fungsi ini adalah
         * SELECT user_id FROM user WHERE user_email = '';
         */
        Cursor cursor = db.query(TABLE_USER, //Tabel untuk query
                columns, //columns untuk mengembalikan
                selection, //columns untuk klausa WHERE
                selectionArgs, //Nilai untuk klausa WHERE
                null, //Kelompok dari row
                null, //Memfilter dari kelompok row
                null); //Mengurutkan order
        int cursorCount = cursor.getCount();

        // Fungsi menutup cursor
        cursor.close();

        // Fungsi menutup database dengan mengembalikan cursor
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    // Membuat method untuk mengecek user ada atau tidak berdasarkan email dan password
    public boolean checkUser(String email, String password) {

        // Mengambil array pada columns dengan menggunakan column user_id
        String[] columns = {
                COLUMN_USER_ID
        };
        // Fungsi untuk membaca database
        SQLiteDatabase db = this.getReadableDatabase();

        // Memilih kriteria column
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // Memilih arguments
        String[] selectionArgs = {email, password};

        // query dari dari user dengan kondisi
        /**
         * Fungsi query disini digunakan untuk mengambil record dari tabel user, fungsi ini seperti menggunakan SQL Query
         * SQL Query yang setara dengan fungsi ini adalah
         * SELECT user_id FROM user WHERE user_email = '';
         */
        Cursor cursor = db.query(TABLE_USER, //Tabel untuk query
                columns, //columns untuk mengembalikan
                selection, //columns untuk klausa WHERE
                selectionArgs, //Nilai untuk klausa WHERE
                null, //Kelompok dari row
                null, //Memfilter dari kelompok row
                null); //Mengurutkan order
        int cursorCount = cursor.getCount();

        // Fungsi menutup cursor
        cursor.close();

        // Fungsi menutup database dengan mengembalikan cursor
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
}
