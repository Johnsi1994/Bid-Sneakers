package com.johnson.bid.bought;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class BoughtPresenter implements BoughtContract.Presenter{

    private BoughtContract.View mBoughtView;

    public BoughtPresenter(@NonNull BoughtContract.View boughtView) {
        mBoughtView = checkNotNull(boughtView, "boughtView cannot be null!");
    }

    @Override
    public void start() {

    }
}
