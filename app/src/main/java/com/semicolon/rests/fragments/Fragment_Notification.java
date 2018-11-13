package com.semicolon.rests.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.semicolon.rests.R;
import com.semicolon.rests.adapters.Notification_Adapter;
import com.semicolon.rests.models.Notification_Model;
import com.semicolon.rests.models.UserModel;
import com.semicolon.rests.service.Api;
import com.semicolon.rests.singletone.UserSingleTone;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Notification extends Fragment {
    private RecyclerView recView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private ProgressBar progressBar;
    private LinearLayout ll_no_notification;
    private List<Notification_Model> notification_modelList;
    private UserModel userModel;
    private UserSingleTone userSingleTone;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Notification getInstance(){
        return new Fragment_Notification();
    }

    private void initView(View view) {
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        notification_modelList = new ArrayList<>();
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        adapter = new Notification_Adapter(getActivity(),notification_modelList);
        recView.setAdapter(adapter);
        recView.setNestedScrollingEnabled(false);
        progressBar = view.findViewById(R.id.progBar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        ll_no_notification = view.findViewById(R.id.ll_no_notification);
    }

    @Override
    public void onStart() {
        super.onStart();
        getNotificationData();

    }

    private void getNotificationData() {
        Api.getServices()
                .displayNotification(userModel.getUser_id())
                .enqueue(new Callback<List<Notification_Model>>() {
                    @Override
                    public void onResponse(Call<List<Notification_Model>> call, Response<List<Notification_Model>> response) {

                        if (response.isSuccessful())
                        {
                            notification_modelList.clear();
                            progressBar.setVisibility(View.GONE);
                            notification_modelList.addAll(response.body());
                            if (notification_modelList.size()>0)
                            {
                                adapter.notifyDataSetChanged();
                                ll_no_notification.setVisibility(View.GONE);
                            }else
                                {
                                    ll_no_notification.setVisibility(View.VISIBLE);

                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Notification_Model>> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }
}
