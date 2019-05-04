package com.johnson.bid.post;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.johnson.bid.Bid;
import com.johnson.bid.R;
import com.johnson.bid.util.RotatePic;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.johnson.bid.MainMvpController.POST;

public class PostPicsGalleryAdapter extends RecyclerView.Adapter {

    private static final int TYPE_PHOTO   = 0x01;
    private static final int TYPE_ADD     = 0x02;

    private PostContract.Presenter mPresenter;
    private ArrayList<Bitmap> mImageBitmap;

    public PostPicsGalleryAdapter(ArrayList<Bitmap> imageBitmap, PostContract.Presenter presenter) {
        mImageBitmap = imageBitmap;
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_PHOTO) {

            return new PhotoViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_post_added_pic, viewGroup, false));
        }  else {

            return new AddViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_post_add_pics, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof PhotoViewHolder) {

//                Log.d("imagetest", "onBindViewHolder path : " + mImagePath.get(position));
//
//                InputStream is = new FileInputStream(mImagePath.get(position));
//                BitmapFactory.Options opts = new BitmapFactory.Options();
//                opts.inTempStorage = new byte[100 * 100];
//                opts.inPreferredConfig = Bitmap.Config.RGB_565;
//                opts.inSampleSize = 4;
//                Bitmap btp = BitmapFactory.decodeStream(is, null, opts);

            bindPhotoViewHolder((PhotoViewHolder) viewHolder, mImageBitmap.get(position));

        }

    }

    @Override
    public int getItemCount() {
        return ( (mImageBitmap.size() == 4) ? mImageBitmap.size() : mImageBitmap.size() + 1);
    }

    @Override
    public int getItemViewType(int position) {

        if (mImageBitmap.size() < 4) {
            if (position == (mImageBitmap.size())) {
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
                mPresenter.openGalleryDialog(POST)
            );

        }
    }
}
