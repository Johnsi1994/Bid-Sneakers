package com.johnson.bid.bidding;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.johnson.bid.R;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton;

public class BiddingAdapter extends RecyclerView.Adapter {

    private BiddingContract.Presenter mPresenter;

    public BiddingAdapter(BiddingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_bid_page, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

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
        private FloatingActionButton mBidFAB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mBackBtn = itemView.findViewById(R.id.button_bidding_back);
            mGalleryRecycler = itemView.findViewById(R.id.recycler_bidding_gallery);
            mTitleText = itemView.findViewById(R.id.text_bidding_title);
            mEyesOnBtn = itemView.findViewById(R.id.button_bidding_eyes_on);
            mIntroText = itemView.findViewById(R.id.text_bidding_intro);
            mConditionText = itemView.findViewById(R.id.text_bidding_condition);
            mLastTimeText = itemView.findViewById(R.id.text_bidding_last_time);
            mPriceText = itemView.findViewById(R.id.text_bidding_price);
            mIncreaseText = itemView.findViewById(R.id.text_bidding_increase);
            mBuyerText = itemView.findViewById(R.id.text_bidding_buyer);
            mPeopleText = itemView.findViewById(R.id.text_bidding_people);
            mSellerText = itemView.findViewById(R.id.text_bidding_seller);
            mBidFAB = itemView.findViewById(R.id.custom_fab_bidding);
        }
    }
}
