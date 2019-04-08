package com.johnson.bid;

import android.graphics.Bitmap;

import com.johnson.bid.centre.auction.AuctionFragment;

import java.util.ArrayList;

public interface MainContract {

    interface View extends BaseView<Presenter> {

        void openLoginUi();

        void openCenterUi();

        void openTradeUi();

        void openChatUi();

        void openSettingsUi();

        void openPostUi(ArrayList<String> imagePath);

        void setPostPics(ArrayList<String> imagePath);

        void openGallery(String from);

        void openCamera(String from);

        void openGalleryDialog(String from);

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

        void openPost(String toolbarTitle, ArrayList<String> imagePath);

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
