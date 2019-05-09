package com.johnson.bid.eyeson;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnson.bid.Bid;
import com.johnson.bid.MainActivity;
import com.johnson.bid.R;
import com.johnson.bid.data.Product;
import com.johnson.bid.util.CardViewImageOutlineProvider;
import com.johnson.bid.util.ImageManager;

import java.util.ArrayList;

import static com.johnson.bid.MainMvpController.ENGLISH;
import static com.johnson.bid.MainMvpController.SEALED;

public class EyesOnAdapter extends RecyclerView.Adapter {

    private EyesOnContract.Presenter mPresenter;
    private MainActivity mMainActivity;
    private SparseArray<CountDownTimer> mCountDownMap;
    private ArrayList<Product> mProductsList;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    public EyesOnAdapter(EyesOnContract.Presenter presenter, MainActivity mainActivity) {
        mPresenter = presenter;
        mMainActivity = mainActivity;
        mCountDownMap = new SparseArray<>();

        mNotificationManager = (NotificationManager) mMainActivity.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel(mMainActivity.getString(R.string.package_name), "J",
                            NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_product_most, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        bindViewHolder((ViewHolder) holder, mProductsList.get(i), i);
    }

    @Override
    public int getItemCount() {

        return (mProductsList == null) ? 0 : mProductsList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private CardView mLayoutBidding;
        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        private TextView mTextPrice;
        private TextView mTextPlaceBidTimes;
        private TextView mTextPriceTitle;
        private TextView mTextSlash;
        private TextView mTextPlaceBidTimesTitle;
        CountDownTimer countDownTimer;

        private ViewHolder(View itemView) {
            super(itemView);

            mLayoutBidding = itemView.findViewById(R.id.layout_product_most_brief);
            mImageMain = itemView.findViewById(R.id.image_product_most);
            mTextTitle = itemView.findViewById(R.id.text_product_most_title);
            mTextTime = itemView.findViewById(R.id.text_product_most_remaining_time);
            mTextPrice = itemView.findViewById(R.id.text_product_most_price);
            mTextPriceTitle = itemView.findViewById(R.id.text_product_most_price_title);
            mTextPlaceBidTimes = itemView.findViewById(R.id.text_product_most_place_bid_times);
            mTextPlaceBidTimesTitle = itemView.findViewById(R.id.text_product_most_place_bid_times_title);
            mTextSlash = itemView.findViewById(R.id.text_product_most_slash);
        }

        private CardView getLayoutBidding() {
            return mLayoutBidding;
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

        private TextView getTextPlaceBidTimesTitle() {
            return mTextPlaceBidTimesTitle;
        }

        private TextView getTextPriceTitle() {
            return mTextPriceTitle;
        }

        private TextView getTextSlash() {
            return mTextSlash;
        }
    }

    private void bindViewHolder(ViewHolder holder, Product product, int i) {


        holder.getLayoutBidding().setOnClickListener(v -> {

            if (product.getAuctionType().equals(mMainActivity.getString(R.string.auction_type_english))) {
                mPresenter.openBidding(ENGLISH, product);
            } else {
                mPresenter.openBidding(SEALED, product);
            }

            mPresenter.hideToolbarAndBottomNavigation();
        });

        holder.getImageMain().setOutlineProvider(new CardViewImageOutlineProvider());
        ImageManager.getInstance().setBriefImageByUrl(holder.getImageMain(), product.getImages().get(0));

        holder.getTextTitle().setText(product.getTitle());

        if (product.getAuctionType().equals(mMainActivity.getString(R.string.auction_type_english))) {

            holder.getTextPrice().setText(String.valueOf(product.getCurrentPrice()));

            holder.getTextPlaceBidTimes().setText(String.valueOf(product.getPlaceBidTimes()));

        } else {

            holder.getTextTitle().setPadding(0, 32, 0, 0);

            holder.getTextPrice().setVisibility(View.GONE);
            holder.getTextPriceTitle().setVisibility(View.GONE);
            holder.getTextPlaceBidTimes().setVisibility(View.GONE);
            holder.getTextPlaceBidTimesTitle().setVisibility(View.GONE);
            holder.getTextSlash().setVisibility(View.GONE);

        }

        long lastTime = product.getExpired() - System.currentTimeMillis();
        if (holder.countDownTimer != null) {
            holder.countDownTimer.cancel();
        }

        holder.countDownTimer = new CountDownTimer(lastTime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                holder.getTextTime().setText(getRemainingTimeToString(millisUntilFinished));

                if (millisUntilFinished > 299000 && millisUntilFinished < 301000) {

                    mBuilder = new NotificationCompat.Builder(mMainActivity, mMainActivity.getString(R.string.package_name));
                    mBuilder.setContentTitle(mMainActivity.getString(R.string.notification_title_eyes_on))
                            .setContentText(product.getTitle() + mMainActivity.getString(R.string.notification_text_eyes_on))
                            .setTicker(mMainActivity.getString(R.string.notification_ticker))
                            .setSmallIcon(R.drawable.icons_24px_notification);
                    mNotificationManager.notify(i, mBuilder.build());
                }
            }

            @Override
            public void onFinish() {
                holder.getTextTime().setText(mMainActivity.getString(R.string.bid_finish));
            }
        }.start();

        mCountDownMap.put(holder.getTextTime().hashCode(), holder.countDownTimer);
    }

    private String getRemainingTimeToString(long millSeconds) {

        long days = millSeconds / (1000 * 60 * 60 * 24);
        long hours = (millSeconds - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (millSeconds - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (millSeconds - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;

        String time = days + " " + mMainActivity.getString(R.string.timer_day) + " "
                + hours + " " + mMainActivity.getString(R.string.timer_hour) + " "
                + minutes + " " + mMainActivity.getString(R.string.timer_minute) + " "
                + seconds + " " + mMainActivity.getString(R.string.timer_second);

        return time;
    }

    public void updateData(ArrayList<Product> productsList) {
        mProductsList = productsList;
        notifyDataSetChanged();
    }

    public void cancelAllTimers() {
        if (mCountDownMap == null) {
            return;
        }

        for (int i = 0, length = mCountDownMap.size(); i < length; i++) {
            CountDownTimer cdt = mCountDownMap.get(mCountDownMap.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }
}
