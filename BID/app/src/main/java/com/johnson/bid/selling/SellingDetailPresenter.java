package com.johnson.bid.selling;

import android.support.annotation.NonNull;

import com.johnson.bid.data.Product;

import static com.google.common.base.Preconditions.checkNotNull;

public class SellingDetailPresenter implements SellingDetailContract.Presenter {

    private SellingDetailContract.View mSellingView;
    private Product mProduct;

    public SellingDetailPresenter(@NonNull SellingDetailContract.View sellingView) {
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

    @Override
    public void openDeleteProductDialog(Product product) {

    }
}
