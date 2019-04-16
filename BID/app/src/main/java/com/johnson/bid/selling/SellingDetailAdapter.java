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


        if (mAuctionType.equals(ENGLISH)) {
            if (mLinearSnapHelper == null) {
                mLinearSnapHelper = new LinearSnapHelper();
                mLinearSnapHelper.attachToRecyclerView(((SellingEnglishViewHolder) holder).getGalleryRecycler());
            }
            ((SellingEnglishViewHolder) holder).getGalleryRecycler().setAdapter(sellingDetailGalleryAdapter);
            ((SellingEnglishViewHolder) holder).getGalleryRecycler().setLayoutManager(layoutManager);

            bindSellingEnglishViewHolder((SellingEnglishViewHolder) holder, mProduct);
        } else {
            if (mLinearSnapHelper == null) {
                mLinearSnapHelper = new LinearSnapHelper();
                mLinearSnapHelper.attachToRecyclerView(((SellingSealedViewHolder) holder).getGalleryRecycler());
            }
            ((SellingSealedViewHolder) holder).getGalleryRecycler().setAdapter(sellingDetailGalleryAdapter);
            ((SellingSealedViewHolder) holder).getGalleryRecycler().setLayoutManager(layoutManager);

            bindSellingSealedViewHolder((SellingSealedViewHolder) holder, mProduct);
        }

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private class SellingEnglishViewHolder extends RecyclerView.ViewHolder {

        private Button mBackBtn;
        private RecyclerView mGalleryRecycler;
        private TextView mTitleText;
        private TextView mIntroText;
        private TextView mConditionText;
        private TextView mAuctionTypeText;
        private TextView mExpireText;
        private TextView mLastTimeText;
        private TextView mBuyerText;
        private TextView mPriceText;
        private TextView mPeopleText;
        private TextView mIncreaseText;
        private TextView mReservePriceText;
        private Button mDeleteBtn;

        public SellingEnglishViewHolder(@NonNull View itemView) {
            super(itemView);

            mBackBtn = itemView.findViewById(R.id.button_selling_e_back);
            mGalleryRecycler = itemView.findViewById(R.id.recycler_selling_e_gallery);
            mTitleText = itemView.findViewById(R.id.text_selling_e_title);
            mIntroText = itemView.findViewById(R.id.text_selling_e_intro);
            mConditionText = itemView.findViewById(R.id.text_selling_e_condition);
            mAuctionTypeText = itemView.findViewById(R.id.text_selling_e_auction_type);
            mExpireText = itemView.findViewById(R.id.text_selling_e_expire);
            mLastTimeText = itemView.findViewById(R.id.text_selling_e_last_time);
            mBuyerText = itemView.findViewById(R.id.text_selling_e_buyer);
            mPriceText = itemView.findViewById(R.id.text_selling_e_current_price);
            mPeopleText = itemView.findViewById(R.id.text_selling_e_bit_times);
            mIncreaseText = itemView.findViewById(R.id.text_selling_e_increase);
            mReservePriceText = itemView.findViewById(R.id.text_selling_e_reserve_price);
            mDeleteBtn = itemView.findViewById(R.id.button_selling_e_delete);

            mBackBtn.setOnClickListener(v ->
                    mMainActivity.onBackPressed()
            );

            mDeleteBtn.setOnClickListener(v ->
                    mPresenter.openDeleteProductDialog(mProduct)
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

        private TextView getExpireText() {
            return mExpireText;
        }

        private TextView getLastTimeText() {
            return mLastTimeText;
        }

        private TextView getBuyerText() {
            return mBuyerText;
        }

        private TextView getPriceText() {
            return mPriceText;
        }

        private TextView getPeopleText() {
            return mPeopleText;
        }

        private TextView getIncreaseText() {
            return mIncreaseText;
        }

        private TextView getReservePriceText() {
            return mReservePriceText;
        }

    }

    private void bindSellingEnglishViewHolder(SellingEnglishViewHolder holder, Product product) {

        holder.getTitleText().setText(product.getTitle());
        holder.getIntroText().setText(product.getIntroduction());
        holder.getConditionText().setText(product.getCondition());
        holder.getAuctionTypeText().setText(product.getAuctionType());
        holder.getExpireText().setText(String.valueOf(product.getExpired()));
        holder.getPriceText().setText(String.valueOf(product.getCurrentPrice()));
        holder.getPeopleText().setText(String.valueOf(product.getParticipantsNumber()));
        holder.getIncreaseText().setText(String.valueOf(product.getIncrease()));
        holder.getReservePriceText().setText(String.valueOf(product.getReservePrice()));

        if (product.getHighestUserId() == -1) {
            holder.getBuyerText().setText(mMainActivity.getString(R.string.nobody));
        } else {
            holder.getBuyerText().setText(String.valueOf(product.getHighestUserId()));
        }

        timer(holder, product);

    }

    private class SellingSealedViewHolder extends RecyclerView.ViewHolder {

        private Button mBackBtn;
        private RecyclerView mGalleryRecycler;
        private TextView mTitleText;
        private TextView mIntroText;
        private TextView mConditionText;
        private TextView mAuctionTypeText;
        private TextView mExpireText;
        private TextView mLastTimeText;
        private TextView mReservePriceText;
        private Button mDeleteBtn;

        public SellingSealedViewHolder(@NonNull View itemView) {
            super(itemView);

            mBackBtn = itemView.findViewById(R.id.button_selling_s_back);
            mGalleryRecycler = itemView.findViewById(R.id.recycler_selling_s_gallery);
            mTitleText = itemView.findViewById(R.id.text_selling_s_title);
            mIntroText = itemView.findViewById(R.id.text_selling_s_intro);
            mConditionText = itemView.findViewById(R.id.text_selling_s_condition);
            mAuctionTypeText = itemView.findViewById(R.id.text_selling_s_auction_type);
            mExpireText = itemView.findViewById(R.id.text_selling_s_expire);
            mLastTimeText = itemView.findViewById(R.id.text_selling_s_last_time);
            mReservePriceText = itemView.findViewById(R.id.text_selling_s_reserve_price);
            mDeleteBtn = itemView.findViewById(R.id.button_selling_s_delete);

            mBackBtn.setOnClickListener(v ->
                    mMainActivity.onBackPressed()
            );

            mDeleteBtn.setOnClickListener(v ->
                    mPresenter.openDeleteProductDialog(mProduct)
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

        private TextView getExpireText() {
            return mExpireText;
        }

        private TextView getLastTimeText() {
            return mLastTimeText;
        }

        private TextView getReservePriceText() {
            return mReservePriceText;
        }
    }

    private void bindSellingSealedViewHolder(SellingSealedViewHolder holder, Product product) {

        holder.getTitleText().setText(product.getTitle());
        holder.getIntroText().setText(product.getIntroduction());
        holder.getConditionText().setText(product.getCondition());
        holder.getAuctionTypeText().setText(product.getAuctionType());
        holder.getExpireText().setText(String.valueOf(product.getExpired()));
        holder.getReservePriceText().setText(String.valueOf(product.getReservePrice()));

        timer(holder, product);

    }

    private String getDateToString(long millSeconds) {

        long days = millSeconds / (1000 * 60 * 60 * 24);
        long hours = (millSeconds - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (millSeconds - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (millSeconds - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;

        String time = days + "天 " + hours + "時 " + minutes + "分 " + seconds + "秒";
        return time;
    }

    private void timer(@NonNull RecyclerView.ViewHolder holder, Product product) {
        long lastTime = product.getExpired() - System.currentTimeMillis();
        TextView textView;

        if (holder instanceof SellingEnglishViewHolder) {
            textView = ((SellingEnglishViewHolder) holder).getLastTimeText();
        } else {
            textView = ((SellingSealedViewHolder) holder).getLastTimeText();
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

    public void updateData(Product product) {
        mProduct = product;
        notifyDataSetChanged();
    }
}
