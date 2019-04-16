package com.johnson.bid.selling;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.data.Product;

public interface SellingContract {

    interface View extends BaseView<Presenter> {

        void showSellingDetailUi(Product product);

    }

    interface Presenter extends BasePresenter {

        void showToolbarAndBottomNavigation();

        void setSellingDetailData(Product product);

        void loadSellingDetailData();



    }

}
