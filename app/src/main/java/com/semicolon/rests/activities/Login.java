package com.semicolon.rests.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.rests.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity implements View.OnClickListener {

    TextView signUp;
    EditText email,password;
    Button logIn;
    private String m_email,m_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"JannaLT-Regular.ttf",true);
*/

        initView();


    }

    private void initView() {
        signUp=findViewById(R.id.sign_txt);
        logIn=findViewById(R.id.btn_login);
        email=findViewById(R.id.edt_email);
        password=findViewById(R.id.edt_password);

        signUp.setOnClickListener(this);
        logIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_txt:
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                login();
                break;


        }

    }

    private void login() {

        m_email = email.getText().toString();
        m_password = password.getText().toString();


    }
}
