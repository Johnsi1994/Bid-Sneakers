package com.johnson.bid.bidding;

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
        BiddingDetailGalleryAdapter biddingDetailGalleryAdapter = new BiddingDetailGalleryAdapter(mPresenter, mProduct.getImages());


        if (mAuctionType.equals(ENGLISH)) {
            if (mLinearSnapHelper == null) {
                mLinearSnapHelper = new LinearSnapHelper();
                mLinearSnapHelper.attachToRecyclerView(((EnglishViewHolder) viewHolder).getGalleryRecycler());
            }
            ((EnglishViewHolder) viewHolder).getGalleryRecycler().setAdapter(biddingDetailGalleryAdapter);
            ((EnglishViewHolder) viewHolder).getGalleryRecycler().setLayoutManager(layoutManager);

            bindEnglishViewHolder((EnglishViewHolder) viewHolder, mProduct);
        } else {
            if (mLinearSnapHelper == null) {
                mLinearSnapHelper = new LinearSnapHelper();
                mLinearSnapHelper.attachToRecyclerView(((SealedViewHolder) viewHolder).getGalleryRecycler());
            }
            ((SealedViewHolder) viewHolder).getGalleryRecycler().setAdapter(biddingDetailGalleryAdapter);
            ((SealedViewHolder) viewHolder).getGalleryRecycler().setLayoutManager(layoutManager);

            bindSealedViewHolder((SealedViewHolder) viewHolder, mProduct);
        }

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private class EnglishViewHolder extends RecyclerView.ViewHolder {

        private Button mBackBtn;
        private RecyclerView mGalleryRecycler;
        private TextView mTitleText;
        private Button mEyesOnBtn;
        private TextView mIntroText;
        private TextView mConditionText;
        private TextView mLastTimeText;
        private TextView mPriceText;
        private TextView mIncreaseText;
        private TextView mBuyerText;
        private TextView mPeopleText;
        private TextView mSellerText;
        private Button mBidBtn;

        public EnglishViewHolder(@NonNull View itemView) {
            super(itemView);

            mBackBtn = itemView.findViewById(R.id.button_bid_back_e);
            mGalleryRecycler = itemView.findViewById(R.id.recycler_bid_gallery_e);
            mTitleText = itemView.findViewById(R.id.text_bid_title_e);
            mEyesOnBtn = itemView.findViewById(R.id.button_bid_eyes_on_e);
            mIntroText = itemView.findViewById(R.id.text_bid_intro_e);
            mConditionText = itemView.findViewById(R.id.text_bid_condition_e);
            mLastTimeText = itemView.findViewById(R.id.text_bid_last_time_e);
            mPriceText = itemView.findViewById(R.id.text_bid_price_e);
            mIncreaseText = itemView.findViewById(R.id.text_bid_increase_e);
            mBuyerText = itemView.findViewById(R.id.text_bid_buyer_e);
            mPeopleText = itemView.findViewById(R.id.text_bid_people_num_e);
            mSellerText = itemView.findViewById(R.id.text_bid_seller_e);
            mBidBtn = itemView.findViewById(R.id.button_bid_e);

            mBackBtn.setOnClickListener(v ->
                    mMainActivity.onBackPressed()
            );

            mBidBtn.setOnClickListener(v ->
                    mPresenter.openBidDialog(ENGLISH, mProduct)
            );

            eyeOnSwitch(mEyesOnBtn);
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

        private TextView getLastTimeText() {
            return mLastTimeText;
        }

        private TextView getPriceText() {
            return mPriceText;
        }

        private TextView getIncreaseText() {
            return mIncreaseText;
        }

        private TextView getBuyerText() {
            return mBuyerText;
        }

        private TextView getPeopleText() {
            return mPeopleText;
        }

        private TextView getSellerText() {
            return mSellerText;
        }
    }

    private void bindEnglishViewHolder(EnglishViewHolder holder, Product product) {

        holder.getTitleText().setText(product.getTitle());
        holder.getIntroText().setText(product.getIntroduction());
        holder.getConditionText().setText(product.getCondition());

        timer(holder, product);

        holder.getPriceText().setText(String.valueOf(product.getCurrentPrice()));
        holder.getIncreaseText().setText(String.valueOf(product.getIncrease()));
        if (product.getHighestUserId() == -1) {
            holder.getBuyerText().setText(mMainActivity.getString(R.string.nobody));
        } else {
            holder.getBuyerText().setText(product.getBuyerName());
        }
        holder.getPeopleText().setText(String.valueOf(product.getPlaceBidTimes()));
        holder.getSellerText().setText(product.getSellerName());

    }

    private class SealedViewHolder extends RecyclerView.ViewHolder {

        private Button mBackBtn;
        private RecyclerView mGalleryRecycler;
        private TextView mTitleText;
        private Button mEyesOnBtn;
        private TextView mIntroText;
        private TextView mConditionText;
        private TextView mLastTimeText;
        private TextView mSellerText;
        private Button mBidBtn;

        public SealedViewHolder(@NonNull View itemView) {
            super(itemView);

            mBackBtn = itemView.findViewById(R.id.button_bid_back_s);
            mGalleryRecycler = itemView.findViewById(R.id.recycler_bid_gallery_s);
            mTitleText = itemView.findViewById(R.id.text_bid_title_s);
            mEyesOnBtn = itemView.findViewById(R.id.button_bid_eyes_on_s);
            mIntroText = itemView.findViewById(R.id.text_bid_intro_s);
            mConditionText = itemView.findViewById(R.id.text_bid_condition_s);
            mLastTimeText = itemView.findViewById(R.id.text_bid_last_time_s);
            mSellerText = itemView.findViewById(R.id.text_bid_seller_s);
            mBidBtn = itemView.findViewById(R.id.button_bid_s);


            mBackBtn.setOnClickListener(v ->
                    mMainActivity.onBackPressed()
            );

            mBidBtn.setOnClickListener(v ->
                    mPresenter.openBidDialog(SEALED, mProduct)
            );

            eyeOnSwitch(mEyesOnBtn);
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

        private TextView getLastTimeText() {
            return mLastTimeText;
        }

        private TextView getSellerText() {
            return mSellerText;
        }

        private Button getBidBtn() {
            return mBidBtn;
        }
    }

    private void bindSealedViewHolder(SealedViewHolder holder, Product product) {

        holder.getTitleText().setText(product.getTitle());
        holder.getIntroText().setText(product.getIntroduction());
        holder.getConditionText().setText(product.getCondition());
        timer(holder, product);
        holder.getSellerText().setText(product.getSellerName());

        ArrayList<Long> myProductsId = UserManager.getInstance().getUser().getMyBiddingProductsId();
        boolean hasProduct = false;

        for (int i = 0; i < myProductsId.size(); i++) {
            if (myProductsId.get(i).equals(mProduct.getProductId())) {
                hasProduct = true;
            }
        }

        if (hasProduct) {
            holder.getBidBtn().setClickable(false);
            holder.getBidBtn().setText("已經出過價嘍~~");
        } else {
            holder.getBidBtn().setClickable(true);
            holder.getBidBtn().setText("我要出價");
        }

    }

    public void updateData(Product product) {
        mProduct = product;
        notifyDataSetChanged();
    }

    private String getDateToString(long millSeconds) {

        long days = millSeconds / (1000 * 60 * 60 * 24);
        long hours = (millSeconds - days * (1000 * 60 * 60 * 24)) / (1000* 60 * 60);
        long minutes = (millSeconds - days * (1000 * 60 * 60 * 24) - hours * (1000* 60 * 60)) / (1000 * 60);
        long seconds = (millSeconds - days * (1000 * 60 * 60 * 24) - hours * (1000* 60 * 60) - minutes * (1000 * 60)) / 1000;

        String time = days + " 天 " + hours + " 時 " + minutes + " 分 " + seconds + " 秒";
        return time;
    }

    private void timer(@NonNull RecyclerView.ViewHolder holder, Product product) {
        long lastTime = product.getExpired() - System.currentTimeMillis();
        TextView textView;

        if (holder instanceof EnglishViewHolder) {
            textView = ((EnglishViewHolder) holder).getLastTimeText();
        } else {
            textView = ((SealedViewHolder) holder).getLastTimeText();
        }

        new CountDownTimer(lastTime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText(getDateToString(millisUntilFinished));
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void eyeOnSwitch(Button mEyesOnBtn) {
        myEyesOn = UserManager.getInstance().getUser().getEyesOn();

        for (int i = 0; i < myEyesOn.size(); i++) {
            if (myEyesOn.get(i).equals(mProduct.getProductId())) {
                isEyesOn = true;
                mEyesOnBtn.setBackgroundResource(R.drawable.ic_eyes_on_selected);
            }
        }

        mEyesOnBtn.setOnClickListener(v -> {
            if (isEyesOn) {
                isEyesOn = false;
                mEyesOnBtn.setBackgroundResource(R.drawable.ic_eyes_on);

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
                mEyesOnBtn.setBackgroundResource(R.drawable.ic_eyes_on_selected);
                UserManager.getInstance().getUser().getEyesOn().add(mProduct.getProductId());
                UserManager.getInstance().setHasUserDataChange(true);
            }
        });
    }
}
