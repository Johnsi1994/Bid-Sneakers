package com.johnson.bid.trade;

import android.support.annotation.NonNull;

import com.johnson.bid.trade.TradeItem.TradeItemFragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class TradePresenter implements TradeContract.Presenter {

    private final TradeContract.View mtradeView;

    public TradePresenter(@NonNull TradeContract.View tradeView) {
        mtradeView = checkNotNull(tradeView, "tradeView cannot be null!");
    }

    @Override
    public void start() {

    }

    @Override
    public TradeItemFragment findMyBidding() {
        return null;
    }

    @Override
    public TradeItemFragment findMySelling() {
        return null;
    }

    @Override
    public TradeItemFragment findMyBought() {
        return null;
    }

    @Override
    public TradeItemFragment findMySold() {
        return null;
    }

}
