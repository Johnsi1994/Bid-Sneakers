package com.johnson.bid.centre.auction;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class AuctionPresenter implements AuctionContract.Presenter {

    private AuctionContract.View mAuctionView;

    public AuctionPresenter(@NonNull AuctionContract.View auctionView) {
        mAuctionView = checkNotNull(auctionView, "centerView cannot be null!");
    }

    @Override
    public void start() {

    }
}
