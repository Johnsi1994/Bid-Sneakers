package com.johnson.bid.auction.auctionitem;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.data.Product;

import java.util.ArrayList;

public interface AuctionItemContract {

    interface View extends BaseView<Presenter> {
        void showAuctionUi(ArrayList<Product> productList);

        void showSellingFailUi(Product product, String from);

        void showSoldSuccessUi(Product product, String from);

        void showBoughtSuccessUi(Product product, String from);

        void showMyBiddingData();

        void showTradeBadge();
    }

    interface Presenter extends BasePresenter {

        void openBidding(String auctionType, Product product);

        void hideToolbarAndBottomNavigation();

        void loadEnglishData();

        void setEnglishData(ArrayList<Product> productList);

        void loadSealedData();

        void setSealedData(ArrayList<Product> productList);

        void loadMySoldData();

        void loadNobodyBidData();

        void loadMySellingData();

        void loadMyBiddingData();

        void loadMyBoughtData();

        void loadNobodyBidBadgeData();

        void loadSoldBadgeData();

        void loadBoughtBadgeData();

        void updateTradeBadge();

        void openSelling(String from, Product product);

        void removeSellingProductId(long productId, String from);

        void addNobodyBidProductId(long productId, String from);

        void addSoldProductId(long productId, String from);

        void removeBiddingProductId(long productId, String from);

        void addBoughtProductId(long productId, String from);

        void increaseUnreadNobodyBid(String from);

        void increaseUnreadSold(String from);

        void increaseUnreadBought(String from);

        void productResult(Product product, String from);

        void createChatRoom(Product product, String from);


    }

}
