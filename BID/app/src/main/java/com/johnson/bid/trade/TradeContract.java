package com.johnson.bid.trade;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.trade.TradeItem.TradeItemFragment;

public interface TradeContract {

    interface View extends BaseView<Presenter> {

        void showBoughtBadgeUI(int unreadBought);

        void showSoldBadgeUI(int unreadSold);

        void showNobodyBidBadgeUI(int unreadNobodyBid);

    }

    interface Presenter extends BasePresenter {

        TradeItemFragment findMyBidding();

        TradeItemFragment findMySelling();

        TradeItemFragment findMyBought();

        TradeItemFragment findMySold();

        TradeItemFragment findNobodyBid();

        void loadBoughtBadgeData();

        void setBoughtBadgeData(int unreadBought);

        void loadSoldBadgeData();

        void setSoldBadgeData(int unreadBought);

        void loadNobodyBidBadgeData();

        void setNobodyBidBadgeData(int unreadBought);

    }
}
