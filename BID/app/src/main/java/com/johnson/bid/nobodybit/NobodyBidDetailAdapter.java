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
            mLinearSnapHelper.attachToRecyclerView(((NobodyBitDetailViewHolder) holder).getRecyclerGallery());
        }
        ((NobodyBitDetailViewHolder) holder).getRecyclerGallery().setAdapter(nobodyBidDetailGalleryAdapter);
        ((NobodyBitDetailViewHolder) holder).getRecyclerGallery().setLayoutManager(layoutManager);

        bindNobodyBitDetailViewHolder((NobodyBitDetailViewHolder) holder, mProduct);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private class NobodyBitDetailViewHolder extends RecyclerView.ViewHolder {

        private Button mBtnBack;
        private RecyclerView mRecyclerGallery;
        private TextView mTextTitle;
        private TextView mTextIntro;
        private TextView mTextCondition;
        private TextView mTextAuctionType;
        private TextView mTextExpired;
        private TextView mTextStartingPrice;
        private TextView mTextIncrease;
        private TextView mTextReservePrice;
        private Button mBtnRepost;

        public NobodyBitDetailViewHolder(@NonNull View itemView) {
            super(itemView);

            mBtnBack = itemView.findViewById(R.id.button_nobody_bit_back);
            mRecyclerGallery = itemView.findViewById(R.id.recycler_nobody_bit_gallery);
            mTextTitle = itemView.findViewById(R.id.text_nobody_bit_title);
            mTextIntro = itemView.findViewById(R.id.text_nobody_bit_intro);
            mTextCondition = itemView.findViewById(R.id.text_nobody_bit_condition);
            mTextAuctionType = itemView.findViewById(R.id.text_nobody_bit_auction_type);
            mTextExpired = itemView.findViewById(R.id.text_nobody_bit_expired);
            mTextStartingPrice = itemView.findViewById(R.id.text_nobody_bit_starting_time);
            mTextIncrease = itemView.findViewById(R.id.text_nobody_bit_increase);
            mTextReservePrice = itemView.findViewById(R.id.text_nobody_bit_reserve_price);
            mBtnRepost = itemView.findViewById(R.id.button_nobody_bit_repost);

            mBtnBack.setOnClickListener(v ->
                    mMainActivity.onBackPressed()
            );

            mBtnRepost.setOnClickListener(v ->
                    Toast.makeText(mMainActivity, "Repost is Coming Soon", Toast.LENGTH_SHORT).show()
            );

        }

        private RecyclerView getRecyclerGallery() {
            return mRecyclerGallery;
        }

        private TextView getTextTitle() {
            return mTextTitle;
        }

        private TextView getTextIntro() {
            return mTextIntro;
        }

        private TextView getTextCondition() {
            return mTextCondition;
        }

        private TextView getTextAuctionType() {
            return mTextAuctionType;
        }

        private TextView getTextExpired() {
            return mTextExpired;
        }

        private TextView getTextStartingPrice() {
            return mTextStartingPrice;
        }

        private TextView getTextIncrease() {
            return mTextIncrease;
        }

        private TextView getTextReservePrice() {
            return mTextReservePrice;
        }

    }

    private void bindNobodyBitDetailViewHolder(NobodyBitDetailViewHolder holder, Product product) {

        holder.getTextTitle().setText(product.getTitle());
        holder.getTextIntro().setText(product.getIntroduction());
        holder.getTextCondition().setText(product.getCondition());
        holder.getTextAuctionType().setText(product.getAuctionType());
        holder.getTextExpired().setText(getDateToString(product.getExpired()));
        holder.getTextStartingPrice().setText(String.valueOf(product.getCurrentPrice()));
        holder.getTextIncrease().setText(String.valueOf(product.getIncrease()));
        holder.getTextReservePrice().setText(String.valueOf(product.getReservePrice()));

    }

    private String getDateToString(long millSeconds) {
        Date d = new Date(millSeconds);
        SimpleDateFormat sf = new SimpleDateFormat(mMainActivity.getString(R.string.simple_date_format_MdHm));
        return sf.format(d);
    }

    public void updateData(Product product) {
        mProduct = product;
        notifyDataSetChanged();
    }
}
