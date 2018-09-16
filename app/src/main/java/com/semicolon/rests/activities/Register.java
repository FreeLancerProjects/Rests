package com.semicolon.rests.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.semicolon.rests.R;

public class Register extends AppCompatActivity {

    Button signUp;
    TextView login;
    EditText name,phone,email,city,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


    }
}
