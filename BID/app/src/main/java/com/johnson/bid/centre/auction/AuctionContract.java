package com.johnson.bid.centre.auction;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.data.Product;

import java.util.ArrayList;

public interface AuctionContract {

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
    }

}
