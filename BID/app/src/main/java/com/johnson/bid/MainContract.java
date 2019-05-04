package com.johnson.bid;

import android.graphics.Bitmap;

import com.johnson.bid.auction.auctionitem.AuctionItemFragment;
import com.johnson.bid.data.ChatRoom;
import com.johnson.bid.data.Product;
import com.johnson.bid.dialog.MessageDialog;
import com.johnson.bid.trade.TradeItem.TradeItemFragment;

import java.util.ArrayList;

public interface MainContract {

    interface View extends BaseView<Presenter> {

        void setBottomNavigation();

        void openLoginUi();

        void openCenterUi();

        void openTradeUi();

        void openChatUi();

        void openSettingsUi();

        void openPostUi(ArrayList<Bitmap> imageBitmap);

        void openSearchUi(String keyword);

        void openEyesOnUi();

        void openChatContentUi(ChatRoom chatRoom, String from);

        void openSearchDialog();

        void setPostPics(ArrayList<Bitmap> imageBitmap);

        void setAfterBidData(Product product);

        void openGallery(String from);

        void openCamera(String from);

        void openGalleryDialog(String from);

        void setSettingsProfile(Bitmap imageBitmap);

        AuctionItemFragment findEnglishAuctionView();

        AuctionItemFragment findSealedAuctionView();

        TradeItemFragment findMyBiddingView();

        TradeItemFragment findMySellingView();

        TradeItemFragment findMyBoughtView();

        TradeItemFragment findMySoldView();

        TradeItemFragment findNobodyBidView();

        void findBiddingView(String auctionType, Product product);

        void findSellingView(String auctionType, Product product);

        void findBoughtDetailView(Product product);

        void findSoldDetailView(Product product);

        void findNobodyBidDetailView(Product product);

        void setToolbarTitleUi(String title);

        void hideToolbarUi();

        void showToolbarUi();

        void hideBottomNavigationUi();

        void showBottomNavigationUi();

        void openBidDialog(String from, Product product);

        void openDeleteProductDialog(Product product);

        void showMessageDialogUi(@MessageDialog.MessageType int type);

        void updateTradeBadgeUi(int unreadCount);
    }

    interface Presenter extends BasePresenter {

        void openLogin();

        void openPost(String toolbarTitle, ArrayList<Bitmap> imageBitmap);

        void openCenter();

        void openTrade();

        void openChat();

        void openSettings();

        void updateToolbar(String title);

        void hideToolbarAndBottomNavigation();

        void showToolbarAndBottomNavigation();

        void hideBottomNavigation();

        void showBottomNavigation();

        void updateTradeBadge();

        void openSearch(String toolbarTitle, String keyWord);
    }
}
