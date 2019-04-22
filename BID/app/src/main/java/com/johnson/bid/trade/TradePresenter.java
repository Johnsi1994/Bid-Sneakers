package com.johnson.bid.trade;

import android.support.annotation.NonNull;
import android.util.Log;

import com.johnson.bid.trade.TradeItem.TradeItemFragment;
import com.johnson.bid.util.UserManager;

import static com.google.common.base.Preconditions.checkNotNull;

public class TradePresenter implements TradeContract.Presenter {

    private final TradeContract.View mTradeView;

    public TradePresenter(@NonNull TradeContract.View tradeView) {
        mTradeView = checkNotNull(tradeView, "tradeView cannot be null!");
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

    @Override
    public TradeItemFragment findNobodyBid() {
        return null;
    }

    @Override
    public void loadBoughtBadgeData() {
        int unreadBought = UserManager.getInstance().getUser().getUnreadBought();
        setBoughtBadgeData(unreadBought);
    }

    @Override
    public void setBoughtBadgeData(int unreadBought) {
        mTradeView.showBoughtBadgeUI(unreadBought);
    }

    @Override
    public void loadSoldBadgeData() {
        int unreadSold = UserManager.getInstance().getUser().getUnreadSold();
        setSoldBadgeData(unreadSold);
    }

    @Override
    public void setSoldBadgeData(int unreadSold) {
        mTradeView.showSoldBadgeUI(unreadSold);
    }

    @Override
    public void loadNobodyBidBadgeData() {
        int unreadNobodyBid = UserManager.getInstance().getUser().getUnreadNobodyBid();
        setNobodyBidBadgeData(unreadNobodyBid);
    }

    @Override
    public void setNobodyBidBadgeData(int unreadNobodyBid) {
        mTradeView.showNobodyBidBadgeUI(unreadNobodyBid);
    }


}
