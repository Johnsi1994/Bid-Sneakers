package com.johnson.bid;

import com.johnson.bid.centre.auction.AuctionFragment;

public interface MainContract {

    interface View extends BaseView<Presenter> {

        void openLoginUi();

        void openCenterUi();

        void openTradeUi();

        void openChatUi();

        void openSettingsUi();

        void openPostUi();

        AuctionFragment findEnglishAuctionView();

        AuctionFragment findSealedAuctionView();

        void setToolbarTitleUi(String title);

        void hideToolbarUi();

        void showToolbarUi();

        void hideBottomNavigationUi();

        void showBottomNavigationUi();
    }

    interface Presenter extends BasePresenter {

        void openLogin();

        void openCenter();

        void openTrade();

        void openChat();

        void openSettings();

        void updateToolbar(String title);

        void hideToolbarAndBottomNavigation();

        void showToolbarAndBottomNavigation();

        void hideBottomNavigation();

        void showBottomNavigation();
    }
}
