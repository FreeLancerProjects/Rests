package com.semicolon.rests.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semicolon.rests.R;
import com.semicolon.rests.adapters.HomePagerAdapter;

public class Fragment_Home extends Fragment {
    private TabLayout tab;
    private ViewPager pager;
    private HomePagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        tab = view.findViewById(R.id.tab);
        pager = view.findViewById(R.id.pager);

        tab.setupWithViewPager(pager);
        adapter = new HomePagerAdapter(getActivity().getSupportFragmentManager());
        adapter.AddFragment(Fragment_Estrahat.getInstance());
        adapter.AddFragment(Fragment_Shalehat.getInstance());
        adapter.AddFragment(Fragment_Kosor.getInstance());

        adapter.AddTitle("إستراحات");
        adapter.AddTitle("شاليهات");
        adapter.AddTitle("قصور افراح");
        pager.setAdapter(adapter);



    }
    public static Fragment_Home getInstance()
    {
        Fragment_Home fragment = new Fragment_Home();
        return fragment;
    }
}
