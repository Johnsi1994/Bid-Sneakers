package com.johnson.bid.bidding;

import android.support.annotation.NonNull;

import com.johnson.bid.data.Product;

import static com.google.common.base.Preconditions.checkNotNull;

public class BiddingPresenter implements BiddingContract.Presenter {

    private final BiddingContract.View mBiddingView;
    private Product mProduct;

    public BiddingPresenter(@NonNull BiddingContract.View biddingView) {
        mBiddingView = checkNotNull(biddingView, "biddingView cannot be null!");
    }

    @Override
    public void start() {

    }

    @Override
    public void openCenter() {

    }

    @Override
    public void showToolbarAndBottomNavigation() {

    }

    @Override
    public void setProductData(Product product) {
        mProduct = product;
        loadProductData();
    }

    @Override
    public void loadProductData() {
        mBiddingView.showBiddingUi(mProduct);
    }

    @Override
    public void openBidDialog(String from, Product product) {

    }

}
