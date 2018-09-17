package com.semicolon.rests.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.semicolon.rests.R;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_signup;
    private EditText edt_email, edt_password;
    private Button btn_login;
    private Animation animation;

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
        tv_signup=findViewById(R.id.tv_signup);
        btn_login=findViewById(R.id.btn_login);
        edt_email =findViewById(R.id.edt_email);
        edt_password =findViewById(R.id.edt_password);


        animation= AnimationUtils.loadAnimation(this,R.anim.press_anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        tv_signup.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_signup:
                tv_signup.clearAnimation();
                tv_signup.startAnimation(animation);
                break;
            case R.id.btn_login:
                CheckData();
                break;


        }

    }

    private void CheckData() {
        String m_email = edt_email.getText().toString();
        String m_password = edt_password.getText().toString();

        if (!TextUtils.isEmpty(m_email)&&!TextUtils.isEmpty(m_password))
        {

            edt_email.setError(null);
            edt_password.setError(null);
            login(m_email,m_password);


        }else
            {

                if (TextUtils.isEmpty(m_email))
                {

                edt_email.setError("Phone number or email is required");
                }else

                    {

                        edt_email.setError(null);

                    }

                    if (TextUtils.isEmpty(m_password))
                    {
                    edt_password.setError("Password is required");
                    }else
                        {
                    edt_password.setError(null);

                        }

            }


    }

    private void login(String email, String password) {



    }
}
