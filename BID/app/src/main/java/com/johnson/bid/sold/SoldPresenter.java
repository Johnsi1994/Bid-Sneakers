package com.johnson.bid.sold;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class SoldPresenter implements SoldContract.Presenter {

    private SoldContract.View mSoldView;

    public SoldPresenter(@NonNull SoldContract.View soldView) {
        mSoldView = checkNotNull(soldView, "soldView cannot be null!");
    }

    @Override
    public void start() {

    }
}
