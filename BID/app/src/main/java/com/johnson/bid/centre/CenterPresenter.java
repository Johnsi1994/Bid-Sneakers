package com.johnson.bid.centre;

import android.support.annotation.NonNull;

import com.johnson.bid.centre.auction.AuctionFragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class CenterPresenter implements CenterContract.Presenter {

    private final CenterContract.View mCenterView;

    public CenterPresenter(@NonNull CenterContract.View centerView) {
        mCenterView = checkNotNull(centerView, "centerView cannot be null!");
    }

    @Override
    public void start() {

    }

    @Override
    public AuctionFragment findEnglishAuction() {
        return null;
    }

    @Override
    public AuctionFragment findSealedAuction() {
        return null;
    }

    @Override
    public void openPost() {

    }

}
