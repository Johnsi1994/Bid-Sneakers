package com.johnson.bid.data;

import java.util.ArrayList;

public class Product {


    private long mProductId;
    private ArrayList<String> mImages;
    private String mTitle;
    private String mIntroduction;
    private String mCondition;

    private String mAuctionType;
    private String mAuctionCondition;
    private long mStartTime;
    private long mExpired;
    private int mStartPrice;
    private int mCurrentPrice;
    private int mReservePrice;
    private int mIncrease;
    private int mPlaceBidTimes;

    private long mSellerId;
    private long mHighestUserId;
    private String mSellerName;
    private String mBuyerName;

    private boolean sellerHasRead;
    private boolean buyerHasRead;

    public Product() {

        mAuctionType = "";
        mAuctionCondition = "";
        mSellerId = -1;
        mProductId = -1;
        mImages = new ArrayList<>();
        mTitle = "";
        mIntroduction = "";
        mCondition = "";
        mStartTime = -1;
        mExpired = -1;
        mStartPrice = -1;
        mCurrentPrice = -1;
        mReservePrice = -1;
        mIncrease = -1;
        mHighestUserId = -1;
        mPlaceBidTimes = -1;
        mSellerName = "";
        mBuyerName = "";
        sellerHasRead = false;
        buyerHasRead = false;

    }

    public String getAuctionCondition() {
        return mAuctionCondition;
    }

    public void setAuctionCondition(String auctionCondition) {
        mAuctionCondition = auctionCondition;
    }

    public String getAuctionType() {
        return mAuctionType;
    }

    public void setAuctionType(String auctionType) {
        mAuctionType = auctionType;
    }

    public long getSellerId() {
        return mSellerId;
    }

    public void setSellerId(long sellerId) {
        mSellerId = sellerId;
    }

    public long getProductId() {
        return mProductId;
    }

    public void setProductId(long productId) {
        mProductId = productId;
    }

    public ArrayList<String> getImages() {
        return mImages;
    }

    public void setImages(ArrayList<String> images) {
        mImages = images;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getIntroduction() {
        return mIntroduction;
    }

    public void setIntroduction(String introduction) {
        mIntroduction = introduction;
    }

    public String getCondition() {
        return mCondition;
    }

    public void setCondition(String condition) {
        mCondition = condition;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(long startTime) {
        mStartTime = startTime;
    }

    public long getExpired() {
        return mExpired;
    }

    public void setExpired(long expired) {
        mExpired = expired;
    }

    public int getCurrentPrice() {
        return mCurrentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        mCurrentPrice = currentPrice;
    }

    public int getReservePrice() {
        return mReservePrice;
    }

    public void setReservePrice(int reservePrice) {
        mReservePrice = reservePrice;
    }

    public int getIncrease() {
        return mIncrease;
    }

    public void setIncrease(int increase) {
        mIncrease = increase;
    }

    public long getHighestUserId() {
        return mHighestUserId;
    }

    public void setHighestUserId(long highestUserId) {
        mHighestUserId = highestUserId;
    }

    public int getPlaceBidTimes() {
        return mPlaceBidTimes;
    }

    public void setPlaceBidTimes(int placeBidTimes) {
        mPlaceBidTimes = placeBidTimes;
    }

    public int getStartPrice() {
        return mStartPrice;
    }

    public void setStartPrice(int startPrice) {
        mStartPrice = startPrice;
    }

    public boolean isSellerHasRead() {
        return sellerHasRead;
    }

    public void setSellerHasRead(boolean sellerHasRead) {
        this.sellerHasRead = sellerHasRead;
    }

    public boolean isBuyerHasRead() {
        return buyerHasRead;
    }

    public void setBuyerHasRead(boolean buyerHasRead) {
        this.buyerHasRead = buyerHasRead;
    }

    public String getSellerName() {
        return mSellerName;
    }

    public void setSellerName(String sellerName) {
        mSellerName = sellerName;
    }

    public String getBuyerName() {
        return mBuyerName;
    }

    public void setBuyerName(String buyerName) {
        mBuyerName = buyerName;
    }
}
