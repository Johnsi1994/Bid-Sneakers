package com.johnson.bid.bidding;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.johnson.bid.R;
import com.johnson.bid.data.Product;
import com.johnson.bid.util.ImageManager;

import java.util.ArrayList;

public class BiddingGalleryAdapter extends RecyclerView.Adapter {

    private BiddingContract.Presenter mPresenter;
    private ArrayList<String> mImages;

    public BiddingGalleryAdapter(BiddingContract.Presenter presenter, ArrayList<String> images
    ) {
        mPresenter = presenter;
        mImages = images;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BiddingGalleryAdapter.PhotoViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_bidding_gallery, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        ImageManager.getInstance().setImageByUrl(((PhotoViewHolder) viewHolder).getPhoto(), mImages.get(i));

    }

    @Override
    public int getItemCount() {
        return mImages.size();
//        return 3;
    }

    private class PhotoViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPhoto;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            mPhoto = itemView.findViewById(R.id.image_bidding_gallery);
        }

        public ImageView getPhoto() {
            return mPhoto;
        }
    }

}
