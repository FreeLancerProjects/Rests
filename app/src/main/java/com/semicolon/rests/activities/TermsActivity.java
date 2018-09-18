package com.semicolon.rests.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.rests.R;
import com.semicolon.rests.models.TermsModel;
import com.semicolon.rests.service.Api;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsActivity extends AppCompatActivity {
    private ImageView image_back;
    private TextView content;
    private SmoothProgressBar smoothProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        initView();
    }

    private void initView() {
        smoothProgress = findViewById(R.id.smoothProgress);
        image_back = findViewById(R.id.image_back);
        content=findViewById(R.id.txt_content);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData();

    }
    private void getData() {
        Api.getServices()
                .getTermsAndConditions().enqueue(new Callback<TermsModel>() {
            @Override
            public void onResponse(Call<TermsModel> call, Response<TermsModel> response) {
                if (response.isSuccessful())
                {
                    smoothProgress.setVisibility(View.GONE);
                    content.setText(response.body().getContent());
                }
            }

            @Override
            public void onFailure(Call<TermsModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
                smoothProgress.setVisibility(View.GONE);
                Toast.makeText(TermsActivity.this, R.string.something_error, Toast.LENGTH_SHORT).show();
            }
        });
    }



}
