package com.semicolon.rests.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lamudi.phonefield.PhoneInputLayout;
import com.semicolon.rests.R;
import com.semicolon.rests.adapters.CityAdapter;
import com.semicolon.rests.common.Common;
import com.semicolon.rests.models.CityModel;
import com.semicolon.rests.service.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private LinearLayout ll_login;
    private Button btn_signup;
    private EditText edt_name,edt_phone,edt_email,edt_password;
    private TextView tv_city;
    private PhoneInputLayout edt_check_phone;
    private String city_id ="";
    private ProgressDialog progressDialog;
    private AlertDialog city_alert;
    private List<CityModel> cityModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();


    }

    private void initView() {
        progressDialog = Common.getProgress(this,"Wait....");
        ll_login = findViewById(R.id.ll_login);
        btn_signup = findViewById(R.id.btn_signup);
        edt_name = findViewById(R.id.edt_name);
        edt_phone = findViewById(R.id.edt_phone);
        edt_email = findViewById(R.id.edt_email);
        tv_city = findViewById(R.id.tv_city);
        edt_password = findViewById(R.id.edt_password);
        edt_check_phone = findViewById(R.id.edt_check_phone);


        final Animation animation = AnimationUtils.loadAnimation(this,R.anim.press_anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ll_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_login.clearAnimation();
                ll_login.startAnimation(animation);
            }
        });

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
                            RegisterActivity.this.cityModelList = response.body();

                        }
                    }

                    @Override
                    public void onFailure(Call<List<CityModel>> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.e("Error",t.getMessage());
                        Toast.makeText(RegisterActivity.this, R.string.somthing_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void UpdateCityUi(List<CityModel> cityModelList)
    {
        View view = LayoutInflater.from(this).inflate(R.layout.alert_city_layout,null);
        RecyclerView recView = view.findViewById(R.id.recView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recView.setLayoutManager(manager);
        RecyclerView.Adapter adapter = new CityAdapter(this,cityModelList);
        recView.setAdapter(adapter);

        city_alert = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();
        city_alert.setCanceledOnTouchOutside(false);
        city_alert.setView(view);
        city_alert.getWindow().getAttributes().windowAnimations=R.style.custom_dialog;
        city_alert.show();


    }

    public void setItemCity(CityModel cityModel)
    {
        //tv_city.setText(cityModel.);
        //city_id=cityModel.
        city_alert.dismiss();

    }
    private void CheckData() {
        String m_name = edt_name.getText().toString();
        String m_phone = edt_phone.getText().toString();
        String m_email = edt_email.getText().toString();
        String m_password = edt_password.getText().toString();
        if (!m_phone.startsWith("+"))
        {
            m_phone = "+"+m_phone;
        }
        edt_check_phone.setPhoneNumber(m_phone);

        //متنساش تحط المدينة وتعمل عليها اتشك
        if (!TextUtils.isEmpty(m_name)&&!TextUtils.isEmpty(m_phone)&&edt_check_phone.isValid()&&!TextUtils.isEmpty(m_password)&&m_password.length()>=5)
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

                /*if (TextUtils.isEmpty(city_id))
                {
                    tv_city.setError("City is required");
                }else
                    {
                        tv_city.setError(null);

                    }*/
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
        Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
    }

}
