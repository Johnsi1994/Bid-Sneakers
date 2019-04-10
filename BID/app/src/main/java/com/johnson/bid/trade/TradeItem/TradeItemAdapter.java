package com.johnson.bid.trade.TradeItem;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnson.bid.MainMvpController;
import com.johnson.bid.R;

import static com.johnson.bid.MainMvpController.BIDDING;
import static com.johnson.bid.MainMvpController.BOUGHT;
import static com.johnson.bid.MainMvpController.SELLING;
import static com.johnson.bid.MainMvpController.SOLD;

public class TradeItemAdapter extends RecyclerView.Adapter {

    private static final int TYPE_LOADING = 0;
    private static final int TYPE_PRODUCT = 0x01;

    private TradeItemContract.Presenter mPresenter;
    private String mTradeType;

    public TradeItemAdapter(TradeItemContract.Presenter presenter, @MainMvpController.TradeType String tradeType) {
        mPresenter = presenter;
        mTradeType = tradeType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_PRODUCT) {
            switch (mTradeType) {
                case BIDDING:
                    return new BiddingEnglishViewHolder(LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.item_english_auction, viewGroup, false));

//                return new BiddingSealedViewHolder(LayoutInflater.from(viewGroup.getContext())
//                        .inflate(R.layout.item_sealed_auction, viewGroup, false));
                case SELLING:
                    return new SellingEnglishViewHolder(LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.item_selling_english, viewGroup, false));

//                return new SellingSealedViewHolder(LayoutInflater.from(viewGroup.getContext())
//                        .inflate(R.layout.item_selling_sealed, viewGroup, false));
                case BOUGHT:
                    return new BoughtViewHolder(LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.item_bought, viewGroup, false));
                case SOLD:
                default:
                    return new SoldViewHolder(LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.item_sold, viewGroup, false));
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
        return 6;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_PRODUCT;
    }

    private class BiddingEnglishViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        private TextView mTextPrice;
        private TextView mTextPeople;

        public BiddingEnglishViewHolder(View itemView) {
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

    private class BiddingSealedViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;

        public BiddingSealedViewHolder(View itemView) {
            super(itemView);

            mImageMain = itemView.findViewById(R.id.image_product_s_auction);
            mTextTitle = itemView.findViewById(R.id.text_product_title_s_auction);
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

    private class SellingEnglishViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        private TextView mTextPrice;
        private TextView mTextBuyer;
        private TextView mTextPeople;

        public SellingEnglishViewHolder(View itemView) {
            super(itemView);

            mImageMain = itemView.findViewById(R.id.image_selling_e);
            mTextTitle = itemView.findViewById(R.id.text_title_selling_e);
            mTextTime = itemView.findViewById(R.id.text_last_time_selling_e);
            mTextPrice = itemView.findViewById(R.id.text_price_selling_e);
            mTextBuyer = itemView.findViewById(R.id.text_buyer_selling_e);
            mTextPeople = itemView.findViewById(R.id.text_people_selling_e);
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

        public TextView getTextBuyer() {
            return mTextBuyer;
        }

        public TextView getTextPeople() {
            return mTextPeople;
        }
    }

    private class SellingSealedViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;

        public SellingSealedViewHolder(View itemView) {
            super(itemView);

            mImageMain = itemView.findViewById(R.id.image_selling_s);
            mTextTitle = itemView.findViewById(R.id.text_title_selling_s);
            mTextTime = itemView.findViewById(R.id.text_last_time_selling_s);
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

    private class BoughtViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        private TextView mTextPrice;
        private TextView mTextSeller;

        public BoughtViewHolder(View itemView) {
            super(itemView);

            mImageMain = itemView.findViewById(R.id.image_bought);
            mTextTitle = itemView.findViewById(R.id.text_title_bought);
            mTextTime = itemView.findViewById(R.id.text_time_bought);
            mTextPrice = itemView.findViewById(R.id.text_price_bought);
            mTextSeller = itemView.findViewById(R.id.text_seller_bought);
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
            return mTextSeller;
        }
    }

    private class SoldViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        private TextView mTextPrice;
        private TextView mTextBuyer;

        public SoldViewHolder(View itemView) {
            super(itemView);

            mImageMain = itemView.findViewById(R.id.image_sold);
            mTextTitle = itemView.findViewById(R.id.text_title_sold);
            mTextTime = itemView.findViewById(R.id.text_time_sold);
            mTextPrice = itemView.findViewById(R.id.text_price_sold);
            mTextBuyer = itemView.findViewById(R.id.text_buyer_sold);
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

        public TextView getTextBuyer() {
            return mTextBuyer;
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
