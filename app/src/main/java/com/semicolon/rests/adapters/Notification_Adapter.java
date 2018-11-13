package com.semicolon.rests.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.semicolon.rests.R;
import com.semicolon.rests.models.Notification_Model;
import com.semicolon.rests.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.MyHolder>{
    private Context context;
    private List<Notification_Model> notificationList;
    public Notification_Adapter(Context context, List<Notification_Model> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Notification_Model notificationModel = notificationList.get(position);
        holder.BindData(notificationModel);

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView tv_name,tv_not_txt;
        public MyHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_not_txt = itemView.findViewById(R.id.tv_not_txt);


        }

        public void BindData(Notification_Model notification_model)
        {

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_PATH+notification_model.getPlace_main_photo())).into(image);
            tv_name.setText(notification_model.getPlace_name());
            if (notification_model.getApproved().equals(Tags.admin_accept))
            {
                tv_not_txt.setText(R.string.res_accept);
            }else if (notification_model.getApproved().equals(Tags.admin_refuse))
            {
                tv_not_txt.setText(R.string.resv_cancel);

            }
        }
    }
}
