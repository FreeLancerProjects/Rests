package com.semicolon.rests.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.semicolon.rests.R;
import com.semicolon.rests.activities.HomeActivity;
import com.semicolon.rests.common.Common;
import com.semicolon.rests.models.UserModel;
import com.semicolon.rests.service.Api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Login extends Fragment {
    private EditText edt_email, edt_password;
    private Button btn_login;
    private HomeActivity homeActivity;
    private ProgressDialog progressDialog;

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
        progressDialog = Common.getProgress(getActivity(),getString(R.string.sign_inn));
        progressDialog.show();
        Api.getServices()
                .login(email,password)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            if (response.body()!=null)
                            {
                                if (response.body().getSuccess_login()==1)
                                {
                                    homeActivity.UpdateUi(response.body());

                                }else if (response.body().getSuccess_login()==0)
                                {
                                    Toast.makeText(homeActivity,R.string.something_error, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        progressDialog.dismiss();

                        Toast.makeText(homeActivity,R.string.something_error, Toast.LENGTH_LONG).show();
                        Log.e("Error",t.getMessage());
                    }
                });

    }
    public static Fragment_Login getInstance()
    {
        Fragment_Login fragment = new Fragment_Login();
        return fragment;
    }

}
