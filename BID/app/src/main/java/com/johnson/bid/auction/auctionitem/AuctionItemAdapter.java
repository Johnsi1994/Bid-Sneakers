package com.johnson.bid.auction.auctionitem;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.johnson.bid.Bid;
import com.johnson.bid.MainMvpController;
import com.johnson.bid.R;
import com.johnson.bid.data.ChatRoom;
import com.johnson.bid.data.Product;
import com.johnson.bid.data.User;
import com.johnson.bid.util.CardViewImageOutlineProvider;
import com.johnson.bid.util.Constants;
import com.johnson.bid.util.Firebase;
import com.johnson.bid.util.ImageManager;
import com.johnson.bid.util.UserManager;

import java.util.ArrayList;

import static com.johnson.bid.MainMvpController.ENGLISH;
import static com.johnson.bid.MainMvpController.SEALED;

public class AuctionItemAdapter extends RecyclerView.Adapter {

    private static final int TYPE_LOADING = 0;
    private static final int TYPE_PRODUCT_ENGLISH = 0x01;
    private static final int TYPE_PRODUCT_SEALED = 0x02;

    private AuctionItemContract.Presenter mPresenter;
    private String mAuctionType;
    private ArrayList<Product> mProductList;
    private SparseArray<CountDownTimer> countDownMap;

    public AuctionItemAdapter(AuctionItemContract.Presenter presenter, @MainMvpController.AuctionType String auctionType) {
        mPresenter = presenter;
        mAuctionType = auctionType;
        countDownMap = new SparseArray<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType == TYPE_PRODUCT_ENGLISH) {

            return new EnglishAuctionViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_product_most, viewGroup, false));
        } else if (viewType == TYPE_PRODUCT_SEALED) {

            return new SealedAuctionViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_product_sealed, viewGroup, false));
        } else {

            return new LoadingViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_all_loading, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if (mProductList != null) {
            if (viewHolder instanceof EnglishAuctionViewHolder) {
                bindEnglishAuctionViewHolder((EnglishAuctionViewHolder) viewHolder, mProductList.get(i));
            } else {
                bindSealedAuctionViewHolder((SealedAuctionViewHolder) viewHolder, mProductList.get(i));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mProductList == null) {
            return 0;
        } else {
            return mProductList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {

        switch (mAuctionType) {
            case ENGLISH:
                return TYPE_PRODUCT_ENGLISH;
            case SEALED:
                return TYPE_PRODUCT_SEALED;
            default:
                return TYPE_LOADING;
        }
    }

    private class EnglishAuctionViewHolder extends RecyclerView.ViewHolder {

        private CardView mLayoutEnglishAuction;
        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        private TextView mTextPrice;
        private TextView mTextPlaceBidTimes;
        CountDownTimer countDownTimer;

        private EnglishAuctionViewHolder(View itemView) {
            super(itemView);

            mLayoutEnglishAuction = itemView.findViewById(R.id.layout_product_most_brief);
            mImageMain = itemView.findViewById(R.id.image_product_most);
            mTextTitle = itemView.findViewById(R.id.text_product_most_title);
            mTextTime = itemView.findViewById(R.id.text_product_most_remaining_time);
            mTextPrice = itemView.findViewById(R.id.text_product_most_price);
            mTextPlaceBidTimes = itemView.findViewById(R.id.text_product_most_place_bid_times);
        }

        private CardView getLayoutEnglishAuction() {
            return mLayoutEnglishAuction;
        }

        private ImageView getImageMain() {
            return mImageMain;
        }

        private TextView getTextTitle() {
            return mTextTitle;
        }

        private TextView getTextTime() {
            return mTextTime;
        }

        private TextView getTextPrice() {
            return mTextPrice;
        }

        private TextView getTextPlaceBidTimes() {
            return mTextPlaceBidTimes;
        }
    }

    private void bindEnglishAuctionViewHolder(EnglishAuctionViewHolder holder, final Product product) {

        holder.getLayoutEnglishAuction().setOnClickListener(v -> {

            if (UserManager.getInstance().getUser().getId() == product.getSellerId()) {

                mPresenter.openSelling(ENGLISH, product);
            } else {

                mPresenter.openBidding(ENGLISH, product);
            }

            mPresenter.hideToolbarAndBottomNavigation();
        });

        holder.getImageMain().setOutlineProvider(new CardViewImageOutlineProvider());
        ImageManager.getInstance().setBriefImageByUrl(holder.getImageMain(), product.getImages().get(0));

        holder.getTextTitle().setText(product.getTitle());

        long lastTime = product.getExpired() - System.currentTimeMillis();
        if (holder.countDownTimer != null) {
            holder.countDownTimer.cancel();
        }
        holder.countDownTimer = new CountDownTimer(lastTime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                holder.getTextTime().setText(getDateToString(millisUntilFinished));
            }

            @Override
            public void onFinish() {

                holder.getTextTime().setText(Bid.getAppContext().getString(R.string.bid_finish));
                mPresenter.productResult(product, ENGLISH);
            }
        }.start();

        countDownMap.put(holder.getTextTime().hashCode(), holder.countDownTimer);

        holder.getTextPrice().setText(String.valueOf(product.getCurrentPrice()));

        holder.getTextPlaceBidTimes().setText(String.valueOf(product.getPlaceBidTimes()));

    }

    private class SealedAuctionViewHolder extends RecyclerView.ViewHolder {

        private CardView mLayoutSealedAuction;
        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        CountDownTimer countDownTimer;

        private SealedAuctionViewHolder(View itemView) {
            super(itemView);

            mLayoutSealedAuction = itemView.findViewById(R.id.layout_product_sealed_brief);
            mImageMain = itemView.findViewById(R.id.image_product_sealed);
            mTextTitle = itemView.findViewById(R.id.text_product_sealed_title);
            mTextTime = itemView.findViewById(R.id.text_product_sealed_remaining_time);

        }

        private CardView getLayoutSealedAuction() {
            return mLayoutSealedAuction;
        }

        private ImageView getImageMain() {
            return mImageMain;
        }

        private TextView getTextTitle() {
            return mTextTitle;
        }

        private TextView getTextTime() {
            return mTextTime;
        }
    }

    private void bindSealedAuctionViewHolder(SealedAuctionViewHolder holder, Product product) {

        holder.getLayoutSealedAuction().setOnClickListener(v -> {

            if (UserManager.getInstance().getUser().getId() == product.getSellerId()) {

                mPresenter.openSelling(SEALED, product);
            } else {

                mPresenter.openBidding(SEALED, product);
            }

            mPresenter.hideToolbarAndBottomNavigation();
        });

        holder.getImageMain().setOutlineProvider(new CardViewImageOutlineProvider());
        ImageManager.getInstance().setBriefImageByUrl(holder.getImageMain(), product.getImages().get(0));

        holder.getTextTitle().setText(product.getTitle());

        long lastTime = product.getExpired() - System.currentTimeMillis();
        if (holder.countDownTimer != null) {
            holder.countDownTimer.cancel();
        }
        holder.countDownTimer = new CountDownTimer(lastTime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                holder.getTextTime().setText(getDateToString(millisUntilFinished));
            }

            @Override
            public void onFinish() {

                holder.getTextTime().setText(Bid.getAppContext().getString(R.string.bid_finish));
                mPresenter.productResult(product, SEALED);
            }
        }.start();

        countDownMap.put(holder.getTextTime().hashCode(), holder.countDownTimer);
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }


    public void updateData(ArrayList<Product> productList) {
        mProductList = productList;
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

    public void cancelAllTimers() {
        if (countDownMap == null) {
            return;
        }

        for (int i = 0, length = countDownMap.size(); i < length; i++) {
            CountDownTimer cdt = countDownMap.get(countDownMap.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }
}
