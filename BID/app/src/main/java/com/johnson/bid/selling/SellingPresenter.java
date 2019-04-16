package com.johnson.bid.selling;

import android.support.annotation.NonNull;
import android.util.Log;

import com.johnson.bid.centre.CenterContract;
import com.johnson.bid.data.Product;

import static com.google.common.base.Preconditions.checkNotNull;

public class SellingPresenter implements SellingContract.Presenter {

    private SellingContract.View mSellingView;
    private Product mProduct;

    public SellingPresenter(@NonNull SellingContract.View sellingView) {
        mSellingView = checkNotNull(sellingView, "sellingView cannot be null!");
    }

    @Override
    public void start() {

    }

    @Override
    public void showToolbarAndBottomNavigation() {

    }

    @Override
    public void setSellingDetailData(Product product) {
        mProduct = product;
    }

    @Override
    public void loadSellingDetailData() {
        mSellingView.showSellingDetailUi(mProduct);
    }
}
