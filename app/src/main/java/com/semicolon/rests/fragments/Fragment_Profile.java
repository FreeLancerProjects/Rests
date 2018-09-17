package com.semicolon.rests.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semicolon.rests.R;

public class Fragment_Profile extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        initView(view);
        return view;

    }

    private void initView(View view) {

    }
    public static Fragment_Profile getInstance()
    {
        Fragment_Profile fragment = new Fragment_Profile();
        return fragment;
    }
}
