package com.example.yunotes.helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class InputValidation {

    //Membuat variabel context
    private Context context;

    //Method untuk constructor InputValidation
    public InputValidation(Context context) {
        this.context = context;
    }

    //Method untuk mengecek InputEditText terisi
    /**
     *
     * parameter textInputEditText
     * parameter textInputLayout
     * parameter message
     * return
     */
    public boolean isInputEditTextFilled(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {

        //Memberi value ke dalam textInputEditText
        String value = textInputEditText.getText().toString().trim();

        //Membuat fungsi if jika value kosong
        if (value.isEmpty()) {

            //Membuat parameter message error untuk textInputLayout jika value kosong
            textInputLayout.setError(message);

            //Fungsi menyembunyikan keyboard pada textInputEditText
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            //Fungsi untuk tidak menampilkan error pada textInputLayout jika value ada
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    //Method untuk mengecek pada InputEditTextEmail

    /**
     *
     * parameter textInputEditText
     * parameter textInputLayout
     * parameter message
     * return
     */
    public boolean isInputEditTextEmail(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {

        //Memberi value ke dalam textInputEditText
        String value = textInputEditText.getText().toString().trim();

        //Membuat fungsi if jika value kosong
        if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {

            //Membuat parameter message error untuk textInputLayout jika value kosong
            textInputLayout.setError(message);

            //Fungsi menyembunyikan keyboard pada textInputEditText
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            //Fungsi untuk tidak menampilkan error pada textInputLayout jika value ada
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    //Method untuk mengecek pada InputEditText sesuai dengan data yang diberikan
    public boolean isInputEditTextMatches(TextInputEditText textInputEditText1, TextInputEditText textInputEditText2, TextInputLayout textInputLayout, String message) {

        //Memberi value pertama ke dalam textInputEditText 1
        String value1 = textInputEditText1.getText().toString().trim();

        //Memberi value kedua ke dalam textInputEditText 1
        String value2 = textInputEditText2.getText().toString().trim();

        //Membuat fungsi if jika value 1 tidak sama dengan value 2
        if (!value1.contentEquals(value2)) {

            //Membuat parameter message error untuk textInputLayout jika value kosong
            textInputLayout.setError(message);

            //Fungsi menyembunyikan keyboard pada textInputEditText
            hideKeyboardFrom(textInputEditText2);
            return false;
        } else {
            //Fungsi untuk tidak menampilkan error pada textInputLayout jika value ada
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    //Method menyembunyikan keyboard pada textInputEditText
    private void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
