package com.semicolon.rests.activities;

import android.Manifest;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.semicolon.rests.R;
import com.semicolon.rests.common.Common;
import com.semicolon.rests.fragments.Fragment_Home;
import com.semicolon.rests.fragments.Fragment_Login;
import com.semicolon.rests.fragments.Fragment_More;
import com.semicolon.rests.fragments.Fragment_Notification;
import com.semicolon.rests.fragments.Fragment_Profile;
import com.semicolon.rests.fragments.Fragment_Register;
import com.semicolon.rests.fragments.Fragment_Reservations;
import com.semicolon.rests.models.LocationModel;
import com.semicolon.rests.models.NotificationCountModel;
import com.semicolon.rests.models.ResponsModel;
import com.semicolon.rests.models.UserModel;
import com.semicolon.rests.preferences.Preferences;
import com.semicolon.rests.service.Api;
import com.semicolon.rests.service.LocationService;
import com.semicolon.rests.singletone.UserSingleTone;
import com.semicolon.rests.tags.Tags;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private TextView tv_title;
    private AHBottomNavigation ahBottomNavigation;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private Preferences preferences;
    private AHBottomNavigationItem item1,item2,item3,item4;
    private BottomSheetBehavior behavior;
    private View root;
    private ImageView bottom_sheet_image_back;
    private TextView bottom_sheet_tv_title;
    private AlertDialog not_alert;
    private Toolbar toolBar;
    private ProgressDialog progressDialog;
    private LocationManager locationManager;
    private final String fine_loc = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int loc_req = 5236,gps_req =5256;
    private AlertDialog dialog;
    private Intent intentService;
    private FrameLayout fl_not;
    private TextView tv_not;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }


    private void initView() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        progressDialog = Common.getProgress(this,getString(R.string.logout2));
        preferences = Preferences.getInstance();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        String session = preferences.getSession(this);
        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tv_title = findViewById(R.id.tv_title);
        fl_not = findViewById(R.id.fl_not);
        tv_not = findViewById(R.id.tv_not);

        bottom_sheet_image_back = findViewById(R.id.bottom_sheet_image_back);
        bottom_sheet_tv_title = findViewById(R.id.bottom_sheet_tv_title);

        root = findViewById(R.id.root);
        behavior = BottomSheetBehavior.from(root);

        ahBottomNavigation = findViewById(R.id.ah_nav_bottom);
        ahBottomNavigation.setForceTint(false);
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ahBottomNavigation.setColored(false);
        ahBottomNavigation.setTitleTextSizeInSp(12f,10f);
        ahBottomNavigation.setInactiveColor(ContextCompat.getColor(this,R.color.gray6));
        ahBottomNavigation.setAccentColor(ContextCompat.getColor(this,R.color.colorPrimary));
        ahBottomNavigation.setDefaultBackgroundResource(R.color.white);

         item1 = new AHBottomNavigationItem(getString(R.string.home),R.drawable.home_icon,R.color.gray6);
         item2 = new AHBottomNavigationItem(getString(R.string.reserv),R.drawable.offer_icon,R.color.gray6);
         item3 = new AHBottomNavigationItem(getString(R.string.profile),R.drawable.profile_icon,R.color.gray6);
         item4 = new AHBottomNavigationItem(getString(R.string.more),R.drawable.more_icon,R.color.gray6);


         if (userModel==null)
         {
             AddNavItem(item1);
             AddNavItem(item2);
             AddNavItem(item4);
         }else
             {
                 AddNavItem(item1);
                 AddNavItem(item2);
                 AddNavItem(item3);
                 AddNavItem(item4);
             }


        ahBottomNavigation.setCurrentItem(0,false);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Fragment_Home.getInstance()).commit();
        ahBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                if (userModel!=null)
                {
                    if (position==0)
                    {
                        UpdateTitle(getString(R.string.home));
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Fragment_Home.getInstance()).commit();
                        ahBottomNavigation.setCurrentItem(position,false);
                    } else if (position==1)
                    {
                        userModel = userSingleTone.getUserModel();
                        if (userModel==null)
                        {
                            not_alert.show();
                        }else
                            {
                                UpdateTitle(getString(R.string.reserv));

                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Fragment_Reservations.getInstance()).commit();
                                ahBottomNavigation.setCurrentItem(position,false);

                            }

                    }
                    else if (position==2)
                    {
                        UpdateTitle(getString(R.string.profile));

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Fragment_Profile.getInstance()).commit();
                        ahBottomNavigation.setCurrentItem(position,false);

                    }else if (position==3)
                    {
                        UpdateTitle(getString(R.string.more));

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Fragment_More.getInstance()).commit();
                        ahBottomNavigation.setCurrentItem(position,false);

                    }
                }else
                    {
                        if (position==0)
                        {
                            UpdateTitle(getString(R.string.home));

                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Fragment_Home.getInstance()).commit();
                            ahBottomNavigation.setCurrentItem(position,false);
                        } else if (position==1)
                        {
                            userModel = userSingleTone.getUserModel();

                            if (userModel==null)
                            {
                                not_alert.show();

                            }else
                            {
                                UpdateTitle(getString(R.string.reserv));

                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Fragment_Reservations.getInstance()).commit();
                                ahBottomNavigation.setCurrentItem(position,false);

                            }

                        }
                        else if (position==2)
                        {
                            UpdateTitle(getString(R.string.more));
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Fragment_More.getInstance()).commit();
                            ahBottomNavigation.setCurrentItem(position,false);

                        }
                    }


                return false;
            }
        });

        bottom_sheet_image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState ==BottomSheetBehavior.STATE_DRAGGING)
                {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        fl_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_authentication_container, Fragment_Notification.getInstance()).commit();
                bottom_sheet_tv_title.setText(getString(R.string.notification));
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                ReadNotification();
            }
        });

        getDataFromPreference();
        CreateNotAlertDialog();
        CreateGpsAlertDialog();

        if (isGPS_Open())
        {
            checkPermissions();
        }else
            {
                dialog.show();
            }

            if (session.equals(Tags.session_login))
            {
                UserModel userModel = preferences.getUserData(this);
                UpdateUi(userModel);
            }


    }

    private void ReadNotification() {

        Api.getServices()
                .readNotification(userModel.getUser_id(),"1")
                .enqueue(new Callback<ResponsModel>() {
                    @Override
                    public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                        if (response.isSuccessful())
                        {
                            UpdateNotificationCount(0);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                    }
                });
    }

    private boolean isGPS_Open()
    {

        if (locationManager!=null)
        {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        return false;

    }

    private void CreateGpsAlertDialog()
    {
        View view = LayoutInflater.from(this).inflate(R.layout.custom_gps_dialog,null);
        Button button = view.findViewById(R.id.openBtn);
        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          dialog.dismiss();
                                          openGps();
                                      }
                                  });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
        dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setView(view)
                .create();
        dialog.setCanceledOnTouchOutside(false);

    }

    private void openGps() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent,gps_req);
    }

    private void getDataFromPreference() {
        String session = preferences.getSession(this);
        if (!TextUtils.isEmpty(session)&& session!=null)
        {
            if (session.equals(Tags.session_login))
            {
                UserModel userModel = preferences.getUserData(this);
                this.userModel = userModel;
                userSingleTone.setUserModel(userModel);
            }
        }
    }

    private void AddNavItem(AHBottomNavigationItem item) {
        ahBottomNavigation.addItem(item);
    }


    public void UpdateUi(UserModel userModel) {
        ahBottomNavigation.removeAllItems();
        AddNavItem(item1);
        AddNavItem(item2);
        AddNavItem(item3);
        AddNavItem(item4);

        this.userModel = userModel;
        preferences.CreateUpdateUserData(this,userModel);
        userSingleTone.setUserModel(userModel);
        if (behavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
        {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        ahBottomNavigation.setCurrentItem(0,false);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Fragment_Home.getInstance()).commit();
        UpdateTitle(getString(R.string.home));
        toolBar.getMenu().clear();
        toolBar.inflateMenu(R.menu.home_menu);
        fl_not.setVisibility(View.VISIBLE);
        EventBus.getDefault().register(this);
        StartLocationService();
        UpdateToken();
        DisplayNotification_count();

    }

    private void DisplayNotification_count()
    {
        Api.getServices()
                .getNotificationCount(userModel.getUser_id())
                .enqueue(new Callback<NotificationCountModel>() {
                    @Override
                    public void onResponse(Call<NotificationCountModel> call, Response<NotificationCountModel> response) {
                        if (response.isSuccessful())
                        {
                            UpdateNotificationCount(response.body().getAlert_count());
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationCountModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                    }
                });
    }

    private void UpdateNotificationCount(int count)
    {
        if (count==0)
        {
            tv_not.setVisibility(View.INVISIBLE);
        }else
            {
                tv_not.setVisibility(View.VISIBLE);
                tv_not.setText(String.valueOf(count));

            }
    }
    private void UpdateToken() {
        Task<InstanceIdResult> instanceId = FirebaseInstanceId.getInstance().getInstanceId();
        instanceId.addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful())
                {
                    String token = task.getResult().getToken();
                    Api.getServices()
                            .updateToken(userModel.getUser_id(),token)
                            .enqueue(new Callback<ResponsModel>() {
                                @Override
                                public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {

                                    if (response.isSuccessful())
                                    {
                                        if (response.body().getSuccess_token_id()==1)
                                        {
                                            Log.e("Token_update","successfully");
                                        }else
                                            {
                                                Log.e("Token_update","Failed");

                                            }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponsModel> call, Throwable t) {
                                    Log.e("Error",t.getMessage());
                                }
                            });
                }
            }
        });
    }

    private void StartLocationService()
    {
        intentService = new Intent(this, LocationService.class);
        startService(intentService);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenForNotificationCount(NotificationCountModel notificationCountModel)
    {
        Log.e("noooooot","nooooooooooooooooooot");
        DisplayNotification_count();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateLocation(LocationModel locationModel)
    {
        Api.getServices()
                .updateLocation(userModel.getUser_id(),locationModel.getLat(),locationModel.getLng())
                .enqueue(new Callback<ResponsModel>() {
                    @Override
                    public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {

                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess_location()==1)
                            {
                                Log.e("LocationUpdate","successfully");
                            }else
                                {
                                    Log.e("LocationUpdate","Failed");

                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                    }
                });
    }

    private void UpdateTitle(String title)
    {
        tv_title.setText(title);
    }

    public void DisplayLoginFragment()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_authentication_container, Fragment_Login.getInstance()).commit();
        bottom_sheet_tv_title.setText(getString(R.string.login));
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    public void DisplayRegisterFragment()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_authentication_container, Fragment_Register.getInstance()).commit();
        bottom_sheet_tv_title.setText(getString(R.string.sign_up));
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    private void CreateNotAlertDialog()
    {
        not_alert = new AlertDialog.Builder(this)
                .setCancelable(false)
                .create();
        View view = LayoutInflater.from(this).inflate(R.layout.custom_auth_dialog,null);
        Button btn_login,btn_reg,btn_close;

        btn_login = view.findViewById(R.id.btn_login);
        btn_reg = view.findViewById(R.id.btn_reg);
        btn_close = view.findViewById(R.id.btn_close);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                not_alert.dismiss();
                DisplayLoginFragment();
            }
        });
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                not_alert.dismiss();
                DisplayRegisterFragment();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                not_alert.dismiss();
                ahBottomNavigation.setCurrentItem(0,false);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Fragment_Home.getInstance()).commit();

            }
        });

        not_alert.setView(view);
        not_alert.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment:fragmentList)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode==gps_req)
        {
            if (resultCode==RESULT_CANCELED)
            {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    checkPermissions();
                }else
                    {
                        openGps();
                    }
            }
        }
    }

    private void checkPermissions() {
        String [] perm = {fine_loc};
        if (ContextCompat.checkSelfPermission(this,fine_loc)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,perm,loc_req);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment:fragmentList)
        {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        if (requestCode==loc_req)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]!=PackageManager.PERMISSION_GRANTED)
                {
                    checkPermissions();
                }
            }else
                {
                    checkPermissions();
                }
        }
    }

    @Override
    public void onBackPressed() {
        if (behavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
        {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else if (ahBottomNavigation.getCurrentItem()!=0)
        {
            ahBottomNavigation.setCurrentItem(0,false);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Fragment_Home.getInstance()).commit();
        }
        else
            {
                super.onBackPressed();

            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String session = preferences.getSession(this);
        if (session.equals(Tags.session_login))
        {
            getMenuInflater().inflate(R.menu.home_menu,menu);

        }else
            {
                getMenuInflater().inflate(R.menu.home_second_menu,menu);

            }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();
        if (id==R.id.out)
        {
            finish();
        }else if (id==R.id.logout)
        {
            Logout();
        }
        return true;
    }

    private void Logout() {
        progressDialog.show();
        Api.getServices()
                .logout(userModel.getUser_id())
                .enqueue(new Callback<ResponsModel>() {
                    @Override
                    public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            if (response.body().getSuccess_logout()==1)
                            {
                                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                manager.cancelAll();
                                userSingleTone.clearUserData();
                                preferences.CreateUpdateSession(HomeActivity.this,Tags.session_logout);
                                finish();

                            }else if (response.body().getSuccess_logout()==0)
                            {
                                Toast.makeText(HomeActivity.this,R.string.something_error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.e("Error",t.getMessage());
                        Toast.makeText(HomeActivity.this,R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (intentService!=null)
        {
            EventBus.getDefault().unregister(this);
            stopService(intentService);
        }
        super.onDestroy();
    }
}
