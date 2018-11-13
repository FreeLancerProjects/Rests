package com.semicolon.rests.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lamudi.phonefield.PhoneInputLayout;
import com.semicolon.rests.R;
import com.semicolon.rests.common.Common;
import com.semicolon.rests.models.UserModel;
import com.semicolon.rests.preferences.Preferences;
import com.semicolon.rests.service.Api;
import com.semicolon.rests.singletone.UserSingleTone;
import com.semicolon.rests.tags.Tags;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Profile extends Fragment {
    private TextView tv_name,tv_phone,tv_email,tv_user_name;
    private UserModel userModel;
    private UserSingleTone userSingleTone ;
    private AlertDialog dialog;
    private ImageView image_edit_phone,image_edit_email,image_edit_name,image_edit_password;
    private ProgressDialog progressDialog;
    private Preferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        initView(view);
        return view;

    }

    public static Fragment_Profile getInstance()
    {
        Fragment_Profile fragment = new Fragment_Profile();
        return fragment;
    }
    private void initView(View view)
    {
        preferences = Preferences.getInstance();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        tv_name = view.findViewById(R.id.tv_name);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_email = view.findViewById(R.id.tv_email);
        tv_user_name = view.findViewById(R.id.tv_user_name);
        image_edit_phone = view.findViewById(R.id.image_edit_phone);
        image_edit_email = view.findViewById(R.id.image_edit_email);
        image_edit_name = view.findViewById(R.id.image_edit_name);
        image_edit_password = view.findViewById(R.id.image_edit_password);

        UpdateData(userModel);



        image_edit_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAlertDialog_UpdateProfile(Tags.update_phone);
            }
        });

        image_edit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAlertDialog_UpdateProfile(Tags.update_email);
            }
        });

        image_edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAlertDialog_UpdateProfile(Tags.update_full_name);
            }
        });
        image_edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAlertDialog_UpdateProfile(Tags.update_password);
            }
        });

    }
    private void UpdateData(UserModel userModel)
    {
        if (userModel!=null)
        {
            tv_name.setText(userModel.getUser_full_name());
            tv_email.setText(userModel.getUser_email());
            tv_phone.setText(userModel.getUser_phone());
            tv_user_name.setText(userModel.getUser_name());
        }
    }
    private void CreateAlertDialog_UpdateProfile(final String type)
    {
        dialog = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_update_profile,null);
        final TextView tv_title = view.findViewById(R.id.tv_title);
        final EditText edt_update = view.findViewById(R.id.edt_update);
        final EditText edt_newPassword = view.findViewById(R.id.edt_newPassword);
        final PhoneInputLayout edt_check_phone = view.findViewById(R.id.edt_check_phone);
        Button btn_update = view.findViewById(R.id.btn_update);
        Button btn_close = view.findViewById(R.id.btn_close);

        if (type.equals(Tags.update_full_name))
        {
            tv_title.setText(R.string.upd_name);
            edt_update.setInputType(InputType.TYPE_CLASS_TEXT);
            edt_newPassword.setVisibility(View.GONE);
            edt_update.setHint(R.string.upd_name);
            if (userModel!=null)
            {
                edt_update.setText(userModel.getUser_full_name());


            }
        }else if (type.equals(Tags.update_phone))
        {
            tv_title.setText(R.string.upd_phone);
            edt_update.setInputType(InputType.TYPE_CLASS_PHONE);
            edt_newPassword.setVisibility(View.GONE);
            edt_update.setHint(R.string.upd_phone);
            if (userModel!=null)
            {
                edt_update.setText(userModel.getUser_phone());


            }
        }else if (type.equals(Tags.update_email))
        {
            tv_title.setText(R.string.upd_email);
            edt_update.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            edt_newPassword.setVisibility(View.GONE);
            edt_update.setHint(R.string.upd_email);
            if (userModel!=null)
            {
                edt_update.setText(userModel.getUser_email());


            }
        }else if (type.equals(Tags.update_password))
        {

            tv_title.setText(R.string.upd_password);
            edt_update.setTransformationMethod(PasswordTransformationMethod.getInstance());
            edt_newPassword.setVisibility(View.VISIBLE);
            edt_update.setHint(R.string.old_pass);

        }

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals(Tags.update_full_name))
                {
                    String m_name = edt_update.getText().toString();
                    if (!TextUtils.isEmpty(m_name))
                    {
                        edt_update.setError(null);

                        Common.CloseKeyBoard(getActivity(),edt_update);
                        progressDialog = Common.getProgress(getActivity(),getString(R.string.uptng_name));
                        progressDialog.show();
                        update_name(m_name);

                    }else
                    {
                        edt_update.setError(getString(R.string.name_req));
                    }

                }else if (type.equals(Tags.update_phone))
                {

                    String m_phone = edt_update.getText().toString();
                    if (!TextUtils.isEmpty(m_phone))
                    {
                        if (!m_phone.startsWith("+"))
                        {
                            m_phone = "+"+m_phone;
                        }
                        edt_check_phone.setPhoneNumber(m_phone);

                    }

                    if (TextUtils.isEmpty(m_phone))
                    {
                        edt_update.setError(getString(R.string.phone_req));
                        Log.e("d","ff");


                    }else if (!edt_check_phone.isValid())
                    {
                        edt_update.setError(getString(R.string.inv_phone));

                    }
                    else
                    {
                        Common.CloseKeyBoard(getActivity(),edt_update);
                        edt_update.setError(null);
                        progressDialog = Common.getProgress(getActivity(),getString(R.string.uptng_phone));
                        progressDialog.show();
                        update_phone(m_phone);

                    }
                }else if (type.equals(Tags.update_email))
                {

                    String m_email = edt_update.getText().toString();

                    if (TextUtils.isEmpty(m_email))
                    {
                        edt_update.setError(getString(R.string.email_req));

                    }else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
                    {
                        edt_update.setError(getString(R.string.inv_email));

                    }else
                    {
                        Common.CloseKeyBoard(getActivity(),edt_update);

                        edt_update.setError(null);
                        progressDialog = Common.getProgress(getActivity(),getString(R.string.uptng_email));
                        progressDialog.show();
                        update_email(m_email);

                    }

                }else if (type.equals(Tags.update_password))
                {

                    String m_oldPassword = edt_update.getText().toString();
                    String m_newPassword = edt_newPassword.getText().toString();

                    if (!TextUtils.isEmpty(m_oldPassword)&&!TextUtils.isEmpty(m_newPassword)&&m_newPassword.length()>=5)
                    {
                        Common.CloseKeyBoard(getActivity(),edt_update);
                        edt_update.setError(null);
                        edt_newPassword.setError(null);

                        progressDialog = Common.getProgress(getActivity(),getString(R.string.updtng_pass));
                        progressDialog.show();
                        update_Password(m_oldPassword,m_newPassword);

                    }else
                    {
                        if (TextUtils.isEmpty(m_oldPassword))
                        {
                            edt_update.setError(getString(R.string.password_req));

                        }else
                        {
                            edt_update.setError(null);

                        }

                        if (TextUtils.isEmpty(m_newPassword))
                        {
                            edt_newPassword.setError(getString(R.string.newpass_req));

                        }else if (m_newPassword.length() < 5)
                        {
                            edt_newPassword.setError(getString(R.string.pass_short));

                        }
                        else
                        {
                            edt_newPassword.setError(null);

                        }

                    }





                }
            }
        });
        dialog.setView(view);
        dialog.show();




    }

    private void update_name(String newName)
    {
        Api.getServices().updateProfile(userModel.getUser_id(),userModel.getUser_phone(),newName,userModel.getUser_email())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            progressDialog.dismiss();
                            if (response.body().getSuccess_update()==1)
                            {
                                UpdateUserData(response.body());
                                Toast.makeText(getActivity(), R.string.upd_succ, Toast.LENGTH_SHORT).show();
                            }
                            else if (response.body().getSuccess_update()==2)
                            {
                                Toast.makeText(getActivity(), R.string.ph_em_exist, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        dialog.dismiss();
                        progressDialog.dismiss();
                        Log.e("Error",t.getMessage());
                        Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void update_phone(String newPhone)
    {
        Api.getServices().updateProfile(userModel.getUser_id(),newPhone,userModel.getUser_full_name(),userModel.getUser_email())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            progressDialog.dismiss();

                            if (response.body().getSuccess_update()==1)
                            {
                                UpdateUserData(response.body());
                                Toast.makeText(getActivity(), R.string.upd_succ, Toast.LENGTH_SHORT).show();
                            }
                            else if (response.body().getSuccess_update()==2)
                            {
                                Toast.makeText(getActivity(), R.string.ph_em_exist, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        dialog.dismiss();
                        progressDialog.dismiss();
                        Log.e("Error",t.getMessage());
                        Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void update_email(String newEmail)
    {
        Api.getServices().updateProfile(userModel.getUser_id(),userModel.getUser_phone(),userModel.getUser_full_name(),newEmail)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            progressDialog.dismiss();

                            if (response.body().getSuccess_update()==1)
                            {
                                UpdateUserData(response.body());
                                Toast.makeText(getActivity(), R.string.upd_succ, Toast.LENGTH_SHORT).show();
                            }
                            else if (response.body().getSuccess_update()==2)
                            {
                                Toast.makeText(getActivity(), R.string.ph_em_exist, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        dialog.dismiss();
                        progressDialog.dismiss();
                        Log.e("Error",t.getMessage());
                        Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void update_Password(String oldPass,String newPass)
    {
        Api.getServices().updatePassword(userModel.getUser_id(),oldPass,newPass)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            progressDialog.dismiss();

                            Log.e("dddd",response.body().getSuccess_update_pass()+"");
                            if (response.body().getSuccess_update_pass()==0)
                            {
                                Toast.makeText(getActivity(), R.string.wrong_oldpass, Toast.LENGTH_SHORT).show();
                            }else if (response.body().getSuccess_update_pass()==1)
                            {
                                UpdateUserData(response.body());
                                Toast.makeText(getActivity(), R.string.upd_succ, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        dialog.dismiss();
                        progressDialog.dismiss();
                        Log.e("Error",t.getMessage());
                        Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void  UpdateUserData(UserModel userModel)
    {
        this.userModel = userModel;
        this.userSingleTone.setUserModel(userModel);
        this.preferences.CreateUpdateUserData(getActivity(),userModel);
        UpdateData(userModel);

    }
}
