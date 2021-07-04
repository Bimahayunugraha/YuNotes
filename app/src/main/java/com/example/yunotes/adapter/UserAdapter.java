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

    private List<User> listUsers;
    private Context context;

    public UserAdapter(List<User> listUsers) {
        this.listUsers = listUsers;
    }
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_recycler, parent, false);
        return new UserViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {

        String id, nm, email, pw;
        DatabaseHelper db = new DatabaseHelper(context);

        id = listUsers.get(position).getId();
        nm = listUsers.get(position).getName();
        email = listUsers.get(position).getEmail();
        pw = listUsers.get(position).getPassword();

        holder.tvNama.setText(nm);
        holder.tvEmail.setText(email);
        holder.tvPassword.setText(pw);

        holder.imageButtonOverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.inflate(R.menu.popupmenu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.edit:
                                Intent i = new Intent(context, EditAccount.class);
                                i.putExtra("user_id", id);
                                i.putExtra("user_name", nm);
                                i.putExtra("user_email", email);
                                i.putExtra("user_password", pw);
                                context.startActivity(i);
                                break;
                            case R.id.delete:
                                //Membuat AlertDialog saat ingin menghapus data
                                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("Do you want to delete your account?");
                                builder.setCancelable(true);
                                //Membuat AlertDialog "Iya" jika ingin melanjutkan untuk menghapus data
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //Fungsi untuk menghapus data
                                        HashMap<String,String> user =  new HashMap<>();
                                        user.put("user_id", id);
                                        db.deleteUser(user);
                                        Toast.makeText(context, "Account Deleted", Toast.LENGTH_SHORT).show();
                                        Intent in = new Intent(context, MainActivity.class);
                                        context.startActivity(in);

                                    }
                                });
                                //Membuat AlertDialog "Tidak" jika ingin membatalkan untuk menghapus data
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                //Memunculkan AlertDialog nya
                                builder.show();
                                break;

                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }
    @Override
    public int getItemCount() {
        Log.v(UserAdapter.class.getSimpleName(),"" + listUsers.size());
        return listUsers.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public ImageButton imageButtonOverflow;
        public TextView tvNama;
        public TextView tvPassword;
        public TextView tvEmail;
        public UserViewHolder(View view) {
            super(view);
            imageButtonOverflow = (ImageButton) view.findViewById(R.id.imageBtnOverflow);
            tvNama = (TextView) view.findViewById(R.id.tvName);
            tvPassword = (TextView) view.findViewById(R.id.tvPassword);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        }
    }

}
