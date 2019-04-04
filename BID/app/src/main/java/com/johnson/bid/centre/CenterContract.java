package com.johnson.bid.centre;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.centre.auction.AuctionFragment;

public interface CenterContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        AuctionFragment findEnglishAuction();

        AuctionFragment findSealedAuction();
    }
}
