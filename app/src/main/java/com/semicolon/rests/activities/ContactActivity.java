package com.semicolon.rests.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.rests.R;
import com.semicolon.rests.common.Common;
import com.semicolon.rests.models.ResponsModel;
import com.semicolon.rests.models.SocialContactModel;
import com.semicolon.rests.service.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactActivity extends AppCompatActivity {
    private ImageView image_back,image_send;
    private EditText edt_name,edt_email,edt_subject,edt_message;
    private LinearLayout ll_in,ll_fb,ll_tw,ll_wa,ll_call,ll_gmail,ll_container;
    private TextView tv_phone;
    private ProgressDialog progressDialog,contact_progressDialog;
    private SocialContactModel socialContactModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        initView();
        getContacts();
    }



    private void initView() {
        progressDialog = Common.getProgress(this,getString(R.string.wait));
        contact_progressDialog = Common.getProgress(this,getString(R.string.sending));

        image_back =findViewById(R.id.image_back);
        image_send =findViewById(R.id.image_send);
        tv_phone =findViewById(R.id.tv_phone);

        edt_name =findViewById(R.id.edt_name);
        edt_email =findViewById(R.id.edt_email);
        edt_subject =findViewById(R.id.edt_subject);
        edt_message =findViewById(R.id.edt_message);
        ll_container =findViewById(R.id.ll_container);
        ll_gmail =findViewById(R.id.ll_gmail);
        ll_in =findViewById(R.id.ll_in);
        ll_tw =findViewById(R.id.ll_tw);
        ll_fb =findViewById(R.id.ll_fb);
        ll_call =findViewById(R.id.ll_call);
        ll_wa =findViewById(R.id.ll_wa);

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        image_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });

        ll_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+socialContactModel.getPhone()));
                startActivity(intent);

            }
        });

        ll_wa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendSMSVia_Whats(socialContactModel.getWhatsapp());

            }
        });

        ll_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMSGVia_Gmail(socialContactModel.getEmail());

            }
        });

        ll_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this,WebActivity.class);
                intent.putExtra("url",socialContactModel.getFacebook());
                startActivity(intent);
            }
        });

        ll_tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this,WebActivity.class);
                intent.putExtra("url",socialContactModel.getTwitter());
                startActivity(intent);
            }
        });

        ll_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this,WebActivity.class);
                intent.putExtra("url",socialContactModel.getInstagram());
                startActivity(intent);
            }
        });


    }

    private void getContacts() {
        progressDialog.show();
        Api.getServices()
                .getContacts()
                .enqueue(new Callback<SocialContactModel>() {
                    @Override
                    public void onResponse(Call<SocialContactModel> call, Response<SocialContactModel> response) {
                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            if (response.body()!=null)
                            {
                                socialContactModel = response.body();
                                UpdateUi(socialContactModel);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SocialContactModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.e("Error",t.getMessage());
                        Toast.makeText(ContactActivity.this, R.string.something_error, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void UpdateUi(SocialContactModel socialContactModel) {
        ll_container.setVisibility(View.VISIBLE);
        image_send.setVisibility(View.VISIBLE);
        tv_phone.setText(socialContactModel.getPhone());
    }
    private void CheckData()
    {
        String m_name = edt_name.getText().toString();
        String m_email= edt_email.getText().toString();
        String m_subject = edt_subject.getText().toString();
        String m_msg = edt_message.getText().toString();

        if (!TextUtils.isEmpty(m_name)&&!TextUtils.isEmpty(m_email)&& Patterns.EMAIL_ADDRESS.matcher(m_email).matches()&&!TextUtils.isEmpty(m_subject)&&!TextUtils.isEmpty(m_msg))
        {
            edt_name.setError(null);
            edt_email.setError(null);
            edt_subject.setError(null);
            edt_message.setError(null);

            SendData(m_name,m_email,m_subject,m_msg);
        }else
            {
                if (TextUtils.isEmpty(m_name)){
                    edt_name.setError(getString(R.string.name_req));
                }else
                    {
                        edt_name.setError(null);

                    }
                if (TextUtils.isEmpty(m_email)){
                    edt_email.setError(getString(R.string.email_req));
                }else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
                {
                    edt_email.setError(getString(R.string.inv_email));
                }
                else
                {
                    edt_email.setError(null);

                }

                if (TextUtils.isEmpty(m_subject)){
                    edt_subject.setError(getString(R.string.subject_req));
                }else
                {
                    edt_subject.setError(null);

                }

                if (TextUtils.isEmpty(m_msg)){
                    edt_message.setError(getString(R.string.msg_req));
                }else
                {
                    edt_message.setError(null);

                }


            }
    }


    private void SendData(String m_name, String m_email, String m_subject, String m_msg) {
        contact_progressDialog.show();
        Api.getServices()
                .sendProblemViaContact(m_name,m_email,m_subject,m_msg)
                .enqueue(new Callback<ResponsModel>() {
                    @Override
                    public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                        if (response.isSuccessful())
                        {
                            contact_progressDialog.dismiss();
                            if (response.body().getSuccess_contact()==1)
                            {
                                finish();
                                Toast.makeText(ContactActivity.this, R.string.send_succ, Toast.LENGTH_SHORT).show();
                            }else if (response.body().getSuccess_contact()==0)
                            {
                                Toast.makeText(ContactActivity.this, R.string.something_error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsModel> call, Throwable t) {
                        contact_progressDialog.dismiss();
                        Toast.makeText(ContactActivity.this, R.string.something_error, Toast.LENGTH_SHORT).show();
                        Log.e("Error",t.getMessage());
                    }
                });
    }

    private void SendMSGVia_Gmail(String email)
    {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{email});
        PackageManager pm =getPackageManager();
        List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
        ResolveInfo best = null;
        for(ResolveInfo info : matches)
        {
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
            {
                best = info;

            }
        }

        if (best != null)
        {
            intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

        }

        startActivity(intent);


    }
    private void SendSMSVia_Whats(String whatsapp_num)
    {
        if (isWhatsApp_installed())
        {
            Uri uri = Uri.parse("smsto:"+whatsapp_num);
            Intent intent =new Intent(Intent.ACTION_SENDTO,uri);
            intent.setPackage("com.whatsapp");
            startActivity(intent.createChooser(intent,"via whatsapp"));
        }else
            {
                Toast.makeText(this, R.string.pls_install_wapp, Toast.LENGTH_SHORT).show();
            }


    }
    private boolean isWhatsApp_installed()
    {

        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo("com.whatsapp",PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

}
