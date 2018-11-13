package com.semicolon.rests.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.semicolon.rests.R;
import com.semicolon.rests.activities.BankActivity;
import com.semicolon.rests.activities.ContactActivity;
import com.semicolon.rests.activities.HomeActivity;
import com.semicolon.rests.activities.SupportActivity;
import com.semicolon.rests.activities.TermsActivity;
import com.semicolon.rests.models.UserModel;
import com.semicolon.rests.preferences.Preferences;
import com.semicolon.rests.singletone.UserSingleTone;
import com.semicolon.rests.tags.Tags;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_More extends Fragment {

    private TextView tv_terms,tv_bank,tv_contact,tv_support,tv_login,tv_register,tv_name;
    private Animation animation;
    private int type=0;
    private HomeActivity homeActivity;
    private Preferences preferences;
    private CircleImageView image;
    private UserSingleTone userSingleTone;
    private UserModel userModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more,container,false);
        initView(view);
        return view;

    }

    private void initView(View view) {

        homeActivity = (HomeActivity) getActivity();
        tv_login = view.findViewById(R.id.tv_login);
        tv_register = view.findViewById(R.id.tv_register);
        tv_terms = view.findViewById(R.id.tv_terms_condition);
        tv_bank = view.findViewById(R.id.tv_bank);
        tv_contact = view.findViewById(R.id.tv_contact);
        //tv_support = view.findViewById(R.id.tv_support);
        image = view.findViewById(R.id.image);
        tv_name = view.findViewById(R.id.tv_name);
        image.setImageResource(R.drawable.logo);
        animation = AnimationUtils.loadAnimation(getActivity(),R.anim.press_anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (type==1)
                {
                    type = 0;
                    homeActivity.DisplayLoginFragment();
                }else if (type==2)
                {
                    type = 0;
                    homeActivity.DisplayRegisterFragment();
                }else if (type==3)
                {
                    type = 0;
                    Intent intent = new Intent(getActivity(), TermsActivity.class);
                    startActivity(intent);

                }
                else if (type==4)
                {
                    type = 0;
                    Intent intent = new Intent(getActivity(), BankActivity.class);
                    startActivity(intent);
                }else if (type==5)
                {
                    type = 0;
                    Intent intent = new Intent(getActivity(), ContactActivity.class);
                    startActivity(intent);

                }else if (type==6)
                {
                    type = 0;
                    Intent intent = new Intent(getActivity(), SupportActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                tv_login.clearAnimation();
                tv_login.startAnimation(animation);
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                tv_register.clearAnimation();
                tv_register.startAnimation(animation);
            }
        });
        tv_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 3;
                tv_terms.clearAnimation();
                tv_terms.startAnimation(animation);

            }
        });
        tv_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 4;
                tv_bank.clearAnimation();
                tv_bank.startAnimation(animation);

            }
        });
        tv_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 5;
                tv_contact.clearAnimation();
                tv_contact.startAnimation(animation);

            }
        });
       /* tv_support .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 6;
                tv_support.clearAnimation();
                tv_support.startAnimation(animation);

            }
        });*/



    }

    @Override
    public void onStart() {
        super.onStart();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        preferences = Preferences.getInstance();
        String session = preferences.getSession(getActivity());
        if (session.equals(Tags.session_login))
        {
            tv_login.setVisibility(View.GONE);
        }else
        {
            tv_login.setVisibility(View.VISIBLE);
        }

        if (userModel!=null)
        {

            tv_name.setText(userModel.getUser_full_name());
        }
    }

    public static Fragment_More getInstance()
    {
        Fragment_More fragment = new Fragment_More();
        return fragment;
    }
}
