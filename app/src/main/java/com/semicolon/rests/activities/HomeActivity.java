package com.semicolon.rests.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.semicolon.rests.R;
import com.semicolon.rests.fragments.Fragment_Home;
import com.semicolon.rests.fragments.Fragment_Login;
import com.semicolon.rests.fragments.Fragment_More;
import com.semicolon.rests.fragments.Fragment_Profile;
import com.semicolon.rests.fragments.Fragment_Register;
import com.semicolon.rests.fragments.Fragment_Reservations;
import com.semicolon.rests.models.UserModel;
import com.semicolon.rests.singletone.UserSingleTone;

public class HomeActivity extends AppCompatActivity {

    private ImageView image_back;
    private TextView tv_title;
    private AHBottomNavigation ahBottomNavigation;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private AHBottomNavigationItem item1,item2,item3,item4;
    private BottomSheetBehavior behavior;
    private View root;
    private ImageView bottom_sheet_image_back;
    private TextView bottom_sheet_tv_title;
    private AlertDialog not_alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }


    private void initView() {
        userSingleTone = UserSingleTone.getInstance();
        image_back = findViewById(R.id.image_back);
        tv_title = findViewById(R.id.tv_title);

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

        AddNavItem(item1);
        AddNavItem(item2);
        AddNavItem(item4);

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

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

        CreateNotAlertDialog();


    }

    private void AddNavItem(AHBottomNavigationItem item) {
        ahBottomNavigation.addItem(item);
    }


    public void UpdateUi(UserModel userModel) {
        AddNavItem(item3);
    }
    private void UpdateTitle(String title)
    {
        tv_title.setText(title);
    }

    private void DisplayLoginFragment()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_authentication_container, Fragment_Login.getInstance()).commit();
        bottom_sheet_tv_title.setText(getString(R.string.login));
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    private void DisplayRegisterFragment()
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
}
