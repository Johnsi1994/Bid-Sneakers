package com.johnson.bid.bidding;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.data.Product;

public interface BiddingDetailContract {

    interface View extends BaseView<Presenter> {
        void showBiddingUi(Product product);
    }

    interface Presenter extends BasePresenter {

        void showToolbarAndBottomNavigation();

        void setProductData(Product product);

        void loadProductData();

        void openBidDialog(String from, Product product);

        void updateAuctionData();

        void loadBiddingFreshData();
    }

}
