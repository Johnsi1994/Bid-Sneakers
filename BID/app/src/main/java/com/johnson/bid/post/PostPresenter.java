package com.johnson.bid.post;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class PostPresenter implements PostContract.Presenter {

    private PostContract.View mPostView;

    public PostPresenter(@NonNull PostContract.View postView) {
        mPostView = checkNotNull(postView, "postView cannot be null!");
    }

    @Override
    public void start() {

    }
}
