package com.johnson.bid.bidding;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.johnson.bid.R;
import com.johnson.bid.util.ImageManager;

import java.util.ArrayList;

public class BiddingDetailGalleryAdapter extends RecyclerView.Adapter {

    private ArrayList<String> mImages;

    public BiddingDetailGalleryAdapter(ArrayList<String> images) {
        mImages = images;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BiddingDetailGalleryAdapter.PhotoViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_gallery_pic, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        ImageManager.getInstance().setImageByUrl(((PhotoViewHolder) viewHolder).getPhoto(), mImages.get(i));

    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    private class PhotoViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPhoto;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            mPhoto = itemView.findViewById(R.id.image_gallery_pic);
        }

        public ImageView getPhoto() {
            return mPhoto;
        }
    }

}
