package com.johnson.bid.selling;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.johnson.bid.R;
import com.johnson.bid.util.ImageManager;

import java.util.ArrayList;

public class SellingGalleryAdapter extends RecyclerView.Adapter {

    private SellingContract.Presenter mPresenter;
    private ArrayList<String> mImages;

    public SellingGalleryAdapter(SellingContract.Presenter presenter, ArrayList<String> images) {
        mPresenter = presenter;
        mImages = images;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SellingGalleryAdapter.PhotoViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_gallery_pic, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        ImageManager.getInstance().setImageByUrl(((PhotoViewHolder) holder).getPhoto(), mImages.get(i));

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
