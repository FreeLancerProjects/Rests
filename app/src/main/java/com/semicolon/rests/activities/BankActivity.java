package com.semicolon.rests.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.semicolon.rests.R;
import com.semicolon.rests.adapters.BanksAdapter;
import com.semicolon.rests.models.BanksModel;
import com.semicolon.rests.service.Api;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankActivity extends AppCompatActivity {
    private ImageView image_back;
    private ArrayList<BanksModel> banksModelArrayList;
    private BanksAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private SmoothProgressBar smoothProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        initView();
    }

    private void initView() {
        image_back = findViewById(R.id.image_back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        smoothProgress = findViewById(R.id.smoothProgress);
        recyclerView = findViewById(R.id.recyc_banks);
        banksModelArrayList = new ArrayList<>();
        manager =new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        adapter = new BanksAdapter(this, banksModelArrayList);
        recyclerView.setAdapter(adapter);
        
        getData();
    }

    private void getData() {
        Api.getServices()
                .getBanks().enqueue(new Callback<List<BanksModel>>() {
            @Override
            public void onResponse(Call<List<BanksModel>> call, Response<List<BanksModel>> response) {
                if (response.isSuccessful())
                {
                    smoothProgress.setVisibility(View.GONE);
                    if (response.body().size()>0) {

                        banksModelArrayList.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BanksModel>> call, Throwable t) {
                smoothProgress.setVisibility(View.GONE);
                Log.e("Error",t.getMessage());
                Toast.makeText(BankActivity.this, R.string.something_error, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
