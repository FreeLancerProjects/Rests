package com.semicolon.rests.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.lamudi.phonefield.PhoneInputLayout;
import com.semicolon.rests.R;
import com.semicolon.rests.activities.HomeActivity;
import com.semicolon.rests.adapters.CityAdapter;
import com.semicolon.rests.common.Common;
import com.semicolon.rests.models.CityModel;
import com.semicolon.rests.models.UserModel;
import com.semicolon.rests.preferences.Preferences;
import com.semicolon.rests.service.Api;
import com.semicolon.rests.tags.Tags;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Register extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {
    private Button btn_signup;
    private EditText edt_name, edt_phone, edt_email, edt_password;
    private TextView tv_city;
    private PhoneInputLayout edt_check_phone;
    private String city_id = "";
    private ProgressDialog progressDialog,progressDialog2;
    private AlertDialog city_alert;
    private List<CityModel> cityModelList;
    private HomeActivity homeActivity;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private final int displacement = 10;
    private final String FineLoc = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int per_req = 1202, gps = 2520;
    private double myLat = 0.0, myLng = 0.0;
    private LocationManager manager;
    private Preferences preferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        preferences = Preferences.getInstance();
        homeActivity = (HomeActivity) getActivity();
        progressDialog = Common.getProgress(getActivity(), getString(R.string.wait));
        manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        btn_signup = view.findViewById(R.id.btn_signup);
        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_email = view.findViewById(R.id.edt_email);
        tv_city = view.findViewById(R.id.tv_city);
        edt_password = view.findViewById(R.id.edt_password);
        edt_check_phone = view.findViewById(R.id.edt_check_phone);


        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });

        tv_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityModelList.size() > 0) {
                    UpdateCityUi(cityModelList);

                } else {
                    getCityData();
                }
            }
        });
        getCityData();
        if (isGpsOpened()) {
            initGoogleApi();

        } else {
            OpenGps();
        }

    }

    private void OpenGps() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        getActivity().startActivityForResult(intent, gps);
    }

    private boolean isGpsOpened() {
        if (manager != null) {
            return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        return false;
    }

    private void initGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    public static Fragment_Register getInstance() {
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
                        if (response.isSuccessful()) {
                            progressDialog.dismiss();
                            Fragment_Register.this.cityModelList = response.body();

                        }
                    }

                    @Override
                    public void onFailure(Call<List<CityModel>> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.e("Error", t.getMessage());
                        Toast.makeText(getActivity(), R.string.something_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void UpdateCityUi(List<CityModel> cityModelList) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.alert_city_layout, null);
        RecyclerView recView = view.findViewById(R.id.recView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        RecyclerView.Adapter adapter = new CityAdapter(getActivity(), cityModelList, this);
        recView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        city_alert = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .create();
        city_alert.setCanceledOnTouchOutside(false);
        city_alert.setView(view);
        city_alert.getWindow().getAttributes().windowAnimations = R.style.custom_dialog;
        city_alert.show();


    }

    public void setItemCity(CityModel cityModel) {
        tv_city.setText(cityModel.getCity_title());
        city_id = cityModel.getCity_id();
        city_alert.dismiss();

    }

    private void CheckData() {
        String m_name = edt_name.getText().toString();
        String m_phone = edt_phone.getText().toString();
        String m_email = edt_email.getText().toString();
        String m_city = tv_city.getText().toString();
        String m_password = edt_password.getText().toString();
        if (!m_phone.startsWith("+")) {
            m_phone = "+" + m_phone;
        }
        edt_check_phone.setPhoneNumber(m_phone);

        //متنساش تحط المدينة وتعمل عليها اتشك
        if (!TextUtils.isEmpty(m_name) && !TextUtils.isEmpty(m_phone) && edt_check_phone.isValid() && !TextUtils.isEmpty(m_city) && !TextUtils.isEmpty(m_password) && m_password.length() >= 5) {
            if (TextUtils.isEmpty(m_email)) {
                edt_name.setError(null);
                edt_email.setError(null);
                edt_phone.setError(null);
                tv_city.setError(null);
                edt_password.setError(null);

                SignUp(m_name, m_phone, m_email, city_id, m_password);

            } else {
                if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches()) {
                    edt_email.setError(getString(R.string.inv_email));

                } else {
                    edt_name.setError(null);
                    edt_email.setError(null);
                    edt_phone.setError(null);
                    tv_city.setError(null);
                    edt_password.setError(null);

                    SignUp(m_name, m_phone, m_email, city_id, m_password);

                }
            }
        } else {
            if (TextUtils.isEmpty(m_name)) {
                edt_name.setError(getString(R.string.name_req));
            } else {
                edt_name.setError(null);

            }

            if (TextUtils.isEmpty(m_phone)) {
                edt_phone.setError(getString(R.string.phone_req));
            } else if (!edt_check_phone.isValid()) {
                edt_phone.setError(getString(R.string.inv_phone));

            } else {
                edt_phone.setError(null);

            }

            if (!TextUtils.isEmpty(m_email)) {
                if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches()) {
                    edt_email.setError(getString(R.string.inv_email));
                } else {
                    edt_email.setError(null);

                }
            }

            if (TextUtils.isEmpty(city_id)) {
                tv_city.setError(getString(R.string.city_req));
            } else {
                tv_city.setError(null);

            }
            if (TextUtils.isEmpty(m_password)) {
                edt_password.setError(getString(R.string.password_req));
            } else if (m_password.length() < 5) {
                edt_password.setError(getString(R.string.pass_short));

            } else {
                edt_password.setError(null);

            }
        }


    }

    private void SignUp(String name, String phone, String email, String city, String password) {
        progressDialog2 = Common.getProgress(getActivity(),getString(R.string.sign_inn));
        progressDialog2.show();
        Api.getServices()
                .register(password,phone,name,"",String.valueOf(myLat),String.valueOf(myLng),email,city_id)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful()){
                            progressDialog2.dismiss();
                            if (response.body().getSuccess_signup()==0)
                            {
                                Toast.makeText(homeActivity, R.string.something_error, Toast.LENGTH_SHORT).show();

                            }else  if (response.body().getSuccess_signup()==1)
                            {

                                String session = preferences.getSession(getActivity());
                                if (session.equals(Tags.session_login))
                                {
                                    Toast.makeText(getActivity(), R.string.reg_succ, Toast.LENGTH_SHORT).show();
                                }else
                                    {
                                        homeActivity.UpdateUi(response.body());

                                    }

                            }else  if (response.body().getSuccess_signup()==2)
                            {
                                Toast.makeText(homeActivity, R.string.phone_email_reg_before, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        progressDialog2.dismiss();
                        Log.e("Error",t.getMessage());
                        Toast.makeText(homeActivity, R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationRequest();
        startLocationUpdate();
    }

    private void startLocationUpdate() {


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[]{FineLoc};
            ActivityCompat.requestPermissions(getActivity(), permissions, per_req);

        }

        LocationServices.getFusedLocationProviderClient(getActivity()).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        }, Looper.myLooper());

    }

    private void initLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setSmallestDisplacement(displacement);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        myLat = location.getLatitude();
        myLng = location.getLongitude();

        Log.e("latupdate", myLat + "");
        Log.e("longupdate", myLng + "");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == gps) {
            if (resultCode == Activity.RESULT_CANCELED) {
                if (isGpsOpened()) {
                    initGoogleApi();
                } else {
                    OpenGps();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == per_req) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        String[] permission = new String[]{FineLoc};
                        ActivityCompat.requestPermissions(getActivity(), permission, per_req);

                    }

                    LocationServices.getFusedLocationProviderClient(getActivity()).requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            onLocationChanged(locationResult.getLastLocation());
                        }
                    }, Looper.myLooper());

                }
            }else
                {
                    String [] permission = new String[]{FineLoc};
                    ActivityCompat.requestPermissions(getActivity(),permission,per_req);

                }
        }
    }

    @Override
    public void onDestroy() {
        if (googleApiClient!=null)
        {
            LocationServices.getFusedLocationProviderClient(getActivity())
                    .removeLocationUpdates(new LocationCallback());
            googleApiClient.disconnect();
        }
        super.onDestroy();

    }
}
