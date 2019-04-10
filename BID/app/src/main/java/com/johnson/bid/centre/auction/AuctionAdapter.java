package com.johnson.bid.centre.auction;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnson.bid.MainMvpController;
import com.johnson.bid.R;

import static com.johnson.bid.MainMvpController.ENGLISH;

public class AuctionAdapter extends RecyclerView.Adapter {

    private static final int TYPE_LOADING = 0;
    private static final int TYPE_PRODUCT = 0x01;

    private AuctionContract.Presenter mPresenter;
    private String mAuctionType;

    public AuctionAdapter(AuctionContract.Presenter presenter, @MainMvpController.AuctionType String auctionType) {
        mPresenter = presenter;
        mAuctionType = auctionType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_PRODUCT) {
            if (mAuctionType.equals(ENGLISH)) {
                return new EnglishAuctionViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_english_auction, viewGroup, false));
            } else {
                return new SealedAuctionViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_sealed_auction, viewGroup, false));
            }
        } else {
            return new LoadingViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_all_loading, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return (mAuctionType.equals(ENGLISH)) ? 10: 5;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_PRODUCT;
    }

    private class EnglishAuctionViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        private TextView mTextPrice;
        private TextView mTextPeople;

        public EnglishAuctionViewHolder(View itemView) {
            super(itemView);

            mImageMain = itemView.findViewById(R.id.image_product_e_auction);
            mTextTitle = itemView.findViewById(R.id.text_product_title_e_auction);
            mTextTime = itemView.findViewById(R.id.text_last_time_e_auction);
            mTextPrice = itemView.findViewById(R.id.text_price_e_auction);
            mTextPeople = itemView.findViewById(R.id.text_participant_num_e_auction);
        }

        public ImageView getImageMain() {
            return mImageMain;
        }

        public TextView getTextTitle() {
            return mTextTitle;
        }

        public TextView getTextTime() {
            return mTextTime;
        }

        public TextView getTextPrice() {
            return mTextPrice;
        }

        public TextView getTextPeople() {
            return mTextPeople;
        }
    }

    private class SealedAuctionViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;

        public SealedAuctionViewHolder(View itemView) {
            super(itemView);

            mImageMain = itemView.findViewById(R.id.image_selling_s);
            mTextTitle = itemView.findViewById(R.id.text_title_selling_s);
            mTextTime = itemView.findViewById(R.id.text_last_time_s_auction);
        }

        public ImageView getImageMain() {
            return mImageMain;
        }

        public TextView getTextTitle() {
            return mTextTitle;
        }

        public TextView getTextTime() {
            return mTextTime;
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

}
