package com.johnson.bid.selling;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.johnson.bid.Bid;
import com.johnson.bid.MainActivity;
import com.johnson.bid.R;
import com.johnson.bid.data.Product;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.johnson.bid.MainMvpController.ENGLISH;

public class SellingDetailAdapter extends RecyclerView.Adapter {

    private SellingDetailContract.Presenter mPresenter;
    private LinearSnapHelper mLinearSnapHelper;
    private String mAuctionType;
    private Product mProduct;
    private MainActivity mMainActivity;

    public SellingDetailAdapter(SellingDetailContract.Presenter presenter, String auctionType, MainActivity mainActivity) {
        mPresenter = presenter;
        mAuctionType = auctionType;
        mMainActivity = mainActivity;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mAuctionType.equals(ENGLISH)) {
            return new SellingEnglishViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_selling_detail_e, viewGroup, false));
        } else {
            return new SellingSealedViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_selling_detail_s, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                Bid.getAppContext(), LinearLayoutManager.HORIZONTAL, false);
        SellingDetailGalleryAdapter sellingDetailGalleryAdapter = new SellingDetailGalleryAdapter(mPresenter, mProduct.getImages());

        if (mLinearSnapHelper == null) {
            mLinearSnapHelper = new LinearSnapHelper();
        }

        if (mAuctionType.equals(ENGLISH)) {
            mLinearSnapHelper.attachToRecyclerView(((SellingEnglishViewHolder) holder).getRecyclerGallery());

            ((SellingEnglishViewHolder) holder).getRecyclerGallery().setAdapter(sellingDetailGalleryAdapter);
            ((SellingEnglishViewHolder) holder).getRecyclerGallery().setLayoutManager(layoutManager);

            bindSellingEnglishViewHolder((SellingEnglishViewHolder) holder, mProduct);
        } else {
            mLinearSnapHelper.attachToRecyclerView(((SellingSealedViewHolder) holder).getRecyclerGallery());

            ((SellingSealedViewHolder) holder).getRecyclerGallery().setAdapter(sellingDetailGalleryAdapter);
            ((SellingSealedViewHolder) holder).getRecyclerGallery().setLayoutManager(layoutManager);

            bindSellingSealedViewHolder((SellingSealedViewHolder) holder, mProduct);
        }

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private class SellingEnglishViewHolder extends RecyclerView.ViewHolder {

        private Button mBtnBack;
        private RecyclerView mRecyclerGallery;
        private TextView mTextTitle;
        private TextView mTextIntro;
        private TextView mTextCondition;
        private TextView mTextAuctionType;
        private TextView mTextExpire;
        private TextView mTextRemainingTime;
        private TextView mTextBuyer;
        private TextView mTextPrice;
        private TextView mTextPlaceBidTimes;
        private TextView mTextIncrease;
        private TextView mTextReservePrice;
        private Button mBtnDelete;

        public SellingEnglishViewHolder(@NonNull View itemView) {
            super(itemView);

            mBtnBack = itemView.findViewById(R.id.button_selling_e_back);
            mRecyclerGallery = itemView.findViewById(R.id.recycler_selling_e_gallery);
            mTextTitle = itemView.findViewById(R.id.text_selling_e_title);
            mTextIntro = itemView.findViewById(R.id.text_selling_e_intro);
            mTextCondition = itemView.findViewById(R.id.text_selling_e_condition);
            mTextAuctionType = itemView.findViewById(R.id.text_selling_e_auction_type);
            mTextExpire = itemView.findViewById(R.id.text_selling_e_expire);
            mTextRemainingTime = itemView.findViewById(R.id.text_selling_e_remaining_time);
            mTextBuyer = itemView.findViewById(R.id.text_selling_e_buyer);
            mTextPrice = itemView.findViewById(R.id.text_selling_e_current_price);
            mTextPlaceBidTimes = itemView.findViewById(R.id.text_selling_e_bit_times);
            mTextIncrease = itemView.findViewById(R.id.text_selling_e_increase);
            mTextReservePrice = itemView.findViewById(R.id.text_selling_e_reserve_price);
            mBtnDelete = itemView.findViewById(R.id.button_selling_e_delete);

            mBtnBack.setOnClickListener(v ->
                    mMainActivity.onBackPressed()
            );

            mBtnDelete.setOnClickListener(v ->
                    mPresenter.openDeleteProductDialog(mProduct)
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

        private TextView getTextExpire() {
            return mTextExpire;
        }

        private TextView getTextRemainingTime() {
            return mTextRemainingTime;
        }

        private TextView getTextBuyer() {
            return mTextBuyer;
        }

        private TextView getTextPrice() {
            return mTextPrice;
        }

        private TextView getTextPlaceBidTimes() {
            return mTextPlaceBidTimes;
        }

        private TextView getTextIncrease() {
            return mTextIncrease;
        }

        private TextView getTextReservePrice() {
            return mTextReservePrice;
        }

    }

    private void bindSellingEnglishViewHolder(SellingEnglishViewHolder holder, Product product) {

        holder.getTextTitle().setText(product.getTitle());
        holder.getTextIntro().setText(product.getIntroduction());
        holder.getTextCondition().setText(product.getCondition());
        holder.getTextAuctionType().setText(product.getAuctionType());
        holder.getTextExpire().setText(getDateToString(product.getExpired()));
        holder.getTextPrice().setText(String.valueOf(product.getCurrentPrice()));
        holder.getTextPlaceBidTimes().setText(String.valueOf(product.getPlaceBidTimes()));
        holder.getTextIncrease().setText(String.valueOf(product.getIncrease()));
        holder.getTextReservePrice().setText(String.valueOf(product.getReservePrice()));

        if (product.getHighestUserId() == -1) {
            holder.getTextBuyer().setText(mMainActivity.getString(R.string.nobody));
        } else {
            holder.getTextBuyer().setText(product.getBuyerName());
        }

        long remainingTime = product.getExpired() - System.currentTimeMillis();
        if (remainingTime > 0) {
            timer(holder, product, remainingTime);
        } else {
            holder.getTextRemainingTime().setText(mMainActivity.getString(R.string.bid_finish));
        }
    }

    private class SellingSealedViewHolder extends RecyclerView.ViewHolder {

        private Button mBtnBack;
        private RecyclerView mRecyclerGallery;
        private TextView mTextTitle;
        private TextView mTextIntro;
        private TextView mTextCondition;
        private TextView mTextAuctionType;
        private TextView mTextExpire;
        private TextView mTextRemainingTime;
        private TextView mTextReservePrice;
        private Button mBtnDelete;

        public SellingSealedViewHolder(@NonNull View itemView) {
            super(itemView);

            mBtnBack = itemView.findViewById(R.id.button_selling_s_back);
            mRecyclerGallery = itemView.findViewById(R.id.recycler_selling_s_gallery);
            mTextTitle = itemView.findViewById(R.id.text_selling_s_title);
            mTextIntro = itemView.findViewById(R.id.text_selling_s_intro);
            mTextCondition = itemView.findViewById(R.id.text_selling_s_condition);
            mTextAuctionType = itemView.findViewById(R.id.text_selling_s_auction_type);
            mTextExpire = itemView.findViewById(R.id.text_selling_s_expire);
            mTextRemainingTime = itemView.findViewById(R.id.text_selling_s_remaining_time);
            mTextReservePrice = itemView.findViewById(R.id.text_selling_s_reserve_price);
            mBtnDelete = itemView.findViewById(R.id.button_selling_s_delete);

            mBtnBack.setOnClickListener(v ->
                    mMainActivity.onBackPressed()
            );

            mBtnDelete.setOnClickListener(v ->
                    mPresenter.openDeleteProductDialog(mProduct)
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

        private TextView getTextExpire() {
            return mTextExpire;
        }

        private TextView getTextRemainingTime() {
            return mTextRemainingTime;
        }

        private TextView getTextReservePrice() {
            return mTextReservePrice;
        }
    }

    private void bindSellingSealedViewHolder(SellingSealedViewHolder holder, Product product) {

        holder.getTextTitle().setText(product.getTitle());
        holder.getTextIntro().setText(product.getIntroduction());
        holder.getTextCondition().setText(product.getCondition());
        holder.getTextAuctionType().setText(product.getAuctionType());
        holder.getTextExpire().setText(getDateToString(product.getExpired()));
        holder.getTextReservePrice().setText(String.valueOf(product.getReservePrice()));

        long remainingTime = product.getExpired() - System.currentTimeMillis();
        if (remainingTime > 0) {
            timer(holder, product, remainingTime);
        } else {
            holder.getTextRemainingTime().setText(mMainActivity.getString(R.string.bid_finish));
        }
    }

    private String getDateToString(long millSeconds) {
        Date d = new Date(millSeconds);
        SimpleDateFormat sf = new SimpleDateFormat(mMainActivity.getString(R.string.simple_date_format_MdHm));
        return sf.format(d);
    }

    private String getRemainingTimeToString(long millSeconds) {

        long days = millSeconds / (1000 * 60 * 60 * 24);
        long hours = (millSeconds - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (millSeconds - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (millSeconds - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;

        String time = days + " " + Bid.getAppContext().getString(R.string.timer_day) + " "
                + hours +  " " + Bid.getAppContext().getString(R.string.timer_hour) + " "
                + minutes + " " + Bid.getAppContext().getString(R.string.timer_minute) + " "
                + seconds + " " + Bid.getAppContext().getString(R.string.timer_second);

        return time;
    }

    private void timer(@NonNull RecyclerView.ViewHolder holder, Product product, long remainingTime) {
        TextView textView;

        if (holder instanceof SellingEnglishViewHolder) {
            textView = ((SellingEnglishViewHolder) holder).getTextRemainingTime();
        } else {
            textView = ((SellingSealedViewHolder) holder).getTextRemainingTime();
        }

        new CountDownTimer(remainingTime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText(getRemainingTimeToString(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                textView.setText(mMainActivity.getString(R.string.bid_finish));
            }
        }.start();
    }

    public void updateData(Product product) {
        mProduct = product;
        notifyDataSetChanged();
    }
}
