package com.johnson.bid.bought;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class BoughtDetailPresenter implements BoughtDetailContract.Presenter{

    private BoughtDetailContract.View mBoughtView;

    public BoughtDetailPresenter(@NonNull BoughtDetailContract.View boughtView) {
        mBoughtView = checkNotNull(boughtView, "boughtView cannot be null!");
    }

    @Override
    public void start() {

    }
}
