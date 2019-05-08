package com.johnson.bid.bought;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.johnson.bid.Bid;
import com.johnson.bid.MainActivity;
import com.johnson.bid.R;
import com.johnson.bid.data.ChatRoom;
import com.johnson.bid.data.Product;
import com.johnson.bid.util.Constants;
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.UserManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.johnson.bid.MainMvpController.BOUGHTDETAIL;

public class BoughtDetailAdapter extends RecyclerView.Adapter {

    private BoughtDetailContract.Presenter mPresenter;
    private LinearSnapHelper mLinearSnapHelper;
    private MainActivity mMainActivity;
    private Product mProduct;

    public BoughtDetailAdapter(BoughtDetailContract.Presenter presenter, MainActivity mainActivity) {
        mPresenter = presenter;
        mMainActivity = mainActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BoughtDetailViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_bought_detail, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                Bid.getAppContext(), LinearLayoutManager.HORIZONTAL, false);
        BoughtDetailGalleryAdapter boughtDetailGalleryAdapter = new BoughtDetailGalleryAdapter(mProduct.getImages());

        if (mLinearSnapHelper == null) {
            mLinearSnapHelper = new LinearSnapHelper();
            mLinearSnapHelper.attachToRecyclerView(((BoughtDetailViewHolder) holder).getRecyclerGallery());
        }

        ((BoughtDetailViewHolder) holder).getRecyclerGallery().setAdapter(boughtDetailGalleryAdapter);
        ((BoughtDetailViewHolder) holder).getRecyclerGallery().setLayoutManager(layoutManager);

        bindBoughtDetailViewHolder((BoughtDetailViewHolder) holder, mProduct);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private class BoughtDetailViewHolder extends RecyclerView.ViewHolder {

        private Button mBtnBack;
        private RecyclerView mRecyclerGallery;
        private TextView mTextTitle;
        private TextView mTextIntro;
        private TextView mTextCondition;
        private TextView mTextAuctionType;
        private TextView mTextExpired;
        private TextView mTextPrice;
        private TextView mTextSeller;
        private Button mBtnConnectSeller;

        public BoughtDetailViewHolder(@NonNull View itemView) {
            super(itemView);

            mBtnBack = itemView.findViewById(R.id.btn_bought_back);
            mRecyclerGallery = itemView.findViewById(R.id.recycler_bought_gallery);
            mTextTitle = itemView.findViewById(R.id.text_bought_title);
            mTextIntro = itemView.findViewById(R.id.text_bought_intro);
            mTextCondition = itemView.findViewById(R.id.text_bought_condition);
            mTextAuctionType = itemView.findViewById(R.id.text_bought_auction_type);
            mTextExpired = itemView.findViewById(R.id.text_bought_expired);
            mTextPrice = itemView.findViewById(R.id.text_bought_price);
            mTextSeller = itemView.findViewById(R.id.text_bought_seller);
            mBtnConnectSeller = itemView.findViewById(R.id.btn_bought_connect_seller);

            mBtnBack.setOnClickListener(v ->
                    mMainActivity.onBackPressed()
            );

            mBtnConnectSeller.setOnClickListener(v ->

                mPresenter.chatWithSeller()
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

        private TextView getTextPrice() {
            return mTextPrice;
        }

        private TextView getTextSeller() {
            return mTextSeller;
        }
    }

    private void bindBoughtDetailViewHolder(BoughtDetailViewHolder holder, Product product) {

        holder.getTextTitle().setText(product.getTitle());
        holder.getTextIntro().setText(product.getIntroduction());
        holder.getTextCondition().setText(product.getCondition());
        holder.getTextPrice().setText(String.valueOf(product.getCurrentPrice()));
        holder.getTextAuctionType().setText(product.getAuctionType());
        holder.getTextExpired().setText(getDateToString(product.getExpired()));
        holder.getTextSeller().setText(product.getSellerName());
    }

    private String getDateToString(long millSeconds) {
        Date d = new Date(millSeconds);
        SimpleDateFormat sf = new SimpleDateFormat(mMainActivity.getString(R.string.simple_date_format));
        return sf.format(d);
    }

    public void updateData(Product product) {
        mProduct = product;
        notifyDataSetChanged();
    }
}
