package com.semicolon.rests.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.semicolon.rests.R;
import com.semicolon.rests.fragments.Fragment_Estrahat;
import com.semicolon.rests.fragments.Fragment_Kosor;
import com.semicolon.rests.fragments.Fragment_Shalehat;
import com.semicolon.rests.models.PlacesModel;
import com.semicolon.rests.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;


public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.Holder> {
    private Context context;
    private List<PlacesModel> placesModelList;
    private Fragment fragment;
    public PlacesAdapter(Context context, List<PlacesModel>placesModelList, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        this.placesModelList = placesModelList;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.place_row, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        PlacesModel placesModel = placesModelList.get(position);
        holder.BindData(placesModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacesModel placesModel = placesModelList.get(holder.getAdapterPosition());

                if (fragment instanceof Fragment_Estrahat)
                {

                    Fragment_Estrahat fragment_estrahat = (Fragment_Estrahat) fragment;
                    fragment_estrahat.setItem(placesModel,v);

                }else if (fragment instanceof Fragment_Shalehat)
                {
                    Fragment_Shalehat fragment_shalehat = (Fragment_Shalehat) fragment;
                    fragment_shalehat.setItem(placesModel,v);

                }else if (fragment instanceof Fragment_Kosor)
                {
                    Fragment_Kosor fragment_kosor = (Fragment_Kosor) fragment;
                    fragment_kosor.setItem(placesModel,v);

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return placesModelList.size();
    }

    class Holder extends RecyclerView.ViewHolder  {

        private RoundedImageView image;
        private TextView tv_name,tv_address;
        public Holder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_address = itemView.findViewById(R.id.tv_address);





        }

        public void BindData(PlacesModel placesModel)
        {
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_PATH+placesModel.getPlace_main_photo())).into(image);
            tv_name.setText(placesModel.getPlace_name());
            tv_address.setText(placesModel.getPlace_address());
        }



}

}
