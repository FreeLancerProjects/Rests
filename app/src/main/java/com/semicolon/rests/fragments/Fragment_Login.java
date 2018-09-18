package com.semicolon.rests.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.semicolon.rests.R;
import com.semicolon.rests.activities.HomeActivity;

public class Fragment_Login extends Fragment {
    private EditText edt_email, edt_password;
    private Button btn_login;
    private HomeActivity homeActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        btn_login    =view.findViewById(R.id.btn_login);
        edt_email    =view.findViewById(R.id.edt_email);
        edt_password =view.findViewById(R.id.edt_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });
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

                edt_email.setError(getString(R.string.phone_email_req));
            }else

            {

                edt_email.setError(null);

            }

            if (TextUtils.isEmpty(m_password))
            {
                edt_password.setError(getString(R.string.password_req));
            }else
            {
                edt_password.setError(null);

            }

        }


    }

    private void login(String email, String password) {



    }
    public static Fragment_Login getInstance()
    {
        Fragment_Login fragment = new Fragment_Login();
        return fragment;
    }

}
