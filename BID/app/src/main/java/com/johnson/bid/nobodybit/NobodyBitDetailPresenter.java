package com.johnson.bid.nobodybit;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class NobodyBitDetailPresenter implements NobodyBitDetailContract.Presenter {

    private NobodyBitDetailContract.View mNobodyBitView;

    public NobodyBitDetailPresenter(@NonNull NobodyBitDetailContract.View nobodyBitView) {
        mNobodyBitView = checkNotNull(nobodyBitView, "nobodyBitView cannot be null!");
    }

    @Override
    public void start() {

    }
}
