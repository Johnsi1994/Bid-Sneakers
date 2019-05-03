package com.johnson.bid.sold;

import android.support.annotation.NonNull;

import com.johnson.bid.data.ChatRoom;
import com.johnson.bid.data.Product;

import static com.google.common.base.Preconditions.checkNotNull;

public class SoldDetailPresenter implements SoldDetailContract.Presenter {

    private SoldDetailContract.View mSoldView;
    private Product mProduct;

    public SoldDetailPresenter(@NonNull SoldDetailContract.View soldView) {
        mSoldView = checkNotNull(soldView, "soldView cannot be null!");
    }

    @Override
    public void start() {

    }

    @Override
    public void setSoldDetailData(Product product) {
        mProduct = product;
    }

    @Override
    public void loadSoldDetailData() {
        mSoldView.showSoldDetailUi(mProduct);
    }

    @Override
    public void showToolbarAndBottomNavigation() {

    }

    @Override
    public void openChatContent(ChatRoom chatRoom, String from) {

    }

    @Override
    public void hideBottomNavigation() {

    }

    @Override
    public void updateToolbar(String name) {

    }

    @Override
    public void showToolbar() {

    }
}
