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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankActivity extends AppCompatActivity {
    private ImageView image_back;

    ArrayList<BanksModel> model;
    BanksAdapter adapter;
    RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;

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

        recyclerView = findViewById(R.id.recyc_banks);
        model = new ArrayList<>();
        mLayoutManager=new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new BanksAdapter(this, model);
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
                    if (response.body()!=null) {
                        model.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BanksModel>> call, Throwable t) {
                Log.e("Error",t.getMessage());
                Toast.makeText(BankActivity.this, R.string.something_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
