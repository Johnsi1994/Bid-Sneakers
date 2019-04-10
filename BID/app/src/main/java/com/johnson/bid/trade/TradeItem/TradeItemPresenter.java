package com.johnson.bid.trade.TradeItem;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class TradeItemPresenter implements TradeItemContract.Presenter {

    private TradeItemContract.View mTradeItemView;

    public TradeItemPresenter(@NonNull TradeItemContract.View tradeItemView) {
        mTradeItemView = checkNotNull(tradeItemView, "tradeItemView cannot be null!");
    }

    @Override
    public void start() {

    }

}
