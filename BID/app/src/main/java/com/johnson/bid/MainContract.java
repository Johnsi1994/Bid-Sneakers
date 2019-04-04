package com.johnson.bid;

import com.johnson.bid.centre.auction.AuctionFragment;

public interface MainContract {

    interface View extends BaseView<Presenter> {
        void openCenterUi();

        void openTradeUi();

        void openChatUi();

        void openSettingsUi();

        AuctionFragment findEnglishAuctionView();

        AuctionFragment findSealedAuctionView();

        void setToolbarTitleUi(String title);
    }

    interface Presenter extends BasePresenter {
        void openCenter();

        void openTrade();

        void openChat();

        void openSettings();

        void updateToolbar(String title);
    }
}
