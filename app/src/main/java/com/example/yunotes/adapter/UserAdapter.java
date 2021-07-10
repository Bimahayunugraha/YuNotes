package com.example.yunotes.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.yunotes.R;
import com.example.yunotes.activity.EditAccount;
import com.example.yunotes.activity.MainActivity;
import com.example.yunotes.database.DatabaseHelper;
import com.example.yunotes.database.User;

import java.util.HashMap;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    //Deklarasi variabel dengan jenis data List
    private List<User> listUsers;

    //Membuat variabel context
    private Context context;

    //Membuat constructor UserAdapter
    public UserAdapter(List<User> listUsers) {
        //Memberikan nilai listuser dari class User
        this.listUsers = listUsers;
    }

    /*Fungsi ini secara otomatis dipanggil ketika tampilan item list siap
      untuk ditampilkan atau akan ditampilkan*/
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Membuat fungsi variable context untuk memanggil context
        context = parent.getContext();

        //Membuat xml mengembang pada recycler item view dan Mengatur Layout untuk menampilkan item
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_recycler, parent, false);

        //Mengembalikan nilai UserViewHolder ke dalam itemView
        return new UserViewHolder(itemView);
    }

    //Membuat method constructor onBindViewHolder untuk menampilkan value sesuai position
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {

        //Deklarasi varibel dengan tipe data string untuk id, nm, email, dan pw
        String id, nm, email, pw;

        //Menghubungkan dengan class DatabaseHelper pada package database
        DatabaseHelper db = new DatabaseHelper(context);

        //Menampilkan value id pada listUser dengan mengatur position dan memanggil method getId
        id = listUsers.get(position).getId();

        //Menampilkan value nm pada listUser dengan mengatur position dan memanggil method getName
        nm = listUsers.get(position).getName();

        //Menampilkan value email pada listUser dengan mengatur position dan memanggil method getEmail
        email = listUsers.get(position).getEmail();

        //Menampilkan value pw pada listUser dengan mengatur position dan memanggil method getPassword
        pw = listUsers.get(position).getPassword();

        //Set item ke tvNama dengan memanggil dar value nm
        holder.tvNama.setText(nm);

        //Set item ke tvEmail dengan memanggil value email
        holder.tvEmail.setText(email);

        //Set item ke tvPassword dengan memanggil value pw
        holder.tvPassword.setText(pw);

        //Membuat event setOnclickLister pada imageButtonOverflow untuk menampilkan popup menu
        holder.imageButtonOverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Membuat objek popup menu
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);

                //Menampilkan popup menu dari layout menu
                popupMenu.inflate(R.menu.popupmenu);

                //Membuat event setOnMenuItemClickListener pada popup menu saat menu dipilih
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        //Membuat fungsi switch pada item menu untuk melakukan edit dan delete account
                        switch (menuItem.getItemId()) {

                            //Membuat case untuk menu edit
                            case R.id.edit:

                                //Membuat fungsi intent untuk menuju ke class EditAccount pada package activity
                                Intent i = new Intent(context, EditAccount.class);

                                //Memberikan value dengan memanggil column user_id berdasarkan value dari id
                                i.putExtra("user_id", id);

                                //Memberikan value dengan memanggil column user_name berdasarkan value dari name
                                i.putExtra("user_name", nm);

                                //Memberikan value dengan memanggil column user_email berdasarkan value dari email
                                i.putExtra("user_email", email);

                                //Memberikan value dengan memanggil column user_password berdasarkan value dari password
                                i.putExtra("user_password", pw);

                                //Membuat fungsi untuk memulai intent
                                context.startActivity(i);

                                //Membuat fungsi break untuk membuat case edit berhenti berulang
                                break;

                            //Membuat case untuk menu delete
                            case R.id.delete:

                                //Membuat Fungsi AlertDialog saat ingin menghapus data
                                final AlertDialog.Builder builder = new AlertDialog.Builder(context);

                                //Membuat value message pada alert dialog
                                builder.setMessage("Do you want to delete your account?");

                                //Membuat value Cancelable untuk fungsi saat di klik 'Yes' tidak akan batal
                                builder.setCancelable(true);

                                //Membuat AlertDialog "Iya" jika ingin melanjutkan untuk menghapus data
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //Membuat objek HashMap dari user
                                        HashMap<String,String> user =  new HashMap<>();

                                        //Memberikan value dari column user_id
                                        user.put("user_id", id);

                                        //Memanggil method deleteUser yang ada pada class DatabaseHelper di package database
                                        db.deleteUser(user);

                                        //Membuat toast saat account berhasil dihapus
                                        Toast.makeText(context, "Account Deleted", Toast.LENGTH_SHORT).show();

                                        //Membuat fungsi intent untuk menuju ke class MainActivity pada package activity
                                        Intent in = new Intent(context, MainActivity.class);

                                        //Fungsi untuk memulai intent
                                        context.startActivity(in);

                                    }
                                });

                                //Membuat AlertDialog "Tidak" jika ingin membatalkan untuk menghapus data
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Membuat dialog cancel untuk membatalkan hapus data
                                        dialog.cancel();
                                    }
                                });

                                //Memunculkan AlertDialog
                                builder.show();
                                break;

                        }
                        return true;
                    }
                });

                //Fungsi memunculkan popup menu
                popupMenu.show();
            }
        });

    }

    //Fungsi getItemCount() mengembalikan jumlah item yang akan ditampilkan dalam list
    @Override
    public int getItemCount() {
        Log.v(UserAdapter.class.getSimpleName(),"" + listUsers.size());
        return listUsers.size();
    }

    //Membuat class untuk mendeklarasikan tempat untuk meletakkan isi kedalam RecyclerView
    public class UserViewHolder extends RecyclerView.ViewHolder {

        //Deklarasi variabel imageButtonOverflow dengan tipe data ImageButton
        public ImageButton imageButtonOverflow;

        //Deklarasi variabel tvNama dengan tipe data TextView
        public TextView tvNama;

        //Deklarasi variabel tvPassword dengan tipe data TextView
        public TextView tvPassword;

        //Deklarasi variabel tvtEmail dengan tipe data TextView
        public TextView tvEmail;

        //Membuat method view pada UserViewHolder
        public UserViewHolder(View view) {
            super(view);
            //Menghubungkan variabel imageButtonOverflow dengan komponen ImageView pada layout
            imageButtonOverflow = (ImageButton) view.findViewById(R.id.imageBtnOverflow);

            //Menghubungkan variabel tvNama dengan komponen TextView pada layout
            tvNama = (TextView) view.findViewById(R.id.tvName);

            //Menghubungkan variabel tvPassword dengan komponen TextView pada layout
            tvPassword = (TextView) view.findViewById(R.id.tvPassword);

            //Menghubungkan variabel tvEmail dengan komponen TextView pada layout
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        }
    }

}
