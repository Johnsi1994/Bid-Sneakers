package com.johnson.bid.data;

public class ChatContent {

    private long mTime;
    private long mAuthorId;
    private String mAuthorImage;
    private String mMessage;

    public ChatContent() {

        mAuthorId = -1;
        mTime = -1;
        mMessage = "";
        mAuthorImage = "";
    }

    public long getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(long authorId) {
        mAuthorId = authorId;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getAuthorImage() {
        return mAuthorImage;
    }

    public void setAuthorImage(String authorImage) {
        mAuthorImage = authorImage;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }
}
