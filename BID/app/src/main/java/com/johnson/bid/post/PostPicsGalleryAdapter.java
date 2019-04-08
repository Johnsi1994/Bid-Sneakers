package com.johnson.bid.post;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.johnson.bid.R;

import java.util.ArrayList;

public class PostPicsGalleryAdapter extends RecyclerView.Adapter {

    private static final int TYPE_PHOTO   = 0x01;
    private static final int TYPE_ADD     = 0x02;

    private ArrayList<Bitmap> mBitmaps;

    public PostPicsGalleryAdapter(ArrayList<Bitmap> bitmaps) {
        mBitmaps = bitmaps;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_PHOTO) {

            return new PhotoViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_post_pic, viewGroup, false));
        }  else {

            return new AddViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_add, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof PhotoViewHolder) {
            bindPhotoViewHolder((PhotoViewHolder) viewHolder, mBitmaps.get(position));
        }
        
    }

    @Override
    public int getItemCount() {
        return (mBitmaps.size() + 1);
    }

    @Override
    public int getItemViewType(int position) {

        if (position == (mBitmaps.size())) {
            return TYPE_ADD;
        } else {
            return TYPE_PHOTO;
        }
    }

    private void bindPhotoViewHolder(PhotoViewHolder holder, Bitmap bitmap) {
        holder.getPhoto().setImageBitmap(bitmap);
    }

    private class PhotoViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPhoto;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            mPhoto = itemView.findViewById(R.id.image_post_pic);
        }

        public ImageView getPhoto() {
            return mPhoto;
        }
    }

    private class AddViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout mAddLayout;

        public AddViewHolder(View itemView) {
            super(itemView);

            mAddLayout = itemView.findViewById(R.id.layout_add_pic);

            mAddLayout.setOnClickListener(v -> {

            });

        }
    }
}
