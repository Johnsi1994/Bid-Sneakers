package com.johnson.bid;

import android.graphics.Bitmap;

import com.johnson.bid.centre.auction.AuctionFragment;
import com.johnson.bid.data.Product;
import com.johnson.bid.dialog.MessageDialog;
import com.johnson.bid.trade.TradeItem.TradeItemFragment;

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

        TradeItemFragment findMyBiddingView();

        TradeItemFragment findMySellingView();

        TradeItemFragment findMyBoughtView();

        TradeItemFragment findMySoldView();

        void findBiddingView(String auctionType, Product product);

        void setToolbarTitleUi(String title);

        void hideToolbarUi();

        void showToolbarUi();

        void hideBottomNavigationUi();

        void showBottomNavigationUi();

        void showMessageDialogUi(@MessageDialog.MessageType int type);
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
