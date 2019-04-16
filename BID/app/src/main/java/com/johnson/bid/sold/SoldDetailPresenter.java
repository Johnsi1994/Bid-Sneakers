package com.johnson.bid.sold;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class SoldDetailPresenter implements SoldDetailContract.Presenter {

    private SoldDetailContract.View mSoldView;

    public SoldDetailPresenter(@NonNull SoldDetailContract.View soldView) {
        mSoldView = checkNotNull(soldView, "soldView cannot be null!");
    }

    @Override
    public void start() {

    }
}
