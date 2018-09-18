package com.semicolon.rests.activities;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.rests.R;
import com.semicolon.rests.models.TermsModel;
import com.semicolon.rests.service.Api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsActivity extends AppCompatActivity {
    private ImageView image_back;
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        initView();
    }

    private void initView() {

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
                    if (response.body()!=null) {
                        content.setText(response.body().getContent());
                    }
                }
            }

            @Override
            public void onFailure(Call<TermsModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
                Toast.makeText(TermsActivity.this, R.string.something_error, Toast.LENGTH_SHORT).show();
            }
        });
    }



}
