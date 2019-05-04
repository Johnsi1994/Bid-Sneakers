package com.johnson.bid.post;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.johnson.bid.data.Product;
import com.johnson.bid.util.UserManager;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class PostPresenter implements PostContract.Presenter {

    private PostContract.View mPostView;

    private Product mProduct;
    private ArrayList<Bitmap> mImageBitmap;

    public PostPresenter(@NonNull PostContract.View postView) {
        mPostView = checkNotNull(postView, "postView cannot be null!");
        mProduct = new Product();
    }

    @Override
    public void start() {

    }

    @Override
    public Product getProduct() {

        return mProduct;
    }

    @Override
    public void setProductTitle(String productTitle) {
        mProduct.setTitle(productTitle);
    }

    @Override
    public void setAuctionCondition(String auctionCondition) {
        mProduct.setAuctionCondition(auctionCondition);
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
    public void setPostProductId2User(long productId) {
        UserManager.getInstance().addSellingProductId(productId);
        UserManager.getInstance().updateUser2Firebase();
//        UserManager.getInstance().setHasUserDataChange(true);
    }

    @Override
    public void setStartingTime(long startingTime) {
        mProduct.setStartTime(startingTime);
    }

    @Override
    public void setImages(ArrayList<String> Url) {
        mProduct.setImages(Url);
    }

    @Override
    public void setSellerId(long sellerId) {
        mProduct.setSellerId(sellerId);
    }

    @Override
    public void setPlaceBidTimes(int participantsNumber) {
        mProduct.setPlaceBidTimes(participantsNumber);
    }

    @Override
    public void setCurrentPrice(int currentPrice) {
        mProduct.setCurrentPrice(currentPrice);
    }

    @Override
    public void showBottomNavigation() {

    }

    @Override
    public void updateToolbar(String title) {

    }

    @Override
    public void setPostPics(ArrayList<Bitmap> imageBitmap) {
        mImageBitmap = imageBitmap;
        loadPostPics();
    }

    @Override
    public void loadPostPics() {
        if (mImageBitmap != null) {
            mPostView.showPostUi(mImageBitmap);
        }
    }

    @Override
    public void openGalleryDialog(String from) {

    }

    @Override
    public void showPostSuccessDialog() {

    }

    @Override
    public void updateCenterData() {

    }

    @Override
    public void setSellerHasRead(boolean isRead) {
        mProduct.setSellerHasRead(isRead);
    }

    @Override
    public void setBuyerHasRead(boolean isRead) {
        mProduct.setBuyerHasRead(isRead);
    }

    @Override
    public void setSellerName(String name) {
        mProduct.setSellerName(name);
    }


}
