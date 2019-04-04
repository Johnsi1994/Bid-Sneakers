package com.johnson.bid.data;

public class ChatContent {

    private long mUserId;
    private String mAuthor;
    private String mContent;
    private long mTime;

    public ChatContent() {
        mUserId = -1;
        mAuthor = "";
        mContent = "";
        mTime = -1;
    }

    public long getUserId() {
        return mUserId;
    }

    public void setUserId(long userId) {
        mUserId = userId;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }
}
