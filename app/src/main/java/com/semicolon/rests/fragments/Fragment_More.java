package com.semicolon.rests.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.semicolon.rests.R;
import com.semicolon.rests.activities.BankActivity;
import com.semicolon.rests.activities.ContactActivity;
import com.semicolon.rests.activities.SupportActivity;
import com.semicolon.rests.activities.TermsActivity;

public class Fragment_More extends Fragment {

    private TextView tv_terms,tv_bank,tv_contact,tv_support;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more,container,false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        tv_terms = view.findViewById(R.id.tv_terms_condition);
        tv_bank = view.findViewById(R.id.tv_bank);
        tv_contact = view.findViewById(R.id.tv_contact);
        tv_support = view.findViewById(R.id.tv_support);

        tv_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TermsActivity.class);
                startActivity(intent);
            }
        });
        tv_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BankActivity.class);
                startActivity(intent);
            }
        });
        tv_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContactActivity.class);
                startActivity(intent);
            }
        });
        tv_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SupportActivity.class);
                startActivity(intent);
            }
        });

    }

    public static Fragment_More getInstance()
    {
        Fragment_More fragment = new Fragment_More();
        return fragment;
    }
}
