package com.johnson.bid.bought;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.data.Product;

public interface BoughtDetailContract {

    interface View extends BaseView<Presenter> {
        void showBoughtDetailUi(Product product);
    }

    interface Presenter extends BasePresenter {

        void showToolbarAndBottomNavigation();

        void loadBoughtDetailData();

        void setBoughtDetailData(Product product);

    }

}
