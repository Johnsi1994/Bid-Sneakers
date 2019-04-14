package com.johnson.bid.trade.TradeItem;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnson.bid.MainMvpController;
import com.johnson.bid.R;
import com.johnson.bid.data.Product;
import com.johnson.bid.util.ImageManager;

import java.util.ArrayList;

import static com.johnson.bid.MainMvpController.MYBIDDING;
import static com.johnson.bid.MainMvpController.MYBOUGHT;
import static com.johnson.bid.MainMvpController.MYSELLING;
import static com.johnson.bid.MainMvpController.MYSOLD;

public class TradeItemAdapter extends RecyclerView.Adapter {

    private static final int TYPE_LOADING = 0;
    private static final int TYPE_PRODUCT = 0x01;

    private TradeItemContract.Presenter mPresenter;
    private String mTradeType;
    private ArrayList<Product> mProductsList;

    public TradeItemAdapter(TradeItemContract.Presenter presenter, @MainMvpController.TradeType String tradeType) {
        mPresenter = presenter;
        mTradeType = tradeType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_PRODUCT) {
            switch (mTradeType) {
                case MYBIDDING:
                    return new BiddingEnglishViewHolder(LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.item_bidding_english, viewGroup, false));

//                return new BiddingSealedViewHolder(LayoutInflater.from(viewGroup.getContext())
//                        .inflate(R.layout.item_bidding_sealed, viewGroup, false));
                case MYSELLING:
                    return new SellingEnglishViewHolder(LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.item_selling_english, viewGroup, false));

//                return new SellingSealedViewHolder(LayoutInflater.from(viewGroup.getContext())
//                        .inflate(R.layout.item_selling_sealed, viewGroup, false));
                case MYBOUGHT:
                    return new BoughtViewHolder(LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.item_bought, viewGroup, false));
                case MYSOLD:
                default:
                    return new SoldViewHolder(LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.item_sold, viewGroup, false));
            }
        } else {
            return new LoadingViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_all_loading, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if (mProductsList != null) {
            if (viewHolder instanceof BiddingEnglishViewHolder) {

                bindBiddingEnglishViewHolder((BiddingEnglishViewHolder) viewHolder, mProductsList.get(i));

            } else if (viewHolder instanceof BiddingSealedViewHolder) {

                bindBiddingSealedViewHolder((BiddingSealedViewHolder) viewHolder, mProductsList.get(i));

            } else if (viewHolder instanceof SellingEnglishViewHolder) {

                bindSellingEnglishViewHolder((SellingEnglishViewHolder) viewHolder, mProductsList.get(i));

            } else if (viewHolder instanceof SellingSealedViewHolder) {

                bindSellingSealedViewHolder((SellingSealedViewHolder) viewHolder, mProductsList.get(i));

            } else if (viewHolder instanceof BoughtViewHolder) {

                bindBoughtViewHolder((BoughtViewHolder) viewHolder, mProductsList.get(i));

            } else {

                bindSoldViewHolder((SoldViewHolder) viewHolder, mProductsList.get(i));

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


    private class BiddingEnglishViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        private TextView mTextPrice;
        private TextView mTextPeople;

        public BiddingEnglishViewHolder(View itemView) {
            super(itemView);

            mImageMain = itemView.findViewById(R.id.image_product_e_auction);
            mTextTitle = itemView.findViewById(R.id.text_product_title_e_auction);
            mTextTime = itemView.findViewById(R.id.text_last_time_e_auction);
            mTextPrice = itemView.findViewById(R.id.text_price_e_auction);
            mTextPeople = itemView.findViewById(R.id.text_participant_num_e_auction);
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

        public TextView getTextPeople() {
            return mTextPeople;
        }
    }

    private void bindBiddingEnglishViewHolder(BiddingEnglishViewHolder holder, Product product) {

//        holder.getLayoutEnglishAuction().setOnClickListener(v -> {
//            mPresenter.openBidding(ENGLISH, product);
//            mPresenter.hideToolbarAndBottomNavigation();
//        });

        ImageManager.getInstance().setImageByUrl(holder.getImageMain(), product.getImages().get(0));

        holder.getTextTitle().setText(product.getTitle());

        timer(holder, product);

        holder.getTextPrice().setText(String.valueOf(product.getCurrentPrice()));

        holder.getTextPeople().setText(String.valueOf(product.getParticipantsNumber()));

    }

    private class BiddingSealedViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;

        public BiddingSealedViewHolder(View itemView) {
            super(itemView);

            mImageMain = itemView.findViewById(R.id.image_product_s_auction);
            mTextTitle = itemView.findViewById(R.id.text_product_title_s_auction);
            mTextTime = itemView.findViewById(R.id.text_last_time_s_auction);
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

    private void bindBiddingSealedViewHolder(BiddingSealedViewHolder holder, Product product) {

//        holder.getLayoutEnglishAuction().setOnClickListener(v -> {
//            mPresenter.openBidding(ENGLISH, product);
//            mPresenter.hideToolbarAndBottomNavigation();
//        });

        ImageManager.getInstance().setImageByUrl(holder.getImageMain(), product.getImages().get(0));

        holder.getTextTitle().setText(product.getTitle());

        timer(holder, product);

    }

    private class SellingEnglishViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        private TextView mTextPrice;
        private TextView mTextBuyer;
        private TextView mTextPeople;

        public SellingEnglishViewHolder(View itemView) {
            super(itemView);

            mImageMain = itemView.findViewById(R.id.image_selling_e);
            mTextTitle = itemView.findViewById(R.id.text_title_selling_e);
            mTextTime = itemView.findViewById(R.id.text_last_time_selling_e);
            mTextPrice = itemView.findViewById(R.id.text_price_selling_e);
            mTextBuyer = itemView.findViewById(R.id.text_buyer_selling_e);
            mTextPeople = itemView.findViewById(R.id.text_people_selling_e);
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

        public TextView getTextBuyer() {
            return mTextBuyer;
        }

        public TextView getTextPeople() {
            return mTextPeople;
        }
    }

    private void bindSellingEnglishViewHolder(SellingEnglishViewHolder holder, Product product) {

//        holder.getLayoutEnglishAuction().setOnClickListener(v -> {
//            mPresenter.openBidding(ENGLISH, product);
//            mPresenter.hideToolbarAndBottomNavigation();
//        });

        ImageManager.getInstance().setImageByUrl(holder.getImageMain(), product.getImages().get(0));

        holder.getTextTitle().setText(product.getTitle());

        timer(holder, product);

        holder.getTextPrice().setText(String.valueOf(product.getCurrentPrice()));

        holder.getTextPeople().setText(String.valueOf(product.getParticipantsNumber()));

        holder.getTextBuyer().setText(String.valueOf(product.getHighestUserId()));

    }

    private class SellingSealedViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;

        public SellingSealedViewHolder(View itemView) {
            super(itemView);

            mImageMain = itemView.findViewById(R.id.image_selling_s);
            mTextTitle = itemView.findViewById(R.id.text_title_selling_s);
            mTextTime = itemView.findViewById(R.id.text_last_time_selling_s);
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

    private void bindSellingSealedViewHolder(SellingSealedViewHolder holder, Product product) {

//        holder.getLayoutEnglishAuction().setOnClickListener(v -> {
//            mPresenter.openBidding(ENGLISH, product);
//            mPresenter.hideToolbarAndBottomNavigation();
//        });

        ImageManager.getInstance().setImageByUrl(holder.getImageMain(), product.getImages().get(0));

        holder.getTextTitle().setText(product.getTitle());

        timer(holder, product);

    }

    private class BoughtViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        private TextView mTextPrice;
        private TextView mTextSeller;

        public BoughtViewHolder(View itemView) {
            super(itemView);

            mImageMain = itemView.findViewById(R.id.image_bought);
            mTextTitle = itemView.findViewById(R.id.text_title_bought);
            mTextTime = itemView.findViewById(R.id.text_time_bought);
            mTextPrice = itemView.findViewById(R.id.text_price_bought);
            mTextSeller = itemView.findViewById(R.id.text_seller_bought);
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

        public TextView getTextPeople() {
            return mTextSeller;
        }
    }

    private void bindBoughtViewHolder(BoughtViewHolder holder, Product product) {

//        holder.getLayoutEnglishAuction().setOnClickListener(v -> {
//            mPresenter.openBidding(ENGLISH, product);
//            mPresenter.hideToolbarAndBottomNavigation();
//        });

        ImageManager.getInstance().setImageByUrl(holder.getImageMain(), product.getImages().get(0));

        holder.getTextTitle().setText(product.getTitle());

        timer(holder, product);

        holder.getTextPrice().setText(String.valueOf(product.getCurrentPrice()));

        holder.getTextPeople().setText(String.valueOf(product.getParticipantsNumber()));

    }

    private class SoldViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        private TextView mTextPrice;
        private TextView mTextBuyer;

        public SoldViewHolder(View itemView) {
            super(itemView);

            mImageMain = itemView.findViewById(R.id.image_sold);
            mTextTitle = itemView.findViewById(R.id.text_title_sold);
            mTextTime = itemView.findViewById(R.id.text_time_sold);
            mTextPrice = itemView.findViewById(R.id.text_price_sold);
            mTextBuyer = itemView.findViewById(R.id.text_buyer_sold);
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

        public TextView getTextBuyer() {
            return mTextBuyer;
        }
    }

    private void bindSoldViewHolder(SoldViewHolder holder, Product product) {

//        holder.getLayoutEnglishAuction().setOnClickListener(v -> {
//            mPresenter.openBidding(ENGLISH, product);
//            mPresenter.hideToolbarAndBottomNavigation();
//        });

        ImageManager.getInstance().setImageByUrl(holder.getImageMain(), product.getImages().get(0));

        holder.getTextTitle().setText(product.getTitle());

        timer(holder, product);

        holder.getTextPrice().setText(String.valueOf(product.getCurrentPrice()));

        holder.getTextBuyer().setText(String.valueOf(product.getHighestUserId()));

    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    private String getDateToString(long millSeconds) {

        long days = millSeconds / (1000 * 60 * 60 * 24);
        long hours = (millSeconds - days * (1000 * 60 * 60 * 24)) / (1000* 60 * 60);
        long minutes = (millSeconds - days * (1000 * 60 * 60 * 24) - hours * (1000* 60 * 60)) / (1000 * 60);
        long seconds = (millSeconds - days * (1000 * 60 * 60 * 24) - hours * (1000* 60 * 60) - minutes * (1000 * 60)) / 1000;

        String time = days + "天 " + hours + "時 " + minutes + "分 " + seconds + "秒";
        return time;
    }

    private void timer(@NonNull RecyclerView.ViewHolder holder, Product product) {
        long lastTime = product.getExpired() - System.currentTimeMillis();
        TextView textView;

        if (holder instanceof BiddingEnglishViewHolder) {

            textView = ((BiddingEnglishViewHolder) holder).getTextTime();

        } else if (holder instanceof BiddingSealedViewHolder) {

            textView = ((BiddingSealedViewHolder) holder).getTextTime();

        } else if (holder instanceof SellingEnglishViewHolder) {

            textView = ((SellingEnglishViewHolder) holder).getTextTime();

        } else if (holder instanceof SellingSealedViewHolder) {

            textView = ((SellingSealedViewHolder) holder).getTextTime();

        } else if (holder instanceof BoughtViewHolder) {

            textView = ((BoughtViewHolder) holder).getTextTime();

        } else {

            textView = ((SoldViewHolder) holder).getTextTime();
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

    public void updateData(ArrayList<Product> productsList) {
        mProductsList = productsList;
        notifyDataSetChanged();
    }
}
