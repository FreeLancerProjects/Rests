package com.semicolon.rests.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.semicolon.rests.R;
import com.semicolon.rests.activities.PlaceProfileActivity;
import com.semicolon.rests.adapters.PlacesAdapter;
import com.semicolon.rests.models.PlacesModel;
import com.semicolon.rests.models.UserModel;
import com.semicolon.rests.service.Api;
import com.semicolon.rests.singletone.UserSingleTone;
import com.semicolon.rests.tags.Tags;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Kosor extends Fragment implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener,GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {
    private TextView tv_search;
    private ImageView image_filter,image_clear;
    private TextView tv_not;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private List<PlacesModel> placesModelList;
    private ProgressBar progressBar;
    private SwipeRefreshLayout sr;
    private DatePickerDialog datePickerDialog;
    private AlertDialog alertDialog;
    private int filter_flag=0;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private final int displacement = 10;
    private final String FineLoc = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int per_req = 1202, gps = 2520;
    private double myLat = 0.0, myLng = 0.0;
    private LocationManager locationManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kosor,container,false);
        initView(view);
        CreateDateDialog();
        CreateAlertDialog();
        return view;

    }

    private void initView(View view) {

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        placesModelList = new ArrayList<>();
        image_clear = view.findViewById(R.id.img_close);
        tv_search = view.findViewById(R.id.tv_search);
        sr = view.findViewById(R.id.sr);
        tv_not = view.findViewById(R.id.tv_not);
        progressBar = view.findViewById(R.id.proBar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        image_filter= view.findViewById(R.id.img_filter);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        adapter = new PlacesAdapter(getActivity(),placesModelList,this);
        recView.setAdapter(adapter);
        sr.setColorSchemeResources(R.color.colorPrimary,R.color.redd,R.color.green,R.color.yellow);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(getActivity().getFragmentManager(),"Select Date");
            }
        });
        image_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_filter.setImageResource(R.drawable.filter2);
                alertDialog.show();

            }
        });
        image_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_search.setText("");
                image_clear.setVisibility(View.INVISIBLE);
                if (userModel==null)
                {
                    sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getData("all");
                        }
                    });

                    getData("all");
                }else
                {
                    sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getData(userModel.getUser_id());
                        }
                    });

                    getData(userModel.getUser_id());
                }
            }
        });

        initGoogleApi();

    }



    private void initGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }
    @Override
    public void onStart() {
        super.onStart();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        if (userModel==null)
        {
            sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getData("all");
                }
            });

            getData("all");
        }else
        {
            sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getData(userModel.getUser_id());
                }
            });

            getData(userModel.getUser_id());
        }
    }
    private void CreateDateDialog()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        datePickerDialog = DatePickerDialog.newInstance
                (this,
                        calendar.get(Calendar.YEAR)
                        ,calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setMinDate(calendar);
        datePickerDialog.setAccentColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
    }
    private void CreateAlertDialog()
    {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.filter_dialog,null);
        LinearLayout ll_price = view.findViewById(R.id.ll_price);
        LinearLayout ll_nearby = view.findViewById(R.id.ll_nearby);
        LinearLayout ll_nofilter = view.findViewById(R.id.ll_nofilter);

        final RadioButton rb_price = view.findViewById(R.id.rb_price);
        final RadioButton  rb_nearby = view.findViewById(R.id.rb_nearby);
        final RadioButton  rb_nofilter = view.findViewById(R.id.rb_nofilter);

        if (filter_flag==0)
        {
            rb_nofilter.setChecked(true);
            rb_price.setChecked(false);
            rb_nearby.setChecked(false);

        }else if (filter_flag==1)
        {
            rb_nofilter.setChecked(false);
            rb_price.setChecked(true);
            rb_nearby.setChecked(false);

        }else if (filter_flag==2)
        {
            rb_nofilter.setChecked(false);
            rb_price.setChecked(false);
            rb_nearby.setChecked(true);
        }

        rb_nofilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_nofilter.isChecked())
                {
                    filter_flag=0;
                    rb_nofilter.setChecked(true);
                    rb_price.setChecked(false);
                    rb_nearby.setChecked(false);
                    image_filter.setImageResource(R.drawable.filter);

                    alertDialog.dismiss();

                    if (userModel==null)
                    {
                        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                getData("all");
                            }
                        });

                        getData("all");
                    }else
                    {
                        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                getData(userModel.getUser_id());
                            }
                        });

                        getData(userModel.getUser_id());
                    }
                }
            }
        });

        rb_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_price.isChecked())
                {
                    filter_flag=1;
                    rb_nofilter.setChecked(false);
                    rb_price.setChecked(true);
                    rb_nearby.setChecked(false);
                    image_filter.setImageResource(R.drawable.filter);

                    alertDialog.dismiss();
                    if (userModel==null)
                    {
                        search_by_cost("all");

                    }else
                    {
                        search_by_cost(userModel.getUser_id());

                    }

                }
            }
        });

        rb_nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_nearby.isChecked())
                {
                    filter_flag=2;
                    rb_nofilter.setChecked(false);
                    rb_price.setChecked(false);
                    rb_nearby.setChecked(true);
                    image_filter.setImageResource(R.drawable.filter);

                    alertDialog.dismiss();
                    if (userModel==null)
                    {
                        search_nearby("all");

                    }else
                    {
                        search_nearby(userModel.getUser_id());

                    }

                }
            }
        });



///////////////////////////////////////////////////////////////////////////
        ll_nofilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_flag=0;
                rb_nofilter.setChecked(true);
                rb_price.setChecked(false);
                rb_nearby.setChecked(false);
                image_filter.setImageResource(R.drawable.filter);

                alertDialog.dismiss();
                if (userModel==null)
                {
                    sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getData("all");
                        }
                    });

                    getData("all");
                }else
                {
                    sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getData(userModel.getUser_id());
                        }
                    });

                    getData(userModel.getUser_id());
                }
            }
        });

        ll_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filter_flag=1;
                rb_nofilter.setChecked(false);
                rb_price.setChecked(true);
                rb_nearby.setChecked(false);
                image_filter.setImageResource(R.drawable.filter);

                alertDialog.dismiss();
                if (userModel==null)
                {
                    search_by_cost("all");

                }else
                {
                    search_by_cost(userModel.getUser_id());

                }
            }
        });

        ll_nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filter_flag=2;
                rb_nofilter.setChecked(false);
                rb_price.setChecked(false);
                rb_nearby.setChecked(true);
                image_filter.setImageResource(R.drawable.filter);

                alertDialog.dismiss();
                if (userModel==null)
                {
                    search_nearby("all");

                }else
                {
                    search_nearby(userModel.getUser_id());

                }
            }
        });

        alertDialog = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .create();

        alertDialog.getWindow().getAttributes().windowAnimations=R.style.custom_dialog;
        alertDialog.setView(view);
    }
    private void search_by_cost(String user_id) {
        progressBar.setVisibility(View.VISIBLE);
        Api.getServices()
                .search_by_cost(user_id,Tags.type_kosor)
                .enqueue(new Callback<List<PlacesModel>>() {
                    @Override
                    public void onResponse(Call<List<PlacesModel>> call, Response<List<PlacesModel>> response) {
                        if (response.isSuccessful())
                        {
                            progressBar.setVisibility(View.GONE);
                            placesModelList.clear();
                            placesModelList.addAll(response.body());
                            adapter.notifyDataSetChanged();

                            if (placesModelList.size()>0)
                            {
                                tv_not.setVisibility(View.GONE);
                            }else
                            {
                                tv_not.setText("لا توجد قصور افراح لعرضها");
                                tv_not.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PlacesModel>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("Error",t.getMessage());
                        Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void search_nearby(String user_id)
    {
        Api.getServices()
                .search_nearby(user_id,Tags.type_kosor,myLat,myLng)
                .enqueue(new Callback<List<PlacesModel>>() {
                    @Override
                    public void onResponse(Call<List<PlacesModel>> call, Response<List<PlacesModel>> response) {
                        if (response.isSuccessful())
                        {
                            placesModelList.clear();
                            placesModelList.addAll(response.body());
                            adapter.notifyDataSetChanged();

                            if (placesModelList.size()>0)
                            {
                                tv_not.setVisibility(View.GONE);

                            }else
                            {
                                tv_not.setText("لا توجد قصور افراح لعرضها");
                                tv_not.setVisibility(View.VISIBLE);

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PlacesModel>> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void getData(String user_id)
    {
        placesModelList.clear();
        Api.getServices()
                .getPlaces(user_id,Tags.type_kosor)
                .enqueue(new Callback<List<PlacesModel>>() {
                    @Override
                    public void onResponse(Call<List<PlacesModel>> call, Response<List<PlacesModel>> response) {
                        if (response.isSuccessful())
                        {
                            sr.setRefreshing(false);

                            progressBar.setVisibility(View.GONE);
                            if (response.body().size()>0)
                            {

                                tv_not.setVisibility(View.GONE);
                                placesModelList.addAll(response.body());
                                adapter.notifyDataSetChanged();
                            }else
                            {
                                tv_not.setText("لا توجد قصور افراح لعرضها");
                                tv_not.setVisibility(View.VISIBLE);

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PlacesModel>> call, Throwable t) {
                        sr.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                        Log.e("Error",t.getMessage());
                        Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void setItem(PlacesModel placesModel,View view)
    {
        Intent intent = new Intent(getActivity(), PlaceProfileActivity.class);
        intent.putExtra("data",placesModel);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),view,"image");

        getActivity().startActivity(intent,options.toBundle());
    }
    public static Fragment_Kosor getInstance()
    {
        Fragment_Kosor fragment = new Fragment_Kosor();
        return fragment;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        calendar.set(Calendar.MONTH,monthOfYear);
        calendar.set(Calendar.YEAR,year);

        Date date = new Date(calendar.getTimeInMillis());
        Date date_now = new Date(now.getTimeInMillis());

        String m_date= dayOfMonth+"-"+(monthOfYear+1)+"-"+year;

        if (date.before(date_now))
        {
            Toast.makeText(getActivity(), R.string.date_old, Toast.LENGTH_LONG).show();
        }else
        {
            image_clear.setVisibility(View.VISIBLE);

            tv_search.setText(m_date);

            if (userModel==null)
            {
                filter_by_date(m_date,"all");

            }else
            {
                filter_by_date(m_date,userModel.getUser_id());

            }

        }
    }

    private void filter_by_date(String m_date, String user_id) {
        progressBar.setVisibility(View.VISIBLE);
        Api.getServices().search_by_date(user_id,Tags.type_kosor,m_date)
                .enqueue(new Callback<List<PlacesModel>>() {
                    @Override
                    public void onResponse(Call<List<PlacesModel>> call, Response<List<PlacesModel>> response) {
                        if (response.isSuccessful())
                        {
                            progressBar.setVisibility(View.GONE);

                            placesModelList.clear();
                            placesModelList.addAll(response.body());
                            adapter.notifyDataSetChanged();
                            if (placesModelList.size()>0)
                            {
                                tv_not.setText("لا توجد قصور افراح متاحة في هذا الموعد");
                                tv_not.setVisibility(View.GONE);
                            }else
                            {
                                tv_not.setText("لا توجد قصور افراح متاحة في هذا الموعد");
                                tv_not.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PlacesModel>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("Error",t.getMessage());
                        Toast.makeText(getActivity(), R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

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
