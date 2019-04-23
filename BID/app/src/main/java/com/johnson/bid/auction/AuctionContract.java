package com.johnson.bid.auction;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.auction.auctionitem.AuctionItemFragment;

public interface AuctionContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        AuctionItemFragment findEnglishAuction();

        AuctionItemFragment findSealedAuction();

        void openGalleryDialog(String from);

        void openEyesOn(String toolbarTitle);

        void openSearchDialog();

        void hideBottomNavigation();
    }
}
