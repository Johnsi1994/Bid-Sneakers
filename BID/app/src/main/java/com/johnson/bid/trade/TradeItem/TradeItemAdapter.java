package com.johnson.bid.trade.TradeItem;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnson.bid.MainMvpController;
import com.johnson.bid.R;
import com.johnson.bid.data.Product;
import com.johnson.bid.util.ImageManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.johnson.bid.MainMvpController.ENGLISH;
import static com.johnson.bid.MainMvpController.MYBIDDING;
import static com.johnson.bid.MainMvpController.MYBOUGHT;
import static com.johnson.bid.MainMvpController.MYSELLING;
import static com.johnson.bid.MainMvpController.MYSOLD;
import static com.johnson.bid.MainMvpController.NOBODYBID;
import static com.johnson.bid.MainMvpController.SEALED;

public class TradeItemAdapter extends RecyclerView.Adapter {

    private static final int TYPE_LOADING = 0;
    private static final int TYPE_PRODUCT = 0x01;

    private TradeItemContract.Presenter mPresenter;
    private String mTradeType;
    private ArrayList<Product> mProductsList;
    private SparseArray<CountDownTimer> mCountDownMap;

    public TradeItemAdapter(TradeItemContract.Presenter presenter, @MainMvpController.TradeType String tradeType) {
        mPresenter = presenter;
        mTradeType = tradeType;
        mCountDownMap = new SparseArray<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_PRODUCT) {
            switch (mTradeType) {
                case MYBIDDING:
                    return new BiddingViewHolder(LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.item_bidding_english, viewGroup, false));
                case MYSELLING:
                    return new SellingViewHolder(LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.item_selling_english, viewGroup, false));
                case MYBOUGHT:
                    return new BoughtViewHolder(LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.item_bought, viewGroup, false));
                case MYSOLD:
                    return new SoldViewHolder(LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.item_sold, viewGroup, false));
                case NOBODYBID:
                default:
                    return new NobodyBidViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_nobody_bid, viewGroup, false));
            }
        } else {
            return new LoadingViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_all_loading, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if (mProductsList != null) {
            if (viewHolder instanceof BiddingViewHolder) {

                bindBiddingViewHolder((BiddingViewHolder) viewHolder, mProductsList.get(i));

            } else if (viewHolder instanceof SellingViewHolder) {

                bindSellingViewHolder((SellingViewHolder) viewHolder, mProductsList.get(i));

            } else if (viewHolder instanceof BoughtViewHolder) {

                bindBoughtViewHolder((BoughtViewHolder) viewHolder, mProductsList.get(i));

            } else if (viewHolder instanceof SoldViewHolder) {

                bindSoldViewHolder((SoldViewHolder) viewHolder, mProductsList.get(i));

            } else {

                bindNobodyBidViewHolder((NobodyBidViewHolder) viewHolder, mProductsList.get(i));

            }
        }
    }

    @Override
    public int getItemCount() {
        if (mProductsList == null) {
            return 0;
        } else {
            return mProductsList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_PRODUCT;
    }


    private class BiddingViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout mBiddingLayout;
        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        private TextView mTextPrice;
        private TextView mTextPeople;
        private TextView mTextPriceTitle;
        private TextView mTextSlash;
        private TextView mTextPeopleTitle;
        CountDownTimer countDownTimer;

        private BiddingViewHolder(View itemView) {
            super(itemView);

            mBiddingLayout = itemView.findViewById(R.id.layout_e_auction);
            mImageMain = itemView.findViewById(R.id.image_product_e_auction);
            mTextTitle = itemView.findViewById(R.id.text_product_title_e_auction);
            mTextTime = itemView.findViewById(R.id.text_last_time_e_auction);
            mTextPrice = itemView.findViewById(R.id.text_price_e_auction);
            mTextPeople = itemView.findViewById(R.id.text_participant_num_e_auction);
            mTextPriceTitle = itemView.findViewById(R.id.text_price_title);
            mTextSlash = itemView.findViewById(R.id.text_slash);
            mTextPeopleTitle = itemView.findViewById(R.id.text_people_title);
        }

        private  ConstraintLayout getBiddingLayout() {return mBiddingLayout;}

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

        private TextView getTextPeople() {
            return mTextPeople;
        }

        private TextView getTextPeopleTitle() {
            return mTextPeopleTitle;
        }

        private TextView getTextPriceTitle() {
            return mTextPriceTitle;
        }

        private TextView getTextSlash() {
            return mTextSlash;
        }
    }

    private void bindBiddingViewHolder(BiddingViewHolder holder, Product product) {


        holder.getBiddingLayout().setOnClickListener(v -> {

            if (product.getAuctionType().equals("English")) {
                mPresenter.openBidding(ENGLISH, product);
            } else {
                mPresenter.openBidding(SEALED, product);
            }

            mPresenter.hideToolbarAndBottomNavigation();
        });

        ImageManager.getInstance().setImageByUrl(holder.getImageMain(), product.getImages().get(0));

        holder.getTextTitle().setText(product.getTitle());

        if (product.getAuctionType().equals("English")) {

            holder.getTextPrice().setText(String.valueOf(product.getCurrentPrice()));

            holder.getTextPeople().setText(String.valueOf(product.getParticipantsNumber()));

        } else {

            holder.getTextTitle().setPadding(0, 40, 0, 0);

            holder.getTextPrice().setVisibility(View.GONE);
            holder.getTextPriceTitle().setVisibility(View.GONE);
            holder.getTextPeople().setVisibility(View.GONE);
            holder.getTextPeopleTitle().setVisibility(View.GONE);
            holder.getTextSlash().setVisibility(View.GONE);

        }

        long lastTime = product.getExpired() - System.currentTimeMillis();
        if (holder.countDownTimer != null) {
            holder.countDownTimer.cancel();
        }
        holder.countDownTimer = new CountDownTimer(lastTime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                holder.getTextTime().setText(getLastTimeToString(millisUntilFinished));
            }

            @Override
            public void onFinish() {

            }
        }.start();

        mCountDownMap.put(holder.getTextTime().hashCode(), holder.countDownTimer);

    }

    private class SellingViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout mSellingLayout;
        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        private TextView mTextPrice;
        private TextView mTextPeople;
        private TextView mTextPriceTitle;
        private TextView mTextSlash;
        private TextView mTextPeopleTitle;
        CountDownTimer countDownTimer;

        private SellingViewHolder(View itemView) {
            super(itemView);

            mSellingLayout = itemView.findViewById(R.id.layout_selling);
            mImageMain = itemView.findViewById(R.id.image_selling_e);
            mTextTitle = itemView.findViewById(R.id.text_title_selling_e);
            mTextTime = itemView.findViewById(R.id.text_last_time_selling_e);
            mTextPrice = itemView.findViewById(R.id.text_price_selling_e);
            mTextPeople = itemView.findViewById(R.id.text_people_selling_e);
            mTextPriceTitle = itemView.findViewById(R.id.text_price_title);
            mTextSlash = itemView.findViewById(R.id.text_slash);
            mTextPeopleTitle = itemView.findViewById(R.id.text_people_title);
        }

        private ConstraintLayout getSellingLayout() {return mSellingLayout;}

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

        private TextView getTextPeople() {
            return mTextPeople;
        }

        private TextView getTextPeopleTitle() {
            return mTextPeopleTitle;
        }

        private TextView getTextPriceTitle() {
            return mTextPriceTitle;
        }

        private TextView getTextSlash() {
            return mTextSlash;
        }
    }

    private void bindSellingViewHolder(SellingViewHolder holder, Product product) {

        holder.getSellingLayout().setOnClickListener(v -> {

            if (product.getAuctionType().equals("English")) {
                mPresenter.openSelling(ENGLISH, product);
            } else {
                mPresenter.openSelling(SEALED, product);
            }

            mPresenter.hideToolbarAndBottomNavigation();
        });

        ImageManager.getInstance().setImageByUrl(holder.getImageMain(), product.getImages().get(0));

        holder.getTextTitle().setText(product.getTitle());

        if (product.getAuctionType().equals("English")) {

            holder.getTextPrice().setText(String.valueOf(product.getCurrentPrice()));

            holder.getTextPeople().setText(String.valueOf(product.getParticipantsNumber()));

        } else {

            holder.getTextTitle().setPadding(0, 40, 0, 0);

            holder.getTextPriceTitle().setVisibility(View.GONE);
            holder.getTextPrice().setVisibility(View.GONE);
            holder.getTextSlash().setVisibility(View.GONE);
            holder.getTextPeople().setVisibility(View.GONE);
            holder.getTextPeopleTitle().setVisibility(View.GONE);
        }

        long lastTime = product.getExpired() - System.currentTimeMillis();
        if (holder.countDownTimer != null) {
            holder.countDownTimer.cancel();
        }
        holder.countDownTimer = new CountDownTimer(lastTime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                holder.getTextTime().setText(getLastTimeToString(millisUntilFinished));
            }

            @Override
            public void onFinish() {

            }
        }.start();

        mCountDownMap.put(holder.getTextTime().hashCode(), holder.countDownTimer);
    }

    private class BoughtViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout mBoughtLayout;
        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        private TextView mTextPrice;

        public BoughtViewHolder(View itemView) {
            super(itemView);

            mBoughtLayout = itemView.findViewById(R.id.layout_bought);
            mImageMain = itemView.findViewById(R.id.image_bought);
            mTextTitle = itemView.findViewById(R.id.text_title_bought);
            mTextTime = itemView.findViewById(R.id.text_time_bought);
            mTextPrice = itemView.findViewById(R.id.text_price_bought);
        }

        public ConstraintLayout getBoughtLayout() {
            return mBoughtLayout;
        }

        public ImageView getImageMain() {
            return mImageMain;
        }

        public TextView getTextTitle() {
            return mTextTitle;
        }

        public TextView getTextTime() {
            return mTextTime;
        }

        public TextView getTextPrice() {
            return mTextPrice;
        }
    }

    private void bindBoughtViewHolder(BoughtViewHolder holder, Product product) {

        holder.getBoughtLayout().setOnClickListener(v -> {
            mPresenter.openBoughtDetail(product);
            mPresenter.hideToolbarAndBottomNavigation();
        });

        ImageManager.getInstance().setImageByUrl(holder.getImageMain(), product.getImages().get(0));

        holder.getTextTitle().setText(product.getTitle());

        holder.getTextPrice().setText(String.valueOf(product.getCurrentPrice()));

        holder.getTextTime().setText(getDateToString(product.getExpired()));

    }

    private class SoldViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        private TextView mTextPrice;

        public SoldViewHolder(View itemView) {
            super(itemView);

            mImageMain = itemView.findViewById(R.id.image_sold);
            mTextTitle = itemView.findViewById(R.id.text_title_sold);
            mTextTime = itemView.findViewById(R.id.text_time_sold);
            mTextPrice = itemView.findViewById(R.id.text_price_sold);
        }

        public ImageView getImageMain() {
            return mImageMain;
        }

        public TextView getTextTitle() {
            return mTextTitle;
        }

        public TextView getTextTime() {
            return mTextTime;
        }

        public TextView getTextPrice() {
            return mTextPrice;
        }
    }

    private void bindSoldViewHolder(SoldViewHolder holder, Product product) {

//        holder.getLayoutEnglishAuction().setOnClickListener(v -> {
//            mPresenter.openBidding(ENGLISH, product);
//            mPresenter.hideToolbarAndBottomNavigation();
//        });

        ImageManager.getInstance().setImageByUrl(holder.getImageMain(), product.getImages().get(0));

        holder.getTextTitle().setText(product.getTitle());

        holder.getTextPrice().setText(String.valueOf(product.getCurrentPrice()));

        holder.getTextTime().setText(getDateToString(product.getExpired()));

    }

    private class NobodyBidViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;

        public NobodyBidViewHolder(View itemView) {
            super(itemView);

            mImageMain = itemView.findViewById(R.id.image_nobody_bit);
            mTextTitle = itemView.findViewById(R.id.text_product_title_nobody_bid);
            mTextTime = itemView.findViewById(R.id.text_time_nobody_bid);
        }

        public ImageView getImageMain() {
            return mImageMain;
        }

        public TextView getTextTitle() {
            return mTextTitle;
        }

        public TextView getTextTime() {
            return mTextTime;
        }
    }

    private void bindNobodyBidViewHolder(NobodyBidViewHolder holder, Product product) {

//        holder.getLayoutEnglishAuction().setOnClickListener(v -> {
//            mPresenter.openBidding(ENGLISH, product);
//            mPresenter.hideToolbarAndBottomNavigation();
//        });

        ImageManager.getInstance().setImageByUrl(holder.getImageMain(), product.getImages().get(0));

        holder.getTextTitle().setText(product.getTitle());

        holder.getTextTime().setText(getDateToString(product.getExpired()));

    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    private String getLastTimeToString(long millSeconds) {

        long days = millSeconds / (1000 * 60 * 60 * 24);
        long hours = (millSeconds - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (millSeconds - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (millSeconds - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;

        String time = days + " 天 " + hours + " 時 " + minutes + " 分 " + seconds + " 秒";
        return time;
    }

    private String getDateToString(long millSeconds) {
        SimpleDateFormat sf = new SimpleDateFormat("MM 月 dd 日 HH 時 mm 分");
        Date d = new Date(millSeconds);
        return sf.format(d);
    }

    public void updateData(ArrayList<Product> productsList) {
        mProductsList = productsList;
        notifyDataSetChanged();
    }

    public void cancelAllTimers() {
        if (mCountDownMap == null) {
            return;
        }

        for (int i = 0,length = mCountDownMap.size(); i < length; i++) {
            CountDownTimer cdt = mCountDownMap.get(mCountDownMap.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }
}
