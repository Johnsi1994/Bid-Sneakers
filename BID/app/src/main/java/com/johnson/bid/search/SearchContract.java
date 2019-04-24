package com.johnson.bid.search;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.data.Product;

import java.util.ArrayList;

public interface SearchContract {

    interface View extends BaseView<Presenter> {
        void showSearchUi(ArrayList<Product> products);
    }

    interface Presenter extends BasePresenter {

        void updateToolbar(String toolbarTitle);

        void openBidding(String from, Product product);

        void hideToolbarAndBottomNavigation();

        void loadSearchData();

        void setKeyword(String keyword);

        void showBottomNavigation();
    }

}
