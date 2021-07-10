package com.example.yunotes.database;

public class User {

    //Membuat inisialisasi objek id
    private String id;

    //Membuat inisialisasi objek name
    private String name;

    //Membuat inisialisasi objek email
    private String email;

    //Membuat inisialisasi objek password
    private String password;

    //Membuat method getId untuk untuk mengembalikan nilai ke variabel id
    public String getId() {
        return id;
    }

    //Membuat method setId untuk untuk mengembalikan nilai ke variabel id
    public void setId(String id) {
        this.id = id;
    }

    //Membuat method getName untuk untuk mengembalikan nilai ke variabel name
    public String getName() {
        return name;
    }

    //Membuat method setName untuk untuk mengembalikan nilai ke variabel name
    public void setName(String name) {
        this.name = name;
    }

    //Membuat method getEmail untuk untuk mengembalikan nilai ke variabel email
    public String getEmail() {
        return email;
    }

    //Membuat method setEmail untuk untuk mengembalikan nilai ke variabel email
    public void setEmail(String email) {
        this.email = email;
    }

    //Membuat method getPassworduntuk untuk mengembalikan nilai ke variabel password
    public String getPassword() {
        return password;
    }

    //Membuat method setPassword untuk untuk mengembalikan nilai ke variabel password
    public void setPassword(String password) {
        this.password = password;
    }
}

