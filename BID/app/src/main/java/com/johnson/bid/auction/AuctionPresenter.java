package com.johnson.bid.auction;

import android.support.annotation.NonNull;

import com.johnson.bid.MainActivity;
import com.johnson.bid.auction.auctionitem.AuctionItemFragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class AuctionPresenter implements AuctionContract.Presenter {

    private final AuctionContract.View mCenterView;
    private MainActivity mMainActivity;

    public AuctionPresenter(@NonNull AuctionContract.View centerView, MainActivity mainActivity) {
        mCenterView = checkNotNull(centerView, "centerView cannot be null!");
        mMainActivity = mainActivity;
    }

    @Override
    public void start() {}

    @Override
    public AuctionItemFragment findEnglishAuction() {
        return null;
    }

    @Override
    public AuctionItemFragment findSealedAuction() {
        return null;
    }

    @Override
    public void openGalleryDialog(String from) {}

    @Override
    public void openEyesOn(String toolbarTitle) {}

    @Override
    public void openSearchDialog() {}

    @Override
    public void hideBottomNavigation() {}

}
