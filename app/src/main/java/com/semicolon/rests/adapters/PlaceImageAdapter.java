package com.semicolon.rests.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.semicolon.rests.R;
import com.semicolon.rests.models.PlacesModel;
import com.semicolon.rests.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaceImageAdapter extends RecyclerView.Adapter <PlaceImageAdapter.MyHolder>{

    private Context context;
    private List<PlacesModel.Gallery> images;

    public PlaceImageAdapter(Context context, List<PlacesModel.Gallery> images) {
        this.context = context;
        this.images= images;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.place_image_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
       PlacesModel.Gallery gallery =images.get(position);
       holder.BindData(gallery);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private RoundedImageView image;
        public MyHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);

        }

        public void BindData(PlacesModel.Gallery gallery)

        {
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_PATH+gallery.getPhoto_name())).into(image);
        }
    }
}
