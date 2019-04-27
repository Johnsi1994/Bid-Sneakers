package com.johnson.bid.data;

public class ChatBrief {

    private long mProductId;
    private String mProfileUrl;
    private String mPartnerName;
    private String mLastMessage;

    public ChatBrief() {

        mProductId = -1;
        mProfileUrl = "";
        mPartnerName = "";
        mLastMessage = "";

    }

    public String getProfileUrl() {
        return mProfileUrl;
    }

    public long getProductId() {
        return mProductId;
    }

    public void setProductId(long productId) {
        mProductId = productId;
    }

    public void setProfileUrl(String profileUrl) {
        mProfileUrl = profileUrl;
    }

    public String getPartnerName() {
        return mPartnerName;
    }

    public void setPartnerName(String partnerName) {
        mPartnerName = partnerName;
    }

    public String getLastMessage() {
        return mLastMessage;
    }

    public void setLastMessage(String lastMessage) {
        mLastMessage = lastMessage;
    }
}
