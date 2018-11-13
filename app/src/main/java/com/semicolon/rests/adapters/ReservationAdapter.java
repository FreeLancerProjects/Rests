package com.semicolon.rests.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.semicolon.rests.R;
import com.semicolon.rests.fragments.Fragment_Reservations;
import com.semicolon.rests.models.PlacesModel;
import com.semicolon.rests.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.Holder> {
    private Context context;
    private List<PlacesModel> placesModelList;
    private Fragment_Reservations fragment_reservations;
    public ReservationAdapter(Context context, List<PlacesModel> placesModelList, Fragment fragment) {
        this.fragment_reservations = (Fragment_Reservations) fragment;
        this.context = context;
        this.placesModelList = placesModelList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reserve_row, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        PlacesModel placesModel = placesModelList.get(position);
        holder.BindData(placesModel);
        holder.btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacesModel placesModel = placesModelList.get(holder.getAdapterPosition());
                fragment_reservations.setItem(placesModel);

            }
        });




    }

    @Override
    public int getItemCount() {
        return placesModelList.size();
    }

    class Holder extends RecyclerView.ViewHolder  {
        private TextView tv_name, tv_date, tv_state;
        private Button btn_update;
        private ImageView image;

        public Holder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_state = itemView.findViewById(R.id.tv_state);
            btn_update = itemView.findViewById(R.id.btn_update);
            image = itemView.findViewById(R.id.image);



        }

        public void BindData(PlacesModel placesModel)
        {
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_PATH+placesModel.getPlace_main_photo())).into(image);
            tv_name.setText(placesModel.getPlace_name());
            tv_state.setText(R.string.booked);
            tv_date.setText(placesModel.getReservation_date());
        }



}

}
