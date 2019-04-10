package com.johnson.bid.bidding;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class BiddingPresenter implements BiddingContract.Presenter {

    private final BiddingContract.View mBiddingView;

    public BiddingPresenter(@NonNull BiddingContract.View biddingView) {
        mBiddingView = checkNotNull(biddingView, "biddingView cannot be null!");
    }

    @Override
    public void start() {

    }
}
