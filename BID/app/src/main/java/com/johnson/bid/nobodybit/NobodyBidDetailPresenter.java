package com.johnson.bid.nobodybit;

import android.support.annotation.NonNull;

import com.johnson.bid.data.Product;

import static com.google.common.base.Preconditions.checkNotNull;

public class NobodyBidDetailPresenter implements NobodyBidDetailContract.Presenter {

    private NobodyBidDetailContract.View mNobodyBitView;
    private Product mProduct;

    public NobodyBidDetailPresenter(@NonNull NobodyBidDetailContract.View nobodyBitView) {
        mNobodyBitView = checkNotNull(nobodyBitView, "nobodyBitView cannot be null!");
    }

    @Override
    public void start() {}

    @Override
    public void showToolbarAndBottomNavigation() {}

    @Override
    public void setNobodyBitDetailData(Product product) {
        mProduct = product;
    }

    @Override
    public void loadNobodyBidDetailData() {
        mNobodyBitView.showNobodyBidDetailUi(mProduct);
    }
}
