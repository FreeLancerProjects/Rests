package com.semicolon.rests;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.semicolon.rests.R;

import me.anwarshahriar.calligrapher.Calligrapher;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"JannaLT-Regular.ttf",true);

    }
}
