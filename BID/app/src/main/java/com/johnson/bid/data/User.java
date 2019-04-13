package com.johnson.bid.data;

import java.util.ArrayList;

public class User {

    private long mId;
    private String mName;
    private String mImage;
    private ArrayList<Long> mMyTradeProductsId;

//    private ArrayList<Product> mEyesOn;
//    private ArrayList<Product> mChatRoomId;

    public User() {

        mId = -1;
        mName = "";
        mImage = "";
        mMyTradeProductsId = new ArrayList<>();
//        mEyesOn = new ArrayList<>();
//        mChatRoomId = new ArrayList<>();

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

    public ArrayList<Long> getMyTradeProductsId() {
        return mMyTradeProductsId;
    }

    public void setMyTradeProductsId(ArrayList<Long> myTradeProductsId) {
        mMyTradeProductsId = myTradeProductsId;
    }

    //    public ArrayList<Product> getEyesOn() {
//        return mEyesOn;
//    }
//
//    public void setEyesOn(ArrayList<Product> eyesOn) {
//        mEyesOn = eyesOn;
//    }
//
//    public ArrayList<Product> getChatRoomId() {
//        return mChatRoomId;
//    }
//
//    public void setChatRoomId(ArrayList<Product> chatRoomId) {
//        mChatRoomId = chatRoomId;
//    }
}
