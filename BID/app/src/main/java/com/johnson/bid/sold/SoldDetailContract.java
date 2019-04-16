package com.johnson.bid.sold;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.data.Product;

public interface SoldDetailContract {

    interface View extends BaseView<Presenter> {

        void showSoldDetailUi(Product product);
    }

    interface Presenter extends BasePresenter {

        void setSoldDetailData(Product product);

        void loadSoldDetailData();

        void showToolbarAndBottomNavigation();

    }

}
