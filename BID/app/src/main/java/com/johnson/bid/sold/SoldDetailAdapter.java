package com.johnson.bid.sold;

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
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.UserManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.johnson.bid.MainMvpController.SOLDDETAIL;

public class SoldDetailAdapter extends RecyclerView.Adapter {

    private SoldDetailContract.Presenter mPresenter;
    private LinearSnapHelper mLinearSnapHelper;
    private MainActivity mMainActivity;
    private Product mProduct;

    public SoldDetailAdapter(SoldDetailContract.Presenter presenter, MainActivity mainActivity) {
        mPresenter = presenter;
        mMainActivity = mainActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SoldDetailViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_sold_detail, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                Bid.getAppContext(), LinearLayoutManager.HORIZONTAL, false);
        SoldDetailGalleryAdapter soldDetailGalleryAdapter = new SoldDetailGalleryAdapter(mPresenter, mProduct.getImages());

        if (mLinearSnapHelper == null) {
            mLinearSnapHelper = new LinearSnapHelper();
            mLinearSnapHelper.attachToRecyclerView(((SoldDetailViewHolder) holder).getRecyclerGallery());
        }

        ((SoldDetailViewHolder) holder).getRecyclerGallery().setAdapter(soldDetailGalleryAdapter);
        ((SoldDetailViewHolder) holder).getRecyclerGallery().setLayoutManager(layoutManager);

        bindSoldDetailViewHolder((SoldDetailViewHolder) holder, mProduct);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private class SoldDetailViewHolder extends RecyclerView.ViewHolder {

        private Button mBtnBack;
        private RecyclerView mRecyclerGallery;
        private TextView mTextTitle;
        private TextView mTextIntro;
        private TextView mTextCondition;
        private TextView mTextAuctionType;
        private TextView mTextExpired;
        private TextView mTextPrice;
        private TextView mTextBuyer;
        private Button mBtnConnectBuyer;

        public SoldDetailViewHolder(@NonNull View itemView) {
            super(itemView);

            mBtnBack = itemView.findViewById(R.id.button_sold_back);
            mRecyclerGallery = itemView.findViewById(R.id.recycler_sold_gallery);
            mTextTitle = itemView.findViewById(R.id.text_sold_title);
            mTextIntro = itemView.findViewById(R.id.text_sold_intro);
            mTextCondition = itemView.findViewById(R.id.text_sold_condition);
            mTextAuctionType = itemView.findViewById(R.id.text_sold_auction_type);
            mTextExpired = itemView.findViewById(R.id.text_sold_expired);
            mTextPrice = itemView.findViewById(R.id.text_sold_price);
            mTextBuyer = itemView.findViewById(R.id.text_sold_buyer);
            mBtnConnectBuyer = itemView.findViewById(R.id.button_sold_connect_buyer);

            mBtnBack.setOnClickListener(v ->
                    mMainActivity.onBackPressed()
            );

            mBtnConnectBuyer.setOnClickListener(v ->

                mPresenter.chatWithBuyer()
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

        private TextView getTextBuyer() {
            return mTextBuyer;
        }

    }

    private void bindSoldDetailViewHolder(SoldDetailViewHolder holder, Product product) {

        holder.getTextTitle().setText(product.getTitle());
        holder.getTextIntro().setText(product.getIntroduction());
        holder.getTextCondition().setText(product.getCondition());
        holder.getTextPrice().setText(String.valueOf(product.getCurrentPrice()));
        holder.getTextAuctionType().setText(product.getAuctionType());
        holder.getTextExpired().setText(getDateToString(product.getExpired()));
        holder.getTextBuyer().setText(product.getBuyerName());

    }

    private String getDateToString(long millSeconds) {
        Date d = new Date(millSeconds);
        SimpleDateFormat sf = new SimpleDateFormat(Bid.getAppContext().getString(R.string.simple_date_format_MdHm));
        return sf.format(d);
    }

    public void updateData(Product product) {
        mProduct = product;
        notifyDataSetChanged();
    }
}
