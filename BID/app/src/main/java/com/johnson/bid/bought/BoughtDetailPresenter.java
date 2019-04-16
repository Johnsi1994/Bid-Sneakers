package com.johnson.bid.bought;

import android.support.annotation.NonNull;

import com.johnson.bid.data.Product;

import static com.google.common.base.Preconditions.checkNotNull;

public class BoughtDetailPresenter implements BoughtDetailContract.Presenter{

    private BoughtDetailContract.View mBoughtView;
    private Product mProduct;

    public BoughtDetailPresenter(@NonNull BoughtDetailContract.View boughtView) {
        mBoughtView = checkNotNull(boughtView, "boughtView cannot be null!");
    }

    @Override
    public void start() {

    }

    @Override
    public void showToolbarAndBottomNavigation() {

    }

    @Override
    public void loadBoughtDetailData() {

        mBoughtView.showBoughtDetailUi(mProduct);

    }

    @Override
    public void setBoughtDetailData(Product product) {
        mProduct = product;
    }
}
