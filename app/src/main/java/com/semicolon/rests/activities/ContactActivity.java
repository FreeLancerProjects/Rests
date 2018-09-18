package com.semicolon.rests.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.semicolon.rests.R;

public class ContactActivity extends AppCompatActivity {
    private ImageView image_back,image_send;
    private EditText edt_name,edt_email,edt_subject,edt_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        initView();
    }

    private void initView() {
        image_back =findViewById(R.id.image_back);
        image_send =findViewById(R.id.image_send);
        edt_name =findViewById(R.id.edt_name);
        edt_email =findViewById(R.id.edt_email);
        edt_subject =findViewById(R.id.edt_subject);
        edt_message =findViewById(R.id.edt_message);

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        image_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });
    }

    private void CheckData()
    {
        String m_name = edt_name.getText().toString();
        String m_email= edt_email.getText().toString();
        String m_subject = edt_subject.getText().toString();
        String m_msg = edt_message.getText().toString();

        if (!TextUtils.isEmpty(m_name)&&!TextUtils.isEmpty(m_email)&& Patterns.EMAIL_ADDRESS.matcher(m_email).matches()&&!TextUtils.isEmpty(m_subject)&&!TextUtils.isEmpty(m_msg))
        {
            edt_name.setError(null);
            edt_email.setError(null);
            edt_subject.setError(null);
            edt_message.setError(null);

            SendData(m_name,m_email,m_subject,m_msg);
        }else
            {
                if (TextUtils.isEmpty(m_name)){
                    edt_name.setError("Name is required");
                }else
                    {
                        edt_name.setError(null);

                    }
                if (TextUtils.isEmpty(m_email)){
                    edt_name.setError("Email is required");
                }else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
                {
                    edt_email.setError("Invalid email");
                }
                else
                {
                    edt_email.setError(null);

                }

                if (TextUtils.isEmpty(m_subject)){
                    edt_subject.setError("Subject is required");
                }else
                {
                    edt_subject.setError(null);

                }

                if (TextUtils.isEmpty(m_msg)){
                    edt_message.setError("Message is required");
                }else
                {
                    edt_message.setError(null);

                }


            }
    }

    private void SendData(String m_name, String m_email, String m_subject, String m_msg) {

    }
}
