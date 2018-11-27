package com.semicolon.rests.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lamudi.phonefield.PhoneInputLayout;
import com.semicolon.rests.R;
import com.semicolon.rests.adapters.PlaceImageAdapter;
import com.semicolon.rests.common.Common;
import com.semicolon.rests.models.PlacesModel;
import com.semicolon.rests.models.ResponsModel;
import com.semicolon.rests.models.UserModel;
import com.semicolon.rests.service.Api;
import com.semicolon.rests.singletone.UserSingleTone;
import com.semicolon.rests.tags.Tags;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    private PlacesModel placesModel;
    private ImageView image_back,image;
    private ImageView image_call;
    private TextView tv_phone,tv_address,tv_size,tv_name,tv_date,tv_price,tv_update_add;
    private LinearLayout ll_gallery;
    private CardView card_map;
    private Button btn_reserve;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private List<PlacesModel.Gallery> images;
    private NestedScrollView nsv;
    private DatePickerDialog datePickerDialog;
    private String m_date="";
    private UserSingleTone userSingleTone ;
    private UserModel userModel;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;
    private ImageView bottom_sheet_image_back,uploaded_image;
    private LinearLayout ll_upload_image;
    private PhoneInputLayout edt_check_phone;
    private EditText edt_name,edt_phone,edt_price;
    private Button btn_trans;
    private View root;
    private BottomSheetBehavior behavior;
    private Bitmap bitmap=null;
    private final int img_req =102,read_per=455;
    private Uri uri =null;
    private String read_permission = Manifest.permission.READ_EXTERNAL_STORAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_profile);
        initView();
        getDataFromIntent();
        CreateDateDialog();
        CheckReadPremission();

    }

    private void CheckReadPremission() {
        if (ContextCompat.checkSelfPermission(this,read_permission)!= PackageManager.PERMISSION_GRANTED)
        {
            String [] per = {read_permission};
            ActivityCompat.requestPermissions(this,per,read_per);
        }
    }

    private void CreateDateDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        datePickerDialog = DatePickerDialog.newInstance
                (this,
                        calendar.get(Calendar.YEAR)
                        ,calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setMinDate(calendar);

        datePickerDialog.setAccentColor(ContextCompat.getColor(this,R.color.colorPrimary));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
    }


    private void initView() {
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        images = new ArrayList<>();
        root = findViewById(R.id.root);
        behavior = BottomSheetBehavior.from(root);
        bottom_sheet_image_back = findViewById(R.id.bottom_sheet_image_back);
        uploaded_image = findViewById(R.id.uploaded_image);
        ll_upload_image = findViewById(R.id.ll_upload_image);
        edt_name = findViewById(R.id.edt_name);
        edt_phone = findViewById(R.id.edt_phone);
        edt_price = findViewById(R.id.edt_price);
        edt_check_phone = findViewById(R.id.edt_check_phone);

        ll_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        btn_trans = findViewById(R.id.btn_trans);

        btn_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });

        bottom_sheet_image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        if (userModel!=null)
        {
            edt_name.setText(userModel.getUser_full_name());
            edt_phone.setText(userModel.getUser_phone());
        }
        ///////////////////////////////////////////////
        image_back  = findViewById(R.id.image_back);
        image       = findViewById(R.id.image);
        image_call  = findViewById(R.id.image_call);
        card_map    = findViewById(R.id.card_map);
        tv_price    = findViewById(R.id.tv_price);
        tv_date    = findViewById(R.id.tv_date);
        tv_update_add    = findViewById(R.id.tv_update_add);
        tv_phone    = findViewById(R.id.tv_phone);
        tv_address  = findViewById(R.id.tv_address);
        tv_name  = findViewById(R.id.tv_name);
        tv_size     = findViewById(R.id.tv_size);
        ll_gallery  = findViewById(R.id.ll_gallery);
        btn_reserve = findViewById(R.id.btn_reserve);
        nsv = findViewById(R.id.nsv);

        recView     = findViewById(R.id.recView);
        manager     = new LinearLayoutManager(this);
        recView.setLayoutManager(manager);
        adapter     = new PlaceImageAdapter(this,images);
        recView.setAdapter(adapter);
        recView.setNestedScrollingEnabled(false);
        recView.setFocusable(false);
        nsv.requestLayout();


        tv_update_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(getFragmentManager(),"Date");
            }
        });
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
            }
        });
        image_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+placesModel.getPlace_phone()));
                startActivity(intent);
            }
        });
        card_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlaceProfileActivity.this,MapActivity.class);
                intent.putExtra("lat",Double.parseDouble(placesModel.getPlace_google_lat()));
                intent.putExtra("lng",Double.parseDouble(placesModel.getPlace_google_long()));

                Log.e("lat",placesModel.getPlace_google_lat()+"_");
                Log.e("lng",placesModel.getPlace_google_long()+"_");

                startActivity(intent);

            }
        });

        btn_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userModel==null)
                {
                    CreateAlertDialog(getString(R.string.si_su));
                }else
                    {
                        if (placesModel.getCan_cancel().equals(Tags.can_cancel))
                        {
                            CancelReserve();
                        }else
                            {
                                if (TextUtils.isEmpty(m_date))
                                {
                                    CreateAlertDialog(getString(R.string.des_date));

                                }else
                                {
                                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                    //Reserve();
                                }
                            }


                    }
            }
        });
    }

    private void SelectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"Select Image"),img_req);

    }

    private void CheckData() {

        String m_name = edt_name.getText().toString();
        String m_phone = edt_phone.getText().toString();
        if (!m_phone.startsWith("+"))
        {
            m_phone= "+"+m_phone;
        }
        edt_check_phone.setPhoneNumber(m_phone);
        String m_price = edt_price.getText().toString();

        if (!TextUtils.isEmpty(m_name)&&!TextUtils.isEmpty(m_phone)&& edt_check_phone.isValid()&& !TextUtils.isEmpty(m_price)&& bitmap!=null)
        {
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_price.setError(null);

            Reserve(m_name,m_phone,m_price);
        }else
            {
                if (TextUtils.isEmpty(m_name))
                {
                    edt_name.setError(getString(R.string.name_req));
                }else
                    {
                        edt_name.setError(null);

                    }

                if (TextUtils.isEmpty(m_phone))
                {
                    edt_phone.setError(getString(R.string.phone_req));
                }else if (!edt_check_phone.isValid())
                {
                    edt_name.setError(getString(R.string.inv_phone));

                }else
                    {
                        edt_phone.setError(null);

                    }

                if (TextUtils.isEmpty(m_price))
                {
                    edt_price.setError(getString(R.string.tran_mon_req));
                }else
                {
                    edt_price.setError(null);

                }


            }



    }

    private void CancelReserve() {
        progressDialog = Common.getProgress(this,getString(R.string.canc_booking));
        progressDialog.show();




        Api.getServices()
                .CancelReservation(placesModel.getId_reservation(),userModel.getUser_id())
                .enqueue(new Callback<ResponsModel>() {
                    @Override
                    public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            if (response.body().getSuccess_delete_reservation()==1)
                            {
                                finish();
                                Toast.makeText(PlaceProfileActivity.this, R.string.cancel_succ, Toast.LENGTH_SHORT).show();
                            }else
                                {
                                    Toast.makeText(PlaceProfileActivity.this,R.string.something_error, Toast.LENGTH_SHORT).show();
                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(PlaceProfileActivity.this,R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void Reserve(String name ,String phone , String price) {

        progressDialog = Common.getProgress(this,getString(R.string.booking));
        progressDialog.show();

        Log.e("mdate",m_date);
        RequestBody place_id_part = Common.getRequestBodyFromData(placesModel.getId_place(),"text/plain");
        RequestBody user_id_part = Common.getRequestBodyFromData(userModel.getUser_id(),"text/plain");
        RequestBody reservation_cost_part = Common.getRequestBodyFromData(placesModel.getPlace_cost(),"text/plain");
        RequestBody date_part = Common.getRequestBodyFromData(m_date,"text/plain");

        RequestBody name_part = Common.getRequestBodyFromData(name,"text/plain");
        RequestBody phone_part = Common.getRequestBodyFromData(phone,"text/plain");
        RequestBody price_part = Common.getRequestBodyFromData(price,"text/plain");

        MultipartBody.Part image_part = Common.getMultipart(this,uri);




        Api.getServices()
                .reserve(place_id_part,user_id_part,reservation_cost_part,date_part,name_part,phone_part,price_part,image_part)
                .enqueue(new Callback<ResponsModel>() {
                    @Override
                    public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();

                            if (response.body().getSuccess_reservation()==1)
                            {

                                CreateAlertDialog2(getString(R.string.book_succ));

                            }else
                                {
                                    CreateAlertDialog(getString(R.string.date_reserve));
                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(PlaceProfileActivity.this,R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void CreateAlertDialog(String msg)
    {
        alertDialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog,null);
        TextView tv_msg = view.findViewById(R.id.tv_msg);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        tv_msg.setText(msg);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        alertDialog.setView(view);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void CreateAlertDialog2(String msg)
    {
        alertDialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog,null);
        TextView tv_msg = view.findViewById(R.id.tv_msg);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        tv_msg.setText(msg);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialog.dismiss();
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                finish();
            }
        });

        alertDialog.setView(view);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            placesModel = (PlacesModel) intent.getSerializableExtra("data");
            UpdateUi(placesModel);
        }
    }

    private void UpdateUi(PlacesModel placesModel) {
        Picasso.with(this).load(Uri.parse(Tags.IMAGE_PATH+placesModel.getPlace_main_photo())).into(image);
        tv_phone.setText(placesModel.getPlace_phone());
        tv_address.setText(placesModel.getPlace_address());
        tv_size.setText(placesModel.getPlace_size());
        tv_name.setText(placesModel.getPlace_name());
        tv_price.setText(placesModel.getPlace_cost()+" "+getString(R.string.sar));
        images.addAll(placesModel.getGalleryList());
        adapter.notifyDataSetChanged();

        Log.e("can ed",placesModel.getCan_edit());
        Log.e("can can",placesModel.getCan_cancel());
        Log.e("can tr",placesModel.getCan_transformat());

        if (placesModel.getCan_cancel().equals(Tags.can_cancel))
        {
            btn_reserve.setText(R.string.cancel_reserve);
        }else if (placesModel.getCan_cancel().equals(Tags.cannot_cancel))
        {
            btn_reserve.setVisibility(View.INVISIBLE);
        }else
            {
                btn_reserve.setText(getString(R.string.reserve));
                btn_reserve.setVisibility(View.VISIBLE);



            }

            if (placesModel.getCan_edit().equals(Tags.can_edit))
            {
                tv_update_add.setText(R.string.upd);
                tv_update_add.setVisibility(View.VISIBLE);
                tv_date.setText(placesModel.getReservation_date());
            }else if (placesModel.getCan_cancel().equals(Tags.cannot_edit))
            {
                tv_update_add.setVisibility(View.INVISIBLE);
                tv_date.setText(placesModel.getReservation_date());

            }else
            {
                tv_update_add.setText(R.string.add_date);
                tv_update_add.setVisibility(View.VISIBLE);



            }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        calendar.set(Calendar.MONTH,monthOfYear);
        calendar.set(Calendar.YEAR,year);

        Date date = new Date(calendar.getTimeInMillis());
        Date date_now = new Date(now.getTimeInMillis());

        if (date.before(date_now))
        {
            Toast.makeText(this, R.string.date_old, Toast.LENGTH_LONG).show();
        }else
            {
                m_date = dayOfMonth+"-"+(monthOfYear+1)+"-"+year;

                if (placesModel.getCan_edit().equals(Tags.can_edit))
                {
                    if (userModel!=null)
                    {
                        UpdateDate(m_date);

                    }
                }else
                    {
                        tv_date.setText(m_date);

                    }


            }


    }



    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

    }
    private void UpdateDate(final String m_date) {

        progressDialog = Common.getProgress(this,getString(R.string.updating_booking_date));
        progressDialog.show();
        Api.getServices()
                .UpdateReservation(placesModel.getId_reservation(),m_date,placesModel.getId_place())
                .enqueue(new Callback<ResponsModel>() {
                    @Override
                    public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            if (response.body().getSuccess_update_reservation()==1)
                            {
                                tv_date.setText(m_date);
                                Toast.makeText(PlaceProfileActivity.this, R.string.book_upd_succ, Toast.LENGTH_SHORT).show();
                            }else
                                {
                                    CreateAlertDialog(getString(R.string.date_reserve));

                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(PlaceProfileActivity.this, R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == img_req && resultCode==RESULT_OK && data!=null)
        {
            uri = data.getData();
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                uploaded_image.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==read_per)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {

                }else
                    {
                        String [] per = {read_permission};
                        ActivityCompat.requestPermissions(this,per,read_per);
                    }
            }else
                {
                    String [] per = {read_permission};
                    ActivityCompat.requestPermissions(this,per,read_per);
                }
        }
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }


}
