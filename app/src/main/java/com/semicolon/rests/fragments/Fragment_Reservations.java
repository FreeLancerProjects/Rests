package com.semicolon.rests.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.rests.R;
import com.semicolon.rests.activities.PlaceProfileActivity;
import com.semicolon.rests.adapters.ReservationAdapter;
import com.semicolon.rests.models.PlacesModel;
import com.semicolon.rests.models.UserModel;
import com.semicolon.rests.service.Api;
import com.semicolon.rests.singletone.UserSingleTone;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Reservations extends Fragment {
    private SwipeRefreshLayout sr ;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private ProgressBar progressBar;
    private List<PlacesModel> placesModelList;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private TextView tv_no_reserve;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservations,container,false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        placesModelList = new ArrayList<>();
        progressBar = view.findViewById(R.id.proBar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        tv_no_reserve = view.findViewById(R.id.tv_no_reserve);
        sr = view.findViewById(R.id.sr);
        sr.setColorSchemeResources(R.color.colorPrimary,R.color.redd,R.color.green,R.color.yellow);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        adapter = new ReservationAdapter(getActivity(),placesModelList,this);
        recView.setAdapter(adapter);
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (userModel!=null)
                {
                    getReservation(userModel.getUser_id());

                }else
                    {
                        sr.setRefreshing(false);
                    }
            }
        });


    }

    public static Fragment_Reservations getInstance()
    {
        Fragment_Reservations fragment = new Fragment_Reservations();
        return fragment;
    }


    @Override
    public void onStart() {
        super.onStart();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        getReservation(userModel.getUser_id());
    }

    private void getReservation(String user_id) {

        Api.getServices().getMyReservations(user_id)
                .enqueue(new Callback<List<PlacesModel>>() {
                    @Override
                    public void onResponse(Call<List<PlacesModel>> call, Response<List<PlacesModel>> response) {
                        if (response.isSuccessful())
                        {
                            progressBar.setVisibility(View.GONE);
                            sr.setRefreshing(false);
                            placesModelList.clear();
                            placesModelList.addAll(response.body());
                            adapter.notifyDataSetChanged();
                            if (placesModelList.size()>0)
                            {
                                tv_no_reserve.setVisibility(View.GONE);
                            }else
                                {
                                    tv_no_reserve.setVisibility(View.VISIBLE);

                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PlacesModel>> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progressBar.setVisibility(View.GONE);
                        sr.setRefreshing(false);
                        Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setItem(PlacesModel placesModel)
    {
        Intent intent = new Intent(getActivity(), PlaceProfileActivity.class);
        intent.putExtra("data",placesModel);
        getActivity().startActivity(intent);
    }
}
