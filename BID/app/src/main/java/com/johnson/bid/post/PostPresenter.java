package com.johnson.bid.post;

import android.support.annotation.NonNull;

import com.johnson.bid.data.Product;

import static com.google.common.base.Preconditions.checkNotNull;

public class PostPresenter implements PostContract.Presenter {

    private PostContract.View mPostView;

    private Product mProduct;

    public PostPresenter(@NonNull PostContract.View postView) {
        mPostView = checkNotNull(postView, "postView cannot be null!");
        mProduct = new Product();
    }

    @Override
    public void start() {

    }

    @Override
    public void setProductTitle(String productTitle) {
        mProduct.setTitle(productTitle);
    }

    @Override
    public void setProductIntro(String productIntro) {
        mProduct.setIntroduction(productIntro);
    }

    @Override
    public void setStartingPrice(int startingPrice) {
        mProduct.setStartPrice(startingPrice);
    }

    @Override
    public void setReservePrice(int reservePrice) {
        mProduct.setReservePrice(reservePrice);
    }

    @Override
    public void setProductCondition(String condition) {
        mProduct.setCondition(condition);
    }

    @Override
    public void setAuctionType(String auctionType) {
        mProduct.setAuctionType(auctionType);
    }

    @Override
    public void setIncrease(int increase) {
        mProduct.setIncrease(increase);
    }

    @Override
    public void setExpireTime(long expireTime) {
        mProduct.setExpired(expireTime);
    }

    @Override
    public void setProductId(long productId) {
        mProduct.setProductId(productId);
    }

    @Override
    public void setStartingTime(long startingTime) {
        mProduct.setStartTime(startingTime);
    }

    @Override
    public void showBottomNavigation() {

    }

    @Override
    public void updateToolbar(String title) {

    }
}
