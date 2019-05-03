package com.johnson.bid.data;

import java.util.ArrayList;

public class ChatRoom {

    private long mChatRoomId;
    private long mBuyerId;
    private long mSellerId;
    private String mBuyerName;
    private String mSellerName;
    private String mBuyerImage;
    private String mSellerImage;
    private ArrayList<ChatContent> mChatContentArrayList;
    

    public ChatRoom() {

        mChatRoomId = -1;
        mBuyerId = -1;
        mSellerId = -1;
        mBuyerName = "";
        mSellerName = "";
        mBuyerImage = "";
        mSellerImage = "";
        mChatContentArrayList = new ArrayList<>();
    }

    public long getChatRoomId() {
        return mChatRoomId;
    }

    public void setChatRoomId(long chatRoomId) {
        mChatRoomId = chatRoomId;
    }

    public long getBuyerId() {
        return mBuyerId;
    }

    public void setBuyerId(long buyerId) {
        mBuyerId = buyerId;
    }

    public long getSellerId() {
        return mSellerId;
    }

    public void setSellerId(long sellerId) {
        mSellerId = sellerId;
    }

    public String getBuyerName() {
        return mBuyerName;
    }

    public void setBuyerName(String buyerName) {
        mBuyerName = buyerName;
    }

    public String getSellerName() {
        return mSellerName;
    }

    public void setSellerName(String sellerName) {
        mSellerName = sellerName;
    }

    public String getBuyerImage() {
        return mBuyerImage;
    }

    public void setBuyerImage(String buyerImage) {
        mBuyerImage = buyerImage;
    }

    public String getSellerImage() {
        return mSellerImage;
    }

    public void setSellerImage(String sellerImage) {
        mSellerImage = sellerImage;
    }

    public ArrayList<ChatContent> getChatContentArrayList() {
        return mChatContentArrayList;
    }

    public void setChatContentArrayList(ArrayList<ChatContent> chatContentArrayList) {
        mChatContentArrayList = chatContentArrayList;
    }
}
