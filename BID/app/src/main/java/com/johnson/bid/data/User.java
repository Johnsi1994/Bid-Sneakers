package com.johnson.bid.data;

import java.util.ArrayList;

public class User {

    private long mId;
    private String mName;
    private String mImage;
    private ArrayList<Product> mSelling;
    private ArrayList<Product> mBidding;
    private ArrayList<Product> mSold;
    private ArrayList<Product> mBought;
    private ArrayList<Product> mEyesOn;
    private ArrayList<Product> mChatRoomId;

    public User() {

        mId = -1;
        mName = "";
        mImage = "";
        mSelling = new ArrayList<>();
        mBidding = new ArrayList<>();
        mSold = new ArrayList<>();
        mBought = new ArrayList<>();
        mEyesOn = new ArrayList<>();
        mChatRoomId = new ArrayList<>();

    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public ArrayList<Product> getSelling() {
        return mSelling;
    }

    public void setSelling(ArrayList<Product> selling) {
        mSelling = selling;
    }

    public ArrayList<Product> getBidding() {
        return mBidding;
    }

    public void setBidding(ArrayList<Product> bidding) {
        mBidding = bidding;
    }

    public ArrayList<Product> getSold() {
        return mSold;
    }

    public void setSold(ArrayList<Product> sold) {
        mSold = sold;
    }

    public ArrayList<Product> getBought() {
        return mBought;
    }

    public void setBought(ArrayList<Product> bought) {
        mBought = bought;
    }

    public ArrayList<Product> getEyesOn() {
        return mEyesOn;
    }

    public void setEyesOn(ArrayList<Product> eyesOn) {
        mEyesOn = eyesOn;
    }

    public ArrayList<Product> getChatRoomId() {
        return mChatRoomId;
    }

    public void setChatRoomId(ArrayList<Product> chatRoomId) {
        mChatRoomId = chatRoomId;
    }
}
