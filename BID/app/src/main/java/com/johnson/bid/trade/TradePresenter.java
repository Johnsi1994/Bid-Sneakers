package com.johnson.bid.trade;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class TradePresenter implements TradeContract.Presenter {

    private final TradeContract.View mtradeView;

    public TradePresenter(@NonNull TradeContract.View tradeView) {
        mtradeView = checkNotNull(tradeView, "tradeView cannot be null!");
    }

    @Override
    public void start() {

    }
}
