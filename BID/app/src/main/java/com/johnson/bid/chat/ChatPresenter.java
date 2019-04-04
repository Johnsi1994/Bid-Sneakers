package com.johnson.bid.chat;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatPresenter implements ChatContract.Presenter {

    private final ChatContract.View mChatView;

    public ChatPresenter(@NonNull ChatContract.View chatView) {
        mChatView = checkNotNull(chatView, "chatView cannot be null!");
    }

    @Override
    public void start() {

    }
}
