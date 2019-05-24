package com.example.matan.project;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    CheckBox checkBox;
    EditText editText_email;
    EditText editText_password;
    Button button_login;
    Button button_forgot;
    Button button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar header = (Toolbar) findViewById(R.id.header);
        TextView mTitle = (TextView) header.findViewById(R.id.toolbar_title);
        mTitle.setText("Login");


        checkBox = (CheckBox)findViewById(R.id.checkBox);
        editText_password = (EditText) findViewById(R.id.edittext_password);
        editText_email = (EditText) findViewById(R.id.edittext_email);

        button_forgot = (Button) findViewById(R.id.forgot_button);
        button_forgot.setOnClickListener(this);
        button_login = (Button)findViewById(R.id.button_login);
        button_login.setOnClickListener(this);
        button_register = (Button) findViewById(R.id.register_button);
        button_register.setOnClickListener(this);





    }

    public void HideKeyboard(View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    @Override
    public void onClick(View v) {
        if(v == button_login)
        {
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(editText_email.getText().toString()).matches() && editText_password.length() >6)
            {
                if(checkBox.isChecked())
                {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("IsCheck", true);
                    editor.putString("Email" , editText_email.getText().toString());
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("EMAIL",editText_email.getText().toString());
                    startActivity(intent);
                    finishAffinity();
                }
                else {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("EMAIL",editText_email.getText().toString());
                    startActivity(intent);
                    finishAffinity();
                }
            }
        }
        if(v == button_forgot){
            Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
            startActivity(intent);

        }

        if(v== button_register) {

            if(getIntent().getStringExtra("LastActivity").equals("HomeActivity"))
            {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
            else {
                super.onBackPressed();
                finish();
            }

        }
    }
}
