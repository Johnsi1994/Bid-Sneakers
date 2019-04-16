package com.johnson.bid.trade;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.trade.TradeItem.TradeItemFragment;

public interface TradeContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        TradeItemFragment findMyBidding();

        TradeItemFragment findMySelling();

        TradeItemFragment findMyBought();

        TradeItemFragment findMySold();

        TradeItemFragment findNobodyBid();
    }
}
