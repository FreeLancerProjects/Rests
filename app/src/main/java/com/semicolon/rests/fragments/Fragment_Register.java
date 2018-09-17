package com.semicolon.rests.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lamudi.phonefield.PhoneInputLayout;
import com.semicolon.rests.R;
import com.semicolon.rests.activities.HomeActivity;
import com.semicolon.rests.adapters.CityAdapter;
import com.semicolon.rests.common.Common;
import com.semicolon.rests.models.CityModel;
import com.semicolon.rests.service.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Register extends Fragment {
    private Button btn_signup;
    private EditText edt_name,edt_phone,edt_email,edt_password;
    private TextView tv_city;
    private PhoneInputLayout edt_check_phone;
    private String city_id ="";
    private ProgressDialog progressDialog;
    private AlertDialog city_alert;
    private List<CityModel> cityModelList;
    private HomeActivity homeActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,container,false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        progressDialog = Common.getProgress(getActivity(),"Wait....");
        btn_signup     = view.findViewById(R.id.btn_signup);
        edt_name       = view.findViewById(R.id.edt_name);
        edt_phone      = view.findViewById(R.id.edt_phone);
        edt_email      = view.findViewById(R.id.edt_email);
        tv_city        = view.findViewById(R.id.tv_city);
        edt_password   = view.findViewById(R.id.edt_password);
        edt_check_phone= view.findViewById(R.id.edt_check_phone);



        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });

        tv_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityModelList.size()>0)
                {
                    UpdateCityUi(cityModelList);

                }else
                {
                    getCityData();
                }
            }
        });
        getCityData();
    }

    public static Fragment_Register getInstance()
    {
        Fragment_Register fragment = new Fragment_Register();
        return fragment;
    }
    private void getCityData() {
        progressDialog.show();
        Api.getServices()
                .getCity()
                .enqueue(new Callback<List<CityModel>>() {
                    @Override
                    public void onResponse(Call<List<CityModel>> call, Response<List<CityModel>> response) {
                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Fragment_Register.this.cityModelList = response.body();

                        }
                    }

                    @Override
                    public void onFailure(Call<List<CityModel>> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.e("Error",t.getMessage());
                        Toast.makeText(getActivity(), R.string.something_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void UpdateCityUi(List<CityModel> cityModelList)
    {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.alert_city_layout,null);
        RecyclerView recView = view.findViewById(R.id.recView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        RecyclerView.Adapter adapter = new CityAdapter(getActivity(),cityModelList,this);
        recView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        city_alert = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .create();
        city_alert.setCanceledOnTouchOutside(false);
        city_alert.setView(view);
        city_alert.getWindow().getAttributes().windowAnimations=R.style.custom_dialog;
        city_alert.show();


    }

    public void setItemCity(CityModel cityModel)
    {
        tv_city.setText(cityModel.getCity_title());
        city_id=cityModel.getCity_id();
        city_alert.dismiss();

    }
    private void CheckData() {
        String m_name = edt_name.getText().toString();
        String m_phone = edt_phone.getText().toString();
        String m_email = edt_email.getText().toString();
        String m_city = tv_city.getText().toString();
        String m_password = edt_password.getText().toString();
        if (!m_phone.startsWith("+"))
        {
            m_phone = "+"+m_phone;
        }
        edt_check_phone.setPhoneNumber(m_phone);

        //متنساش تحط المدينة وتعمل عليها اتشك
        if (!TextUtils.isEmpty(m_name)&&!TextUtils.isEmpty(m_phone)&&edt_check_phone.isValid()&&!TextUtils.isEmpty(m_city)&&!TextUtils.isEmpty(m_password)&&m_password.length()>=5)
        {
            if (TextUtils.isEmpty(m_email))
            {
                edt_name.setError(null);
                edt_email.setError(null);
                edt_phone.setError(null);
                tv_city.setError(null);
                edt_password.setError(null);

                SignUp(m_name,m_phone,m_email, city_id,m_password);

            }else
            {
                if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
                {
                    edt_email.setError("Invalid email");

                }else
                {
                    edt_name.setError(null);
                    edt_email.setError(null);
                    edt_phone.setError(null);
                    tv_city.setError(null);
                    edt_password.setError(null);

                    SignUp(m_name,m_phone,m_email, city_id,m_password);

                }
            }
        }else
        {
            if (TextUtils.isEmpty(m_name))
            {
                edt_name.setError("Name is required");
            } else
            {
                edt_name.setError(null);

            }

            if (TextUtils.isEmpty(m_phone))
            {
                edt_phone.setError("Phone number is required");
            }else if (!edt_check_phone.isValid())
            {
                edt_phone.setError("Invalid Phone number");

            }
            else
            {
                edt_phone.setError(null);

            }

            if (!TextUtils.isEmpty(m_email))
            {
                if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
                {
                    edt_email.setError("Invalid email");
                }else
                {
                    edt_email.setError(null);

                }
            }

            if (TextUtils.isEmpty(city_id))
            {
                tv_city.setError("City is required");
            }else
            {
                tv_city.setError(null);

            }
            if (TextUtils.isEmpty(m_password))
            {
                edt_password.setError("Password is required");
            }else if (m_password.length()<5)
            {
                edt_password.setError("Password is to short minimum 5 characters");

            }else
            {
                edt_password.setError(null);

            }
        }



    }

    private void SignUp(String name, String phone, String email, String city, String password) {
        Toast.makeText(getActivity(), "true", Toast.LENGTH_SHORT).show();
    }

}
