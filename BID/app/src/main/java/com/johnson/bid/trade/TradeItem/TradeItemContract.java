package com.johnson.bid.trade.TradeItem;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.data.Product;
import com.johnson.bid.login.LoginContract;

import java.util.ArrayList;

public interface TradeItemContract {

    interface View extends BaseView<Presenter> {
        void showTradeUi(ArrayList<Product> productsList);
    }

    interface Presenter extends BasePresenter {

        void loadMyBiddingData();

        void setMyBiddingData(ArrayList<Product> productsList);

        void loadMySellingData();

        void setMySellingData(ArrayList<Product> productsList);

        void loadMyBoughtData();

        void setMyBoughtData(ArrayList<Product> productsList);

        void loadMySoldData();

        void setMySoldData(ArrayList<Product> productsList);

        void loadNobodyBidData();

        void setNobodyBidData(ArrayList<Product> productsList);

        void hideToolbarAndBottomNavigation();

        void openBidding(String auctionType, Product product);

        void openSelling(String auctionType, Product product);

        void openBoughtDetail(Product product);

        void openSoldDetail(Product product);

        void openNobodyBidDetail(Product product);

        void setBuyerHasRead(boolean hasRead, Product product);

        void setSellerHasRead(boolean hasRead, Product product, int from);

        void minusNobodyBidBadgeCount(TradeItemPresenter.LoadCallback loadCallback);

        void minusSoldBadgeCount(TradeItemPresenter.LoadCallback loadCallback);

        void minusBoughtBadgeCount(TradeItemPresenter.LoadCallback loadCallback);

        void loadNobodyBidBadgeData();

        void loadSoldBadgeData();

        void loadBoughtBadgeData();

        void updateTradeBadge();

    }

}
