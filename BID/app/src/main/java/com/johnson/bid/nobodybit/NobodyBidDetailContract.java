package com.johnson.bid.nobodybit;

import com.johnson.bid.BasePresenter;
import com.johnson.bid.BaseView;
import com.johnson.bid.data.Product;

public interface NobodyBidDetailContract {

    interface View extends BaseView<Presenter> {
        void showNobodyBidDetailUi(Product product);
    }

    interface Presenter extends BasePresenter {

        void showToolbarAndBottomNavigation();

        void setNobodyBitDetailData(Product product);

        void loadNobodyBidDetailData();

    }
}
