package com.johnson.bid.post;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.johnson.bid.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class PostPicsGalleryAdapter extends RecyclerView.Adapter {

    private static final int TYPE_PHOTO   = 0x01;
    private static final int TYPE_ADD     = 0x02;

    private PostContract.Presenter mPresenter;
    private ArrayList<String> mImagePath;
    private Bitmap bitImage;

    public PostPicsGalleryAdapter(ArrayList<String> imagePath, PostContract.Presenter presenter) {
        mImagePath = imagePath;
        mPresenter = presenter;
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
            try {

                InputStream is = new FileInputStream(mImagePath.get(position));
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inTempStorage = new byte[100 * 1024];
                opts.inPreferredConfig = Bitmap.Config.RGB_565;
                opts.inSampleSize = 4;
                Bitmap btp = BitmapFactory.decodeStream(is,null, opts);
                bindPhotoViewHolder((PhotoViewHolder) viewHolder, btp);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getItemCount() {
        return ( (mImagePath.size() == 4) ? mImagePath.size() : mImagePath.size() + 1);
    }

    @Override
    public int getItemViewType(int position) {

        if (mImagePath.size() < 4) {
            if (position == (mImagePath.size())) {
                return TYPE_ADD;
            } else {
                return TYPE_PHOTO;
            }
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

            mAddLayout.setOnClickListener(v ->
                mPresenter.openGalleryDialog("POSTGALLERY")
            );

        }
    }
}
