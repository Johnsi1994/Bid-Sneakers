package com.johnson.bid.nobodybit;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.johnson.bid.Bid;
import com.johnson.bid.MainActivity;
import com.johnson.bid.R;
import com.johnson.bid.data.Product;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NobodyBidDetailAdapter extends RecyclerView.Adapter {

    private NobodyBidDetailContract.Presenter mPresenter;
    private LinearSnapHelper mLinearSnapHelper;
    private MainActivity mMainActivity;
    private Product mProduct;

    public NobodyBidDetailAdapter(NobodyBidDetailContract.Presenter presenter, MainActivity mainActivity) {
        mPresenter = presenter;
        mMainActivity = mainActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NobodyBitDetailViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_nobody_bid_detail, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                Bid.getAppContext(), LinearLayoutManager.HORIZONTAL, false);
        NobodyBidDetailGalleryAdapter nobodyBidDetailGalleryAdapter = new NobodyBidDetailGalleryAdapter(mPresenter, mProduct.getImages());

        if (mLinearSnapHelper == null) {
            mLinearSnapHelper = new LinearSnapHelper();
            mLinearSnapHelper.attachToRecyclerView(((NobodyBitDetailViewHolder) holder).getGalleryRecycler());
        }
        ((NobodyBitDetailViewHolder) holder).getGalleryRecycler().setAdapter(nobodyBidDetailGalleryAdapter);
        ((NobodyBitDetailViewHolder) holder).getGalleryRecycler().setLayoutManager(layoutManager);

        bindNobodyBitDetailViewHolder((NobodyBitDetailViewHolder) holder, mProduct);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private class NobodyBitDetailViewHolder extends RecyclerView.ViewHolder {

        private Button mBackBtn;
        private RecyclerView mGalleryRecycler;
        private TextView mTitleText;
        private TextView mIntroText;
        private TextView mConditionText;
        private TextView mAuctionTypeText;
        private TextView mExpiredText;
        private TextView mStartingPriceText;
        private TextView mIncreaseText;
        private TextView mReservePriceText;
        private Button mRepostBtn;

        public NobodyBitDetailViewHolder(@NonNull View itemView) {
            super(itemView);

            mBackBtn = itemView.findViewById(R.id.button_nobody_bit_back);
            mGalleryRecycler = itemView.findViewById(R.id.recycler_nobody_bit_gallery);
            mTitleText = itemView.findViewById(R.id.text_nobody_bit_title);
            mIntroText = itemView.findViewById(R.id.text_nobody_bit_intro);
            mConditionText = itemView.findViewById(R.id.text_nobody_bit_condition);
            mAuctionTypeText = itemView.findViewById(R.id.text_nobody_bit_auction_type);
            mExpiredText = itemView.findViewById(R.id.text_nobody_bit_expired);
            mStartingPriceText = itemView.findViewById(R.id.text_nobody_bit_starting_time);
            mIncreaseText = itemView.findViewById(R.id.text_nobody_bit_increase);
            mReservePriceText = itemView.findViewById(R.id.text_nobody_bit_reserve_price);
            mRepostBtn = itemView.findViewById(R.id.button_nobody_bit_repost);

            mBackBtn.setOnClickListener(v ->
                    mMainActivity.onBackPressed()
            );

            mRepostBtn.setOnClickListener(v ->
                    Toast.makeText(mMainActivity, "Repost is Coming Soon", Toast.LENGTH_SHORT).show()
            );

        }

        private RecyclerView getGalleryRecycler() {
            return mGalleryRecycler;
        }

        private TextView getTitleText() {
            return mTitleText;
        }

        private TextView getIntroText() {
            return mIntroText;
        }

        private TextView getConditionText() {
            return mConditionText;
        }

        private TextView getAuctionTypeText() {
            return mAuctionTypeText;
        }

        private TextView getExpiredText() {
            return mExpiredText;
        }

        private TextView getStartingPriceText() {
            return mStartingPriceText;
        }

        private TextView getIncreaseText() {
            return mIncreaseText;
        }

        private TextView getReservePriceText() {
            return mReservePriceText;
        }

    }

    private void bindNobodyBitDetailViewHolder(NobodyBitDetailViewHolder holder, Product product) {

        holder.getTitleText().setText(product.getTitle());
        holder.getIntroText().setText(product.getIntroduction());
        holder.getConditionText().setText(product.getCondition());
        holder.getAuctionTypeText().setText(product.getAuctionType());
        holder.getExpiredText().setText(getDateToString(product.getExpired()));
        holder.getStartingPriceText().setText(String.valueOf(product.getCurrentPrice()));
        holder.getIncreaseText().setText(String.valueOf(product.getIncrease()));
        holder.getReservePriceText().setText(String.valueOf(product.getReservePrice()));

    }

    private String getDateToString(long millSeconds) {
        Date d = new Date(millSeconds);
        SimpleDateFormat sf = new SimpleDateFormat("MM 月 dd 日 HH 時 mm 分");
        return sf.format(d);
    }

    public void updateData(Product product) {
        mProduct = product;
        notifyDataSetChanged();
    }
}
