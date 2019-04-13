package com.johnson.bid.centre.auction;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.johnson.bid.MainMvpController.ENGLISH;
import static com.johnson.bid.MainMvpController.SEALED;

public class AuctionAdapter extends RecyclerView.Adapter {

    private static final int TYPE_LOADING = 0;
    private static final int TYPE_PRODUCT = 0x01;

    private AuctionContract.Presenter mPresenter;
    private String mAuctionType;
    private ArrayList<Product> mProductList;

    public AuctionAdapter(AuctionContract.Presenter presenter, @MainMvpController.AuctionType String auctionType) {
        mPresenter = presenter;
        mAuctionType = auctionType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_PRODUCT) {
            if (mAuctionType.equals(ENGLISH)) {
                return new EnglishAuctionViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_english_auction, viewGroup, false));
            } else {
                return new SealedAuctionViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_sealed_auction, viewGroup, false));
            }
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
        return (position < mProductList.size()) ? TYPE_PRODUCT : TYPE_LOADING;
    }

    private class EnglishAuctionViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout mLayoutEnglishAuction;
        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;
        private TextView mTextPrice;
        private TextView mTextPeople;

        private EnglishAuctionViewHolder(View itemView) {
            super(itemView);


            mLayoutEnglishAuction = itemView.findViewById(R.id.layout_e_auction);
            mImageMain = itemView.findViewById(R.id.image_product_e_auction);
            mTextTitle = itemView.findViewById(R.id.text_product_title_e_auction);
            mTextTime = itemView.findViewById(R.id.text_last_time_e_auction);
            mTextPrice = itemView.findViewById(R.id.text_price_e_auction);
            mTextPeople = itemView.findViewById(R.id.text_participant_num_e_auction);
        }

        private ConstraintLayout getLayoutEnglishAuction() {
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

        private TextView getTextPeople() {
            return mTextPeople;
        }
    }

    private void bindEnglishAuctionViewHolder(EnglishAuctionViewHolder holder, Product product) {

        holder.getLayoutEnglishAuction().setOnClickListener(v -> {
            mPresenter.openBidding(ENGLISH, product);
            mPresenter.hideToolbarAndBottomNavigation();
        });

        ImageManager.getInstance().setImageByUrl(holder.getImageMain(), product.getImages().get(0));

        holder.getTextTitle().setText(product.getTitle());

        holder.getTextTime().setText(getDateToString(product.getStartTime()));

        holder.getTextPrice().setText(String.valueOf(product.getCurrentPrice()));

        holder.getTextPeople().setText(String.valueOf(product.getParticipantsNumber()));

    }

    private class SealedAuctionViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout mLayoutSealedAuction;
        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextTime;

        private SealedAuctionViewHolder(View itemView) {
            super(itemView);

            mLayoutSealedAuction = itemView.findViewById(R.id.layout_s_auction);
            mImageMain = itemView.findViewById(R.id.image_product_s_auction);
            mTextTitle = itemView.findViewById(R.id.text_product_title_s_auction);
            mTextTime = itemView.findViewById(R.id.text_last_time_s_auction);

        }

        private ConstraintLayout getLayoutSealedAuction() {
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
            mPresenter.openBidding(SEALED, product);
            mPresenter.hideToolbarAndBottomNavigation();
        });

        ImageManager.getInstance().setImageByUrl(holder.getImageMain(), product.getImages().get(0));

        holder.getTextTitle().setText(product.getTitle());

        holder.getTextTime().setText(getDateToString(product.getStartTime()));

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
        SimpleDateFormat sf = new SimpleDateFormat("MM月 dd日 HH時 mm分");
        Date d = new Date(millSeconds);
        String time = sf.format(d);
        return time;
    }
}
