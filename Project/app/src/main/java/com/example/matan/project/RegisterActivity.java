package com.example.matan.project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email;
    EditText password;
    EditText confrimpassword;
    Button register_button;
    Button login_button;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar header = (Toolbar) findViewById(R.id.header);
        TextView mTitle = (TextView) header.findViewById(R.id.toolbar_title);
        mTitle.setText("Register");

        email = (EditText) findViewById(R.id.edittext_email);
        password = (EditText) findViewById(R.id.edittext_password);
        confrimpassword = (EditText) findViewById(R.id.edittext_confrimpassword);
        register_button = (Button) findViewById(R.id.button_register);
        register_button.setOnClickListener(this);
        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(this);

        pref = getApplicationContext().getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        editor = pref.edit();
    }


    public void HideKeyboard(View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }


    @Override
    public void onClick(View v) {
        if (v == register_button) {
            if (password.getText().toString().equals(confrimpassword.getText().toString()) && android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() && password.length() >= 6 && confrimpassword.length() >= 6) {
                editor.putString("Email" , email.getText().toString());
                editor.commit();

                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    Toast.makeText(this, "Email is not correct", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.getText().toString().equals(confrimpassword.getText().toString())) {
                        if (password.length() < 6 && confrimpassword.length() < 6) {
                            Toast.makeText(this, "Password is short", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(this, "Password is not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        if (v == login_button) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.putExtra("LastActivity","RegisterActivity");
            startActivity(intent);
        }
    }
}
