package com.johnson.bid.bidding;

import android.os.CountDownTimer;
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

import com.johnson.bid.Bid;
import com.johnson.bid.MainActivity;
import com.johnson.bid.R;
import com.johnson.bid.data.Product;
import com.johnson.bid.util.Constants;
import com.johnson.bid.util.UserManager;

import java.util.ArrayList;
import java.util.Iterator;

import static com.johnson.bid.MainMvpController.ENGLISH;
import static com.johnson.bid.MainMvpController.SEALED;

public class BiddingDetailAdapter extends RecyclerView.Adapter {

    private BiddingDetailContract.Presenter mPresenter;
    private LinearSnapHelper mLinearSnapHelper;
    private MainActivity mMainActivity;
    private String mAuctionType;
    private Product mProduct;
    private ArrayList<Long> myEyesOn;
    private Boolean isEyesOn = false;

    public BiddingDetailAdapter(BiddingDetailContract.Presenter presenter, MainActivity mainActivity, String auctionType) {
        mPresenter = presenter;
        mMainActivity = mainActivity;
        mAuctionType = auctionType;
        Log.d(Constants.TAG, "auctionType : " + auctionType);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (mAuctionType.equals(ENGLISH)) {
            return new EnglishViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_bidding_detail_e, viewGroup, false));
        } else {
            return new SealedViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_bidding_detail_s, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                Bid.getAppContext(), LinearLayoutManager.HORIZONTAL, false);
        BiddingDetailGalleryAdapter biddingDetailGalleryAdapter = new BiddingDetailGalleryAdapter(mProduct.getImages());
        if (mLinearSnapHelper == null) {
            mLinearSnapHelper = new LinearSnapHelper();
        }

        if (mAuctionType.equals(ENGLISH)) {

            mLinearSnapHelper.attachToRecyclerView(((EnglishViewHolder) viewHolder).getRecyclerGallery());

            ((EnglishViewHolder) viewHolder).getRecyclerGallery().setAdapter(biddingDetailGalleryAdapter);
            ((EnglishViewHolder) viewHolder).getRecyclerGallery().setLayoutManager(layoutManager);

            bindEnglishViewHolder((EnglishViewHolder) viewHolder, mProduct);
        } else {

            mLinearSnapHelper.attachToRecyclerView(((SealedViewHolder) viewHolder).getRecyclerGallery());

            ((SealedViewHolder) viewHolder).getRecyclerGallery().setAdapter(biddingDetailGalleryAdapter);
            ((SealedViewHolder) viewHolder).getRecyclerGallery().setLayoutManager(layoutManager);

            bindSealedViewHolder((SealedViewHolder) viewHolder, mProduct);
        }

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private class EnglishViewHolder extends RecyclerView.ViewHolder {

        private Button mBtnBack;
        private RecyclerView mRecyclerGallery;
        private TextView mTextTitle;
        private Button mBtnEyesOn;
        private TextView mTextIntro;
        private TextView mTextCondition;
        private TextView mTextRemainingTime;
        private TextView mTextPrice;
        private TextView mTextIncrease;
        private TextView mTextBuyer;
        private TextView mTextPlaceBidTimes;
        private TextView mTextSeller;
        private Button mBtnPlaceBid;

        public EnglishViewHolder(@NonNull View itemView) {
            super(itemView);

            mBtnBack = itemView.findViewById(R.id.btn_bid_e_back);
            mRecyclerGallery = itemView.findViewById(R.id.recycler_bid_e_gallery);
            mTextTitle = itemView.findViewById(R.id.text_bid_e_title);
            mBtnEyesOn = itemView.findViewById(R.id.btn_bid_e_eyes_on);
            mTextIntro = itemView.findViewById(R.id.text_bid_e_intro);
            mTextCondition = itemView.findViewById(R.id.text_bid_e_condition);
            mTextRemainingTime = itemView.findViewById(R.id.text_bid_e_remaining_time);
            mTextPrice = itemView.findViewById(R.id.text_bid_e_price);
            mTextPlaceBidTimes = itemView.findViewById(R.id.text_bid_e_place_bid_times);
            mTextIncrease = itemView.findViewById(R.id.text_bid_e_increase);
            mTextBuyer = itemView.findViewById(R.id.text_bidr_e_buye);
            mTextSeller = itemView.findViewById(R.id.text_bid_e_seller);
            mBtnPlaceBid = itemView.findViewById(R.id.btn_bid_e_place_bid);

            mBtnBack.setOnClickListener(v ->
                    mMainActivity.onBackPressed()
            );

            mBtnPlaceBid.setOnClickListener(v ->
                    mPresenter.openBidDialog(ENGLISH, mProduct)
            );

            eyeOnSwitch(mBtnEyesOn);
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

        private TextView getTextRemainingTime() {
            return mTextRemainingTime;
        }

        private TextView getTextPrice() {
            return mTextPrice;
        }

        private TextView getTextIncrease() {
            return mTextIncrease;
        }

        private TextView getTextBuyer() {
            return mTextBuyer;
        }

        private TextView getTextPlaceBidTimes() {
            return mTextPlaceBidTimes;
        }

        private TextView getTextSeller() {
            return mTextSeller;
        }

        private Button getBtnPlaceBid() {
            return mBtnPlaceBid;
        }
    }

    private void bindEnglishViewHolder(EnglishViewHolder holder, Product product) {

        holder.getTextTitle().setText(product.getTitle());
        holder.getTextIntro().setText(product.getIntroduction());
        holder.getTextCondition().setText(product.getCondition());

        long remainingTime = product.getExpired() - System.currentTimeMillis();
        if (remainingTime > 0) {
            timer(holder, remainingTime);
        } else {
            holder.getTextRemainingTime().setText(mMainActivity.getString(R.string.bid_finish));
            holder.getBtnPlaceBid().setText(mMainActivity.getString(R.string.bid_finish));
            holder.getBtnPlaceBid().setClickable(false);
        }

        holder.getTextPrice().setText(String.valueOf(product.getCurrentPrice()));
        holder.getTextIncrease().setText(String.valueOf(product.getIncrease()));
        if (product.getHighestUserId() == -1) {
            holder.getTextBuyer().setText(mMainActivity.getString(R.string.nobody));
        } else {
            holder.getTextBuyer().setText(product.getBuyerName());
        }
        holder.getTextPlaceBidTimes().setText(String.valueOf(product.getPlaceBidTimes()));
        holder.getTextSeller().setText(product.getSellerName());

    }

    private class SealedViewHolder extends RecyclerView.ViewHolder {

        private Button mBtnBack;
        private RecyclerView mRecyclerGallery;
        private TextView mTextTitle;
        private Button mBtnEyesOn;
        private TextView mTextIntro;
        private TextView mTextCondition;
        private TextView mTextRemainingTime;
        private TextView mTextSeller;
        private Button mBtnPlaceBid;

        public SealedViewHolder(@NonNull View itemView) {
            super(itemView);

            mBtnBack = itemView.findViewById(R.id.btn_bid_s_back);
            mRecyclerGallery = itemView.findViewById(R.id.recycler_bid_s_gallery);
            mTextTitle = itemView.findViewById(R.id.text_bid_s_title);
            mBtnEyesOn = itemView.findViewById(R.id.btn_bid_s_eyes_on);
            mTextIntro = itemView.findViewById(R.id.text_bid_s_intro);
            mTextCondition = itemView.findViewById(R.id.text_bid_s_condition);
            mTextRemainingTime = itemView.findViewById(R.id.text_bid_s_remaining_time);
            mTextSeller = itemView.findViewById(R.id.text_bid_s_seller);
            mBtnPlaceBid = itemView.findViewById(R.id.btn_bid_s_place_bid);


            mBtnBack.setOnClickListener(v ->
                    mMainActivity.onBackPressed()
            );

            mBtnPlaceBid.setOnClickListener(v ->
                    mPresenter.openBidDialog(SEALED, mProduct)
            );

            eyeOnSwitch(mBtnEyesOn);
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

        private TextView getTextRemainingTime() {
            return mTextRemainingTime;
        }

        private TextView getTextSeller() {
            return mTextSeller;
        }

        private Button getBtnPlaceBid() {
            return mBtnPlaceBid;
        }
    }

    private void bindSealedViewHolder(SealedViewHolder holder, Product product) {

        holder.getTextTitle().setText(product.getTitle());
        holder.getTextIntro().setText(product.getIntroduction());
        holder.getTextCondition().setText(product.getCondition());

        long remainingTime = product.getExpired() - System.currentTimeMillis();
        if (remainingTime > 0) {
            timer(holder, remainingTime);
        } else {
            holder.getTextRemainingTime().setText(mMainActivity.getString(R.string.bid_finish));
            holder.getBtnPlaceBid().setText(mMainActivity.getString(R.string.bid_finish));
            holder.getBtnPlaceBid().setClickable(false);
        }

        holder.getTextSeller().setText(product.getSellerName());

        ArrayList<Long> myProductsId = UserManager.getInstance().getUser().getMyBiddingProductsId();
        boolean hasProduct = false;

        for (int i = 0; i < myProductsId.size(); i++) {
            if (myProductsId.get(i).equals(mProduct.getProductId())) {
                hasProduct = true;
            }
        }

        if (hasProduct) {
            holder.getBtnPlaceBid().setClickable(false);
            holder.getBtnPlaceBid().setText(mMainActivity.getString(R.string.already_placed_bid));
        } else {
            holder.getBtnPlaceBid().setClickable(true);
            holder.getBtnPlaceBid().setText(mMainActivity.getString(R.string.place_bid));
        }

    }

    public void updateData(Product product) {
        mProduct = product;
        notifyDataSetChanged();
    }

    private String getDateToString(long millSeconds) {

        long days = millSeconds / (1000 * 60 * 60 * 24);
        long hours = (millSeconds - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (millSeconds - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (millSeconds - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;

        String time = days + " " + Bid.getAppContext().getString(R.string.timer_day) + " "
                + hours + " " + Bid.getAppContext().getString(R.string.timer_hour) + " "
                + minutes + " " + Bid.getAppContext().getString(R.string.timer_minute) + " "
                + seconds + " " + Bid.getAppContext().getString(R.string.timer_second);

        return time;
    }

    private void timer(@NonNull RecyclerView.ViewHolder holder, long remainingTime) {
        TextView textView;
        Button bidBtn;

        if (holder instanceof EnglishViewHolder) {
            textView = ((EnglishViewHolder) holder).getTextRemainingTime();
            bidBtn = ((EnglishViewHolder) holder).getBtnPlaceBid();
        } else {
            textView = ((SealedViewHolder) holder).getTextRemainingTime();
            bidBtn = ((SealedViewHolder) holder).getBtnPlaceBid();
        }

        new CountDownTimer(remainingTime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText(getDateToString(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                textView.setText(mMainActivity.getString(R.string.bid_finish));
                bidBtn.setText(mMainActivity.getString(R.string.bid_finish));
                bidBtn.setClickable(false);
            }
        }.start();
    }

    private void eyeOnSwitch(Button mBtnEyesOn) {
        myEyesOn = UserManager.getInstance().getUser().getEyesOn();

        for (int i = 0; i < myEyesOn.size(); i++) {
            if (myEyesOn.get(i).equals(mProduct.getProductId())) {
                isEyesOn = true;
                mBtnEyesOn.setBackgroundResource(R.drawable.ic_eyes_on_selected);
            }
        }

        mBtnEyesOn.setOnClickListener(v -> {
            if (isEyesOn) {
                isEyesOn = false;
                mBtnEyesOn.setBackgroundResource(R.drawable.ic_eyes_on);

                ArrayList<Long> EyesOnList = UserManager.getInstance().getUser().getEyesOn();
                Iterator<Long> iterator = EyesOnList.iterator();

                while (iterator.hasNext()) {
                    long id = iterator.next();
                    if (id == mProduct.getProductId()) {
                        iterator.remove();
                    }
                }

                UserManager.getInstance().setHasUserDataChange(true);

            } else {
                isEyesOn = true;
                mBtnEyesOn.setBackgroundResource(R.drawable.ic_eyes_on_selected);
                UserManager.getInstance().getUser().getEyesOn().add(mProduct.getProductId());
                UserManager.getInstance().setHasUserDataChange(true);
            }
        });
    }
}
