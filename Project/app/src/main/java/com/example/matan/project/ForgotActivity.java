package com.example.matan.project;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText_email;
    Button button_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        Toolbar header = (Toolbar) findViewById(R.id.header);
        TextView mTitle = (TextView) header.findViewById(R.id.toolbar_title);
        mTitle.setText("Forgot password");

        editText_email= (EditText) findViewById(R.id.edittext_email);
        button_send =(Button) findViewById(R.id.button_send);
        button_send.setOnClickListener(this);


    }

    public void HideKeyboard(View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    @Override
    public void onClick(View v) {

        if(v == button_send)
        {
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(editText_email.getText().toString()).matches())
            {
                Toast.makeText(this, "Email sent", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Email is not correct", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
