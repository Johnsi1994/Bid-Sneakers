package com.johnson.bid.auction.auctionitem;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.data.Product;

import java.util.ArrayList;

public interface AuctionItemContract {

    interface View extends BaseView<Presenter> {
        void showAuctionUi(ArrayList<Product> productList);
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


    }

}
