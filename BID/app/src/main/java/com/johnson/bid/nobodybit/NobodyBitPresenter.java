package com.johnson.bid.nobodybit;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class NobodyBitPresenter implements NobodyBitContract.Presenter {

    private NobodyBitContract.View mNobodyBitView;

    public NobodyBitPresenter(@NonNull NobodyBitContract.View nobodyBitView) {
        mNobodyBitView = checkNotNull(nobodyBitView, "nobodyBitView cannot be null!");
    }

    @Override
    public void start() {

    }
}
